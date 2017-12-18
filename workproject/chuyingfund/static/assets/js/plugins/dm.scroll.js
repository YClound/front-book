$(function () {
    var $w = $(window)
    $.fn.inview = function (callback, options) {
        var defaultOptions = {
                threshold: 0
            },
            o = $.extend({}, defaultOptions, options),
            $this = this,
            loaded
        var tick = function () {
            var wt = $w.scrollTop(),
                wb = wt + $w.height()
            var $inview = $this.filter(function () {
                var $e = $(this)
                if ($e.is(':hidden')) {
                    return false
                }
                var et = $e.offset().top,
                    eb = et + $e.height()
                return eb >= wt - o.threshold && et <= wb + o.threshold
            })
            loaded = $inview.trigger('inview')
            $this = $this.not(loaded)
        }
        this.one('inview', callback)
        $w.on('scroll.inview resize.inview lookup.inview', tick)
        tick()
        return this
    }
    $.fn.headroom = function (options) {
        var defaultOption = {
                offset: 200,
                offsetBottom: 10,
                tolerance: 20,
                unpinnedClass: 'headroom--unpinned',
                bottom: false
            },
            timer = 0,
            y = 0,
            $this = this.addClass('headroom')
        var o = $.extend({}, defaultOption, options)

        var checkTolerance = function () {
            var wt = $w.scrollTop(),
                check = function () {
                    if ((wt - y) > o.tolerance) {
                        $this.addClass(o.unpinnedClass)
                    } else if ((y - wt) > o.tolerance) {
                        $this.removeClass(o.unpinnedClass)
                    }
                }
            //if (wt < o.offset || (maxScroll - wt) <= o.offsetBottom) $this.removeClass(o.unpinnedClass)
            if (wt < o.offset) $this.removeClass(o.unpinnedClass)
            else check()

            y = wt
            timer = 0
        }
        var tick = function () {
            if (!timer) {
                y = $w.scrollTop()
                timer = setTimeout(checkTolerance, 100)
            }

        }
        $w.on('scroll.headroom resize.headroom lookup.headroom', tick)
        return this
    }
    $.fn.simpleStick = function (options) {
        var defaultOption = {
            offset: 200,
            stickClass: 'stick'
        }, $this = this
        var o = $.extend({}, defaultOption, options)
        var tick = function () {
            var wt = $w.scrollTop()
            if (wt < o.offset) {
                $this.removeClass(o.stickClass)
            } else {
                $this.addClass(o.stickClass)
            }
        }
        $w.on('scroll.headroom resize.headroom lookup.headroom', tick)
        return this
    }
    $.fn.stick = function (options) {
        var $this = this,
            $parent = $this.parent(),
            offsetList = [],
            bottomList = [],
            oriCSS = [],
            defaultOptions = {
                offset: 0,
                hasBottom: true
            }
        var o = $.extend({}, defaultOptions, options)
        this.each(function (i) {
            var $elm = $(this),
                $wrap = $elm.parent()
            if ($wrap.css('position') === 'static') $wrap.css('position', 'relative')
            offsetList[i] = $elm.offset()
            oriCSS[i] = {
                position: $elm.css('position'),
                left: $elm.css('left'),
                top: $elm.css('top'),
                bottom: $elm.css('bottom')
            }
            bottomList[i] = $wrap.offset().top + $wrap.height() - $elm.height()
        })
        var tick = function () {
            var wt = $w.scrollTop()
            for (var i = 0, len = offsetList.length; i < len; i++) {
                if (wt > (offsetList[i].top - o.offset)) {
                    if (o.hasBottom && wt > bottomList[i]) {
                        $this.eq(i).css({
                            position: 'absolute',
                            left: oriCSS[i].left,
                            top: 'auto',
                            bottom: 0
                        }).addClass('stick')
                        $parent.addClass('stick')
                    } else {
                        $this.eq(i).css({
                            position: 'fixed',
                            left: offsetList[i].left,
                            top: o.offset,
                            bottom: 'auto'
                        }).addClass('stick')
                        $parent.addClass('stick')
                    }
                } else {
                    $this.eq(i).css(oriCSS[i]).removeClass('stick')
                    $parent.removeClass("stick")
                }
            }
        }
        $w.on('scroll.stick resize.stick lookup.stick', tick)
        tick()
        return this
    }
    $.fn.scrollAnchor = function (options) {
        var defaultOptions = {
                threshold: 20
            },
            o = $.extend({}, defaultOptions, options)
        var $wrap = $(this),
            $anchorList = $(),
            offsetList = []
        $wrap.find("[href^='#']").each(function () {
            var $anchor = $(this),
                $target = $($anchor.attr('href'))
            if ($target.length) {
                $anchorList = $anchorList.add($anchor)
                offsetList.push($target.offset().top)
            }
        })
        var tick = function () {
            var wt = $w.scrollTop(),
                targetIndex = 0
            for (var i = 0, len = offsetList.length; i < len; i++) {
                if (wt > offsetList[i] - o.threshold) {
                    targetIndex = i
                }
            }
            $anchorList.removeClass('active').eq(targetIndex).addClass('active')
        }
        $w.on('scroll.anchor resize.anchor lookup.anchor', tick)
        tick()
        return this
    }
    // 滚动加载更多
    $.fn.infiniteLoading = function (options) {
        var $wrap = $(this),
            options = options || {},
            $loading = $wrap.find('.loading'),
            $form = $wrap.find('form'),
            $list = $($wrap.data('listWrap')),
            $offset = $form.find('[data-name=offset]').length?$form.find('[data-name=offset]'):$form.find('[name=offset]'),
            // 第几页数据
            offset = parseInt($offset.data('init'), 10) || 2,
            type = $wrap.data('type') || 'html',
            parseData = options.parseData || function(){}
            // 总页数
            totalPage = $wrap.data('totalPage')

        $offset.val(offset)
        if (type === 'json') {
            var templateId = $wrap.data('template')
        }


        var init = function () {
            if (totalPage === 0) { // 无记录
                $wrap.addClass('blank')
                return
            }
            if (totalPage != null && offset > totalPage) {
                $wrap.addClass('end')
                if (offset <= 2) { // 只有一页
                    $wrap.hide()
                }
            } else {
                $loading.inview(function () {
                    $form.submit().one('success', function (e, res) {
                        
                        offset += 1;
                        $offset.val(offset);
                        if (type === 'html') {
                            $list.append(res)
                            if (res) {
                                init()
                            } else {
                                $wrap.addClass('end')
                            }
                        } else if (type === 'json') {

                            var html = '',
                                list = res.data.list
                            if (list == null || list.length === 0) {
                                $wrap.addClass('end')
                            } else {
                                parseData(list)
                                for (var i = 0, len = list.length; i < len; i++) {
                                    html += template(templateId, list[i])
                                }
                                $list.append(html)
                                totalPage = res.data.page.total_page
                                init()
                            }
                        }
                    })
                }, {threshold: 300})
            }
        }
        init()
    }
    // 点击加载更多 bb = true 立刻加载第一页
    $.fn.moreLoading = function (bb) {
        
        return this.each(function(){
            var $wrap = $(this),
                callback = typeof bb == 'function'?bb:function(){},
                options = typeof bb == 'object'?bb:{},
                parseData = options.parseData || function(){},
                $btn = $wrap.find('.more'),
                $loading = $wrap.find('.loading'),
                $loadingTxt = $wrap.find('.loading-text'),
                $endText = $wrap.find('.end-text'),
                $form = $wrap.find('form'),
                $list = $($wrap.data('listWrap')),
                $offset = $form.find('[data-name=offset]'),
                // 第几页数据 默认加载第二页
                offset = parseInt($offset.data('init'), 10) || 2,
                offset = bb?1:2,
                type = $wrap.data('type') || 'json',
                // 总页数
                totalPage = $wrap.data('totalPage'),
                _totalPage = parseInt(totalPage) || 0;

            
            $offset.val(offset)
            if (type === 'json') {
                var templateId = $wrap.data('template')
            }
            //console.log(12,_totalPage,$wrap)
            var init = function(){
                if (_totalPage === 0) { // 无记录
                    $wrap.addClass('blank')
                    $btn.hide()
                    $loadingTxt.hide()
                    $endText.show();
                    return
                }
                if (_totalPage != null && offset > _totalPage) {
                    $wrap.addClass('end')
                    $btn.hide()
                    $loadingTxt.hide()
                    $endText.show();
                    if (offset <= 2) { // 只有一页
                        //$wrap.hide()
                    }
                }
            };

            var loading = function () {
                $loadingTxt.show();
                $btn.hide();
                $endText.hide();
                $form.submit().one('success', function (e, res) {
                    offset += 1;
                    $offset.val(offset);
                    if (type === 'html') {
                        $list.append(res)
                        if (res) {
                            init()
                        } else {
                            $wrap.addClass('end')
                        }
                    } else if (type === 'json') {

                        var html = '',
                            list = res.data.list
                        if (list == null || list.length === 0) {
                            $wrap.addClass('end')
                        } else {
                            // 解析数据
                            parseData(list);
                            for (var i = 0, len = list.length; i < len; i++) {
                                html += template(templateId, list[i])
                            }
                            $list.append(html)
                            totalPage = res.data.page.total_page
                            //init()
                        }
                    }
                    callback && callback(res);

                    if(offset > totalPage){
                        $btn.hide();
                        $loadingTxt.hide();
                        $endText.show();
                    }else {
                        $btn.show();
                        $loadingTxt.hide();
                        $endText.hide();
                    }
                        
                }).one('fail',function(){
                    $loadingTxt.hide();
                    $btn.show();
                    $endText.hide();
                });
                
            }
            
            init();
            
            //

            $btn.on('click',function(){
               
                loading();
            });
        });
            
        
    }
    
    // 分页异步加载
    $.fn.pageLoading = function (options) {
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
                  html += template('projItem', item)
                }
                $pagedContent.html(html);
                // 返回顶部
                $('html,body').animate({
                    scrollTop: 0
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
                            console.log('num',num)
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




    $.fn.backtop = function () {
        var $backtop = $(this).on('click', function () {
            $('html,body').stop(true, false).animate({
                scrollTop: 0
            }, 400);
        })
        var tick = function () {
            var wt = $w.scrollTop()

            if (wt > 600) {
                $backtop.stop(true, false).fadeIn()
            } else {
                $backtop.stop(true, false).fadeOut()
            }
        }
        $w.on('scroll.backtop resize.backtop lookup.backtop', tick)
        tick()
    }

    var retina = window.devicePixelRatio > 1
    $('img[data-src]').inview(function () {
        var $this = $(this),
            src
        if (retina) src = $this.data('srcRetina') || $this.data('src')
        else src = $this.data('src')
        return $this.attr('src', src)
    })
    $('.infinite-loading').not('.manual').infiniteLoading()
    


    $('.backtop').backtop()

})



