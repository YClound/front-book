define(function(require, exports, module){


    $('form').on('beforeSend',function(e,res){
        $(this).data('error','');


        if(1){

        }
    });
    // 假如返回跳转地址则跳转
    $('form').on('success',function(e,res){
        if(res&&res.redirect){
            location.href = res.redirect;
        }
    });
});