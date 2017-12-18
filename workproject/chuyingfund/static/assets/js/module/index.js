define(function(require, exports, module) {
    require('owl.carousel');
    require('header');

   
    
    
    /*$('.s-banner .owl-carousel').owlCarousel({
        items: 1,
        //nav: true,
        loop: true,
        //navText: ['&lsaquo;', '&rsaquo;'],
        autoplay: true,
        autoplayHoverPause: true,
        dotsContainer: '.dots-wrap',
        animateOut: 'fadeOut'

    });*/

   
    var starCarousel = function () {
        var that = {}
        that.init = function () {
            that.$carousel = $('.s-star .owl-carousel')
            that.$items = that.$carousel.find('.item')
            that.$quotes = $('.s-star .quote-list').children()
            var switchStar = function (idx) {
                var $item;
                //console.log(that.$items.eq(idx))
                $item = that.$carousel.find('.item').removeClass('active').eq(idx).addClass('active')
                //that.$quotes.hide().eq($item.data('index')).stop(true, false).fadeIn()
            }
            that.$carousel.on('changed.owl.carousel', function (e) {
                console.log(e.item.index)
                switchStar(e.item.index + 1)
            }).on('initialized.owl.carousel', function (e) {
                //console.log(22)
                return switchStar(1)
            }).owlCarousel({
                items: 3,
                //loop: true,
                nav: true,
                navText: ['&lsaquo;', '&rsaquo;'],
            })
            that.$carousel.find('.owl-item').on('click', function () {
                var $owlItem = $(this)
                var index = $owlItem.parent().children('.active').index($owlItem)
                if (index < 2) {
                    that.$carousel.trigger('prev.owl.carousel')
                    if (index < 1) that.$carousel.trigger('prev.owl.carousel')
                }
                if (index > 2) {
                    that.$carousel.trigger('next.owl.carousel')
                    if (index > 3) that.$carousel.trigger('next.owl.carousel')
                }
            })
        }

        return that
    }();

    starCarousel.init();

    
    if(isMobile()) {
        //$('#starCarousel').show();

    }else {
        // 唯独首页
        //$('body').css('paddingTop', '62px');
        //$('.s-header').addClass('s-header-fixed');
        //starCarousel.init();
        /*var winH = window.innerHeight;
        var h = winH-62;

        $('.s-banner').css('height', h+'px');
        starCarousel.init();*/
    }
        
    
});
