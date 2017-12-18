define(function(require, exports, module) {
    require('header');
    
    require('create.common');
    require('webuploader');
    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');
    require('dm.city.select');
    require('dm.wu');


    
    
    $('.submit-form').on('beforeSend',function(res){
        var value = [];
        $('.check-label input').each(function(){
            if(this.checked){
                value.push(this.value);
            }
        });
        $('#tradeIdAny').val(value.join(','));
        //console.log(11111,value.join(','))
    });
    $('.submit-form').on('success',function(res){
        console.log(res);
        // 跳转
        // location.href = '';
    });
    $('.submit-form').on('fail',function(res){
        console.log(res);
        // 跳转
        // location.href = '';
    });
      
});