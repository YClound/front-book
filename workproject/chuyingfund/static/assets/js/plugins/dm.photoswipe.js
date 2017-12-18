define(function(require, exports, module){
    require('photoswipe');
    require('photoswipe-ui');
    require('photoswipe.css');
    require('photoswipe-ui.css');

    var TPL = '<div class="pswp" tabindex="-1" role="dialog" aria-hidden="true">' +
            '<div class="pswp__bg"></div>'+
            '<div class="pswp__scroll-wrap">'+
                '<div class="pswp__container">'+
                    '<div class="pswp__item"></div>'+
                    '<div class="pswp__item"></div>'+
                    '<div class="pswp__item"></div>'+
                '</div>'+
                '<div class="pswp__ui pswp__ui--hidden">'+
                    '<div class="pswp__top-bar">'+
                        '<div class="pswp__counter"></div>'+
                        '<button class="pswp__button pswp__button--close" title="Close (Esc)"></button>'+
                        '<button class="pswp__button pswp__button--fs" title="Toggle fullscreen"></button>'+
                        '<button class="pswp__button pswp__button--zoom" title="Zoom in/out"></button>'+
                        '<div class="pswp__preloader">'+
                            '<div class="pswp__preloader__icn">'+
                              '<div class="pswp__preloader__cut">'+
                                '<div class="pswp__preloader__donut"></div>'+
                              '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                    '<div class="pswp__share-modal pswp__share-modal--hidden pswp__single-tap">'+
                        '<div class="pswp__share-tooltip"></div>' +
                    '</div>'+
                    '<button class="pswp__button pswp__button--arrow--left" title="Previous (arrow left)"></button>'+
                    '<button class="pswp__button pswp__button--arrow--right" title="Next (arrow right)"></button>'+
                    '<div class="pswp__caption">'+
                        '<div class="pswp__caption__center"></div>'+
                    '</div>'+
                '</div>'+
            '</div>'+
        '</div>';

    $(function(){
        $('body').append(TPL);
        var Util = {
            //获取图片宽 和 高
            imgReady: function( url, success, error ) {
                var width, height, intervalId, check, div,
                    img = new Image(),
                    body = document.body;
                    
                img.src = url;
                
                // 从缓存中读取
                if (img.complete) {
                    return success(img.width, img.height);
                }
                // 通过占位提前获取图片头部数据
                if (body) {
                    div = document.createElement('div');
                    div.style.cssText = 'position:absolute;left:-99999px;top:0;';
                    div.appendChild(img);
                    body.appendChild(div);
                    width = img.offsetWidth;
                    height = img.offsetHeight;
                   
                    check = function () {
                       
                        if (img.offsetWidth !== width || img.offsetHeight !== height) {
                            
                            clearInterval(intervalId);
                            success(img.offsetWidth, img.clientHeight);
                            img.onload = null;
                            div.innerHTML = '';
                            div.parentNode.removeChild(div);
                        };

                    };
                    
                    intervalId = setInterval(check, 150);
                }
                
                // 加载完毕后方式获取
                img.onload = function () {
                    if(img.complete){
                        success(img.width, img.height);
                        img.onload = img.onerror = null;
                        clearInterval(intervalId);
                        body && img.parentNode &&img.parentNode.removeChild(img);
                    }
                        
                };
                
                // 图片加载错误
                img.onerror = function () {
                    error && error();
                    clearInterval(intervalId);
                    body && img.parentNode && img.parentNode.removeChild(img);
                };
            }
        };
        $('body').on('click', '[data-role="photoItem"]', function(e){
            e.preventDefault();
            var items = [];
            var $photoItem = $(e.currentTarget);
            var $photoList = $photoItem.closest('[data-role="photoList"]');
            var isSize = false;
            if(!$photoList.length){
                $photoList = $photoItem.parent();
            }

            $photoList.find('[data-role="photoItem"]').each(function(i,item){
                var $photoItem = $(this);
                var pic = $photoItem.attr('data-original');
                var size = $photoItem.attr('data-size') || '';
                var w,h,arr;
                $photoItem.attr('photo-index',i);
                $(this).attr('photo-index',i);
                //console.log(pic)
                if(size) {
                    arr = size.split('x');
                    w = arr[0];
                    h = arr[1];
                    isSize = true;
                }

                items.push({
                    src:pic,
                    w: w,
                    h: h
                });
            });

            
            
            var index = $photoItem.attr('photo-index') || 0;
            var path = $photoItem.attr('data-original')  || '';
            index = parseInt(index);
            
            
            var pswpElement = document.querySelectorAll('.pswp')[0];

            // define options (if needed)
            var options = {
                index:index,
                history: false,
                focus: false,
                showAnimationDuration: 0,
                hideAnimationDuration: 0
                
            };
            //console.log(pswpElement, PhotoSwipeUI_Default, items, options)
            //console.log(options,items)
            var gallery = new PhotoSwipe( pswpElement, PhotoSwipeUI_Default, items, options);
            // Image loaded
            gallery.listen('imageLoadComplete', function(index, item) { 
                // index - index of a slide that was loaded
                // item - slide object
                 //console.log('imageLoadComplete',index)
            });

            // 假如有图片尺寸则直接初始化相册
            if(isSize) {
                gallery.init();
                return;
            }



            var count = 0;
            var error = 0
            var tick = function(){
                
                if(count === items.length) {
                    if(error){
                        dm.alert('图片加载失败');
                    }else {
                        gallery.init();
                    }
                    
                }
            }
            for (var i = items.length - 1; i >= 0; i--) {
                (function(item){
                    console.log(item.src)
                    Util.imgReady(item.src,function(w,h){
                        item.w = w;
                        item.h = h;
                        count++;
                        tick();
                    },function(){

                        count++;
                        error++;
                        tick();
                    });
                })(items[i]);
            }
            

            

        });
    });


});