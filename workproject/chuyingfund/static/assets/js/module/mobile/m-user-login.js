define(function(require, exports, module){
    // 假如返回跳转地址则跳转
    $('form').on('success',function(e,res){
            location.href = 'index.html';
    });
    
});