$(function(){
    $(".navs-silder").each(function(){
        var navsLink = $(this).children("a");
        console.log(navsLink);
        navsLink.on("click",function(){
            navsLink.removeClass("active");
            $(this).addClass("active");
            var index = $(this).index();
            console.log(index);
            if(index == 1){
                $(".navs-silder-bar").css("left","4em");
                $(".view-signin").show();
                $(".view-singup").hide();
            }else{
                $(".navs-silder-bar").css("left","0");
                $(".view-signin").hide();
                $(".view-singup").show();
            }
        });
    });

    $(".weibo-signup-wrapper").each(function(){
        var otherAccount = $(this).find(".other-account-login");        
        otherAccount.on("click", function(){
            var isShow = otherAccount.hasClass("show");
            console.log(isShow);
           if(isShow){
                $(".other-account").css("opacity","1");
                otherAccount.removeClass("show");
            }else{
                $(".other-account").css("opacity","0");
                otherAccount.addClass("show");
            } 
        });
        
    });

    // 登录成功

    $(".login-form").on("success",function(){
        console.log(111);
        location.href = 'index.html';
    });

    // 注册成功
    $(".register-form").on("success",function(){
        console.log(222);
    })

})