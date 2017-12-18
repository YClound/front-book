$(function(){
    $('.s-header').headroom();
    // 同乡注册成功表单验证成功后
    $(".village-register .register-form").on("success",function(e){
        location.href = "village-success.html";
    });
})