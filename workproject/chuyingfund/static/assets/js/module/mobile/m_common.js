define(function(require, exports, module) {
    
    

    $(function(){
        


        // swipe
        dm.swipe = []
        $('.swipe').each(function () {
            var $dots,
                defaultOption = {
                    callback: function (i) {
                        $dots.removeClass('active')
                        $dots.eq(i).addClass('active')
                    }
                },
                o = $.extend({}, defaultOption, $(this).data()),
                sw = Swipe(this, o),
                count = sw.getNumSlides()
            var dotsWrap = $(this).find('.dots-wrap').html(new Array(count + 1).join('<u></u>'))
            $dots = dotsWrap.children()
            $dots.eq(0).addClass('active')
        })

        


    });
        



    
      
});