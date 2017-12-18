!function () {
    $.fn.ajaxTab = function () {
        return this.each(function () {
            var $titleWrap = $(this)
            var $titles = $titleWrap.children(),
                $contentsWrap = $titleWrap.siblings('.tab-content')
            var $contents = $contentsWrap.children()

            // 初始化
            var $active = $titles.filter('.active')
            if(!$active.length){
                $titles.eq(0).addClass('active');
            }
            $contents.hide().eq($active.length ? $active.index() : 0).show().trigger('in')
            //$contentsWrap.css('min-height', $contentsWrap.height())

            // 点击事件
            $titles.on('click', function () {
                var $this = $(this)
                $titles.removeClass('active')
                $this.addClass('active')
                $contents.stop(true, false).hide().eq($this.index()).fadeIn().trigger('in')
                //$contentsWrap.css('min-height', $contentsWrap.height())
            })
        })
    };
    
    $(function(){
        $('.ajax.tab-content').children().on('fetch', function () {
            var $this = $(this)
            var $pagedContent = $this.find('.paged-content');

            dm.getJSON($this.data('ajax'), null, {alert: 'notice'}).done(function (json) {
                var data = json.data || '';
                var html = data;
                $pagedContent.html(html); 

            });
        }).on('in', function () {
            var $this = $(this)
            $this.trigger('fetch')
        });
    });


    var getTabHash = function () {
        var m;
        m = location.hash.match(/#\/([\w-]+)/);
        if (m) {
            return m[1];
        } else {
            return '';
        }
    }
    return $.fn.tab = function () {
        var preventHashChangeEvent, previousState;
        previousState = null;
        preventHashChangeEvent = false;
        $(window).off('hashchange.switchTab').on('hashchange.switchTab', function (event) {
            var targetTab;
            if (typeof history.replaceState === "function") {
                history.replaceState(previousState, '');
            }
            if (preventHashChangeEvent) {
                preventHashChangeEvent = false;
                return;
            }
            targetTab = getTabHash();
            if (targetTab) {
                $('.tab-title').find('[data-tab=' + targetTab + ']').eq(0).triggerHandler('click.switchTab');
            } else {
                $('.tab-title').has('[data-tab]').children().eq(0).triggerHandler('click.switchTab');
            }
        });
        return this.each(function () {
            var $activeTitle, $contents, $tabTitle, $titles, targetTab;
            $tabTitle = $(this);
            $titles = $tabTitle.children();
            $contents = $tabTitle.siblings('.tab-content').children();
            $titles.off('click.switchTab').on('click.switchTab', function () {
                var $targetTab, $this, targetTab;
                $this = $(this);
                $this.addClass('active').siblings().removeClass('active');
                $contents.hide();
                targetTab = $this.data('tab');
                $targetTab = $contents.filter('[data-tab=' + targetTab + ']');
                if (!$targetTab.length) {
                    $targetTab = $contents.eq($this.index());
                }
                return $targetTab.fadeIn(300).trigger('in');
            });
            $titles.off('click.changeHash').on('click.changeHash', function (event) {
                var targetTab;
                targetTab = $(this).data('tab');
                if (!event.isTrigger && targetTab) {
                    previousState = history.state;
                    if (location.hash !== '/' + targetTab) {
                        preventHashChangeEvent = true;
                    }
                    return location.hash = '/' + targetTab;
                }
            });
            targetTab = getTabHash();
            $activeTitle = [];
            if (targetTab) {
                $activeTitle = $titles.filter('[data-tab=' + targetTab + ']');
            }
            if (!$activeTitle.length) {
                $activeTitle = $titles.filter('.active');
            }
            if (!$activeTitle.length) {
                $activeTitle = $titles.eq(0);
            }
            return $activeTitle.triggerHandler('click.switchTab');
        });
    }



        


}();
