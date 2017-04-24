var sideMenu = function () {
    var $btn, $side, $icon, $wrap, $menuList, $subMenus
    return {
        init: function () {
            $side = $('.side-menu-wrap')
            $btn = $('.btn-view-menu')
            $icon = $btn.find('.icon-menu')
            $wrap = $('#container, #wrap')
            $wrap.height(window.innerHeight)
            $menuList = $side.find('.menu-list')
            $subMenus = $side.find('.sub-menu-list')

            $btn.on('click', function (e) {
                e.stopPropagation()
                $icon.addClass('menu-close')
                $wrap.addClass('slide-menu')
            })

            $('[data-sub-menu]').on('click', function (e) {
                e.stopPropagation()
                var $subMenu = $subMenus.eq($(this).data('subMenu'))
                if ($subMenu.hasClass('in')) {
                    $subMenus.removeClass('in')
                    $side.removeClass('sub')
                } else {
                    $subMenu.addClass('in')
                    $subMenus.not($subMenu).removeClass('in')
                    $side.addClass('sub')
                }
            })
            $side.on('click', 'li', function (e) {
                e.stopPropagation()
            })
            $(document).on('click', '.slide-menu', function () {
                $icon.removeClass('menu-close')
                $wrap.removeClass('slide-menu')
                $side.removeClass('sub')
                $subMenus.removeClass('in')
            })
        }
    }
}()
var marquee = function () {
    var that = {}, $win, $c = [], i = 1, count = 0, height = 0, transitionCSS
    var $w = $(window)
    var tick = function () {
        if ($w.scrollTop() > 310) return;
        var j = i % (count * 2), m = 0
        if (j === 0 || j > count) m = 1

        if (j % count === 0) {
            $c[m].css('transform', 'translateY(' + (-height * (count + 1)) + 'px)')
            $c[m ? 0 : 1].css('transform', 'translateY(' + (-height) + 'px)')
        } else {
            $c[m].css('transform', 'translateY(' + (-height * (j === count ? (count + 1) : (j % (count) + 1))) + 'px)')
        }
        if (j % count === 1) {
            initContent($c[m ? 0 : 1])
        }
        i++
    }
    var initContent = function ($elm) {
        $elm.css('transition', 'none')
            .flushStyle()
            .css({
                position: 'absolute',
                width: '100%',
                top: height,
                transform: 'translateY(0)'
            })
            .flushStyle()
            .css('transition', transitionCSS)
    }
    that.init = function () {
        $win = $('.marquee-win')
        height = $win.height()
        $c[0] = $win.find('.marquee-content')
        $c[1] = $c[0].clone().appendTo($win)
        count = $c[0].children().length
        transitionCSS = $c[0].css('transition')
        initContent($c[0])
        initContent($c[1])
        $c[0].css('transform', 'translateY(' + (-height) + 'px)')
        setInterval(tick, 2500)
    }
    return that
}()

$(function () {
    sideMenu.init()
    //marquee.init()
})