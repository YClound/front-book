$(function(){
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
            i++;
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
            height = $win.height();
            $c[0] = $win.find('.marquee-content')
            $c[1] = $c[0].clone().appendTo($win)
            count = $c[0].children().length
            transitionCSS = $c[0].css('transition');
            initContent($c[0])
            initContent($c[1])
            $c[0].css('transform', 'translateY(' + (-height) + 'px)');
            setInterval(tick, 2500)
        }
        return that
    }()
    //判断用户身份信息 relateCommerce为true时关联商会
    var relateCommerce = serverData.user.relateCommerce;
    if(relateCommerce){
        $(".tab-title").show();
        $('.tab-title').find(".tab-title-commerce").show();
        $('.tab-title').find('.tab-title-community').show();
        $(".tab-title").hashTab();
        marquee.init();   
    }else{
        $('.tab-title').find(".tab-title-community").css("width","100%").show();
        $('.tab-content').find(".tab-content-commerce").hide();
        $('.tab-content').find(".tab-content-community").show();
        $(".tab-title").hide();
    }

    // 会话过期提示
    var overdue = serverData.notice.overdue;
    if(overdue ==1){
        dm.confirm("会员已过期",function(){
            
        })
    }
})
