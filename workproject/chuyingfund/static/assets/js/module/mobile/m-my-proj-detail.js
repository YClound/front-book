define(function(require, exports, module) {
    require('template');
    require('webuploader');
    require('dm.wu');
    require('dm.photoswipe');
    require('/static/assets/js/module/mobile/m-project-common.js')
    require('/static/assets/js/plugins/raphael-min.js');
    require('countup');
    require('/static/assets/js/plugins/dm.circle.js');
    
    $(function () {
        $('#proj-header').headroom();
        // expandable
        $('.expandable').each(function () {
            
            var $wrap = $(this),
                $win = $wrap.find('.win'),
                $content = $wrap.find('.content'),
                $btn = $wrap.find('.btn-more')
            var height = $content.height(),
                winHeight = $win.height()
            // height < winHeight
            if (false) {
                $btn.hide()
            } else {
                $btn.on('click', function () {
                    
                    if ($btn.hasClass('expand')) { // 如果是展开则收缩
                        $win.stop(true, false).animate({height: winHeight})
                        $btn.removeClass('expand')
                    } else { // 如果是收缩则展开
                        $win.stop(true, false).animate({height: height})
                        $btn.addClass('expand')
                    }
                })
            }
        });

        // 项目融资百分比
        $('[data-limit="1"]').find('.percent-circle').circle().trigger('circle');


        


        // 查看商业计划书
        $('.btn-plan').on('click',function(e){
            e.preventDefault();
            dm.router.go('/plan');
        });

        // 查看项目动态
        $('.btn-news').on('click',function(e){
            e.preventDefault();
            dm.router.go('/news');
        });


        // 项目动态更多
        $('.more-page').morePage({
            parseData:function(list){
                var ret = [];

                for (var i = list.length - 1; i >= 0; i--) {
                    var item = window.parsePhotoData(list[i]);
                }
                
            }
        });
        dm.router(['/plan','/news'], function (path) {
            dm.view.show(path.replace(/^\//, ''));
        });
        
        
    });


    /*发布项目动态*/
    $(function(){

        var $modalFeed = $('.modal-feed'),$feedBody = $('#feed-table-body'),uploader;

        $modalFeed.on('shown', function () {
            
            $modalFeed.find('.wu').wu({
                fileNumLimit:9
            }).removeClass('hidden');
            $modalFeed.find('form').validate('init').find('input[maxlength], textarea[maxlength]').not('[nocount]').wordCount();
            
            uploader = $modalFeed.find('.wu').data('uploader');
            uploader.on('startUpload',function(){
                console.log('startUpload');
                window.lock = true;
            });
            uploader.on('uploadFinished',function(){
                console.log('uploadFinished');
                window.lock = false;
            });
            
            
        }).on('hidden', function () {
            $feedBody.empty();
            
        });

        $('.btn-publish').on('click',function(){
            $feedBody.html(template('feedPublish', {}));
        });
        $('.form-feed').on('boforeSend',function(e,res){
            $(this).find('#proid').val($("#projectId").val());
            $(this).data('error','');
            if(window.lock){
                dm.alert('图片还在上传中，请稍等');
                $(this).data('error',true);
                return;
            }
            //console.log('success')
        });
        $('.form-feed').on('success',function(e,res){
            var data = res.data || {};
            window.parsePhotoData(data);

            var html = template('feedItem', data);
            $('.feed-list').prepend(html);
            $modalFeed.modal('hide');
            dm.notice('发布成功');
            //console.log('success')
        });

            
            
    });



    
      
});