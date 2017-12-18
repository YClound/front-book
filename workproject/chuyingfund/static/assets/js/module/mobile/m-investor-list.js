define(function(require, exports, module) {
    require('template');
    
    
    $(function () {
        $(".infinite-loading").infiniteLoading();
        $(document).on('click','.investor-screen-btn',function(e){
            e.preventDefault();
            $('.m-investor-filter').addClass('active');
        });
        $(document).on('click','.investor-filter-cancel-btn',function(e){
            e.preventDefault();
            $('.m-investor-filter').removeClass('active');
        });

        
        


        
        $('.m-investor-filter .filter-list').each(function(i,index){
            //console.log(i)
            var prefix = 'r_';
            prefix = prefix + i + '_';
            $(this).find('.check-field').each(function(j){
                //console.log(j)
                var id = prefix + j;
                $(this).find('input').attr('id',id);
                $(this).find('label').attr('for',id);
            });


        });
        
    });



    
      
});