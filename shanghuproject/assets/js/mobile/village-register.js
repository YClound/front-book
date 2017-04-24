$(function(){
    $("input[type='checkbox']").on("click",function(){
        var check = $("input[type='checkbox']").is(':checked');
        var radio = $("input[type=radio]");
        console.log(check);
        if(check){
            $('.group').show();
        }else{
            radio.removeAttr('checked');
            $('.group').hide();
        }
    })
    
    // 同乡注册成功表单验证成功后
    $(".village-register .ajax-form").on("success",function(e){
        e.preventDefault;
        location.href = "village-success.html";
    });
})