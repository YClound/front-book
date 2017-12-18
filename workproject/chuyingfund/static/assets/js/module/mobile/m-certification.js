define(function(require, exports, module){
    // 假如返回跳转地址则跳转
    $('form').on('success',function(e,res){
        if(res&&res.redirect){
            location.href = res.redirect;
        }
    });
});