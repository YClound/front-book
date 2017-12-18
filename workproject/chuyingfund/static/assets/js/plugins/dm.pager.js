define(function(require, exports, module) {
    require('jqPaginator');
    // 分页异步加载
    $.fn.pager = function (options) {
        return this.each(function(){
            var $wrap = $this = $(this),
                options = options || {},
                $pager = $wrap.find('.pagination'),
                $form = $wrap.find('form'),
                $pagedContent = $($wrap.data('listWrap')),
                $offset = $form.find('[data-name=offset]'),
                // 第几页数据 
                offset = parseInt($offset.data('init'), 10) || 1,
                type = $wrap.data('type') || 'json',
                // 总页数
                totalPage = $wrap.data('totalPage'),
                _totalPage = parseInt(totalPage);

            $offset.val(offset)
            if (type === 'json') {
                var templateId = $wrap.data('template')
            }

            if (!_totalPage || _totalPage <= 1) {
                $pager.hide()
                return
            }

            $pager.show().jqPaginator({
                totalPages: parseInt(totalPage, 10) || 0,
                visiblePages: 10,
                currentPage: parseInt($offset.val()) || 1,
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);"><i class="arrow arrow2"><\/i>上一页<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页<i class="arrow arrow3"><\/i><\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',

                onPageChange: function (num, type) {
                    if (type === 'change') {
                        options.onPageChange && options.onPageChange(num);
                        console.log('num',num)
                        $offset.val(num)
                        $this.trigger('fetch')
                    }
                }
            
            });


            $form.on('success', function (e, res) {
                var data = res.data || {};
                var list = data.list || [];
                var html = '';
                if (!list) return;
                for (var i = 0, len = list.length; i < len; i++) {
                  var item = list[i]
                  html += template(templateId, item)
                }
                $pagedContent.html(html);
                // 返回顶部
                var top = $pagedContent.offset().top || 0;
                $('html,body').animate({
                    scrollTop: top
                }, 400);

                var pageInfo = data.page;
                if (!pageInfo.total_page || pageInfo.total_page <= 1) {
                    $pager.hide()
                    return
                }
                $pager.show().jqPaginator({
                    totalPages: parseInt(pageInfo.total_page, 10) || 0,
                    visiblePages: 10,
                    currentPage: parseInt($offset.val()) || parseInt(pageInfo.current_page, 10) || 1,
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);"><i class="arrow arrow2"><\/i>上一页<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">下一页<i class="arrow arrow3"><\/i><\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',

                    onPageChange: function (num, type) {
                        if (type === 'change') {
                            options.onPageChange && options.onPageChange(num);
                            //console.log('num',num)
                            $offset.val(num)
                            $this.trigger('fetch')
                        }
                    }
                
                });
                

            });

            $this.on('fetch',function(){
                $form.submit();
            });
        });
            

        //$this.trigger('fetch')
        
        
    }
    $(function(){
        /*$('img').each(function(){
            this.onerror = function(){
                console.log(1)
            };
        });*/
        /*console.log(2)
        $('img').one('error',function(){
            console.log('img error',this)
            var $img = $(this);
            if($img.data('type')==='avatar'){
                this.src = '/static/images/avatar.png';
            }
        });*/
    });
});