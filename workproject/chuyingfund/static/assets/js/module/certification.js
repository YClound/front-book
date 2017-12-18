define(function(require, exports, module) {
    require('header');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');

    
    /*$('form').on('beforeSend',function(e){
        // 如果有错误想阻止提交 加这句话
        $(this).data('error','错误信息的内容');
    });*/
    // 假如返回跳转地址则跳转
    $('form').on('success',function(e,res){
        if(res&&res.redirect){
            location.href = res.redirect;
        }
    });
    
});