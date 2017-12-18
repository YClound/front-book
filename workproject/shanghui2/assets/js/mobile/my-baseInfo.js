/*
    个人信息修改
*/
$(function(){
    //如果个人信息编辑处于审核状态，保存按钮不可用
    console.log(serverData.user.status);
    if(serverData.user.status == 0){
        $(".footer-tip").show();
        $("[type=submit]").attr("disabled","true");
    }else{
        $(".footer-tip").hide();
        $("[type=submit]").removeAttr('disabled');
    }

    //修改手机号码
    var value = $(".phone").find("input").val();
    $(".phone").on("click", ".modify-phone",function(){
        var button = $(".phone").find("button");
        if(button.hasClass('modify')){
            $(".identityCode").css("display","flex");
            $(".phone").find("input").removeClass('readonly').attr("readonly",false).val("");
            button.removeClass('modify');
            button.text("取消");
             $(".identityCode").find("input[type=text]").attr("required",true);
        }else{
            $(".identityCode").hide();
            $(".phone").find("input").addClass('readonly').attr("readonly",true).val(value).removeClass('error').removeBubble();
            button.addClass('modify');
            button.text("修改");
            $(".identityCode").find("input[type=text]").attr("required",false).removeClass('error').removeBubble();
        }
    })
  
    $("form").on("before",function(){
        $(this).validate('init')
        $("[name=status]").val(0);
    })

    // 表单保存成功
    $("form").on("success",function(){
        dm.alert("信息修改后，需要秘书审核通过，才能生效",function(){
            $(".footer-tip").show();
            $("[type=submit]").attr("disabled","true");
        });
       
    })

})



