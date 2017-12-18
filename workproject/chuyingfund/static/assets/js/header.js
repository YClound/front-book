var hoverMenu = function () {
    var that = {}

    var init = function () {
        var $wrap = $(this),
            $list = $wrap.find('.list'),
            height = $list.height() + 16
        $list.css({
            height: 0,
            paddingTop: 0
        }).hide()

        $wrap.hover(function () {
            
            $list.stop(true, false).css({
                display: 'block',
                opacity: 1
            }).animate({
                height: height,
                paddingTop: 8
            }, 200)
        }, function () {
            $list.stop(true, false).animate({
                height: 0,
                opacity: 0,
                paddingTop: 0
            }, 200, function () {
                $list.css({
                    display: 'none'
                })
            })
        })
    }
    that.init = function () {
        $('.s-menu').each(init)
    }
    return that
}();


// 手机端显示菜单按钮
var navToggleMenu = function () {
    
    var that = {}
    var checked = false;
    var init = function () {
        var $wrap = $(this),
            $list = $('#menu'),
            winHeight = window.innerHeight,
            height = $list.height();

        
        height = winHeight-48;
        $list.css({
            height: 0,
            paddingTop: 0,
            visibility: 'visible'
        }).hide()

        $wrap.click(function () {
            checked = !checked;

            console.log('actived',checked)
            if(checked) {
                $list.stop(true, false).css({
                    display: 'block',
                    opacity: 1
                }).animate({
                    height: height
                }, 300);
            }else {
                $list.stop(true, false).animate({
                    height: 0,
                    opacity: 0,
                    paddingTop: 0
                }, 300, function () {
                    $list.css({
                        display: 'none'
                    })
                });
            }
                
        });
    }
    that.init = function () {
        $('#toggleMenuBtn').each(init)
    }
    return that
}();

//isMobile() && navToggleMenu.init();
//!isMobile() && hoverMenu.init();
hoverMenu.init();




