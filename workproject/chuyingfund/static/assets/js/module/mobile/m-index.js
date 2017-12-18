define(function(require, exports, module) {
    $(function(){
        var swiper = new Swiper('.swiper-container',{
            direction: 'vertical'
        });

        $(document).on('click',function(){
            swiper.slideTo(0);
        });
    })

    
      
});