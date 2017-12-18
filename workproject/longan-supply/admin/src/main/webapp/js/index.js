$(function () {
    //日历
    $("#datepicker").datepicker();
    //左边菜单
    $('.one').click(function () {
        var $this = $(this)
        $('.one').removeClass('one-hover');
        $(this).addClass('one-hover');
        $(this).parent().find('.kid').toggle();
        var $typeDetail = $this.closest(".type-detail");
        var $one = $typeDetail.find(".one")
        if($one.hasClass("show-off")){
            $one.removeClass("show-off").addClass("show-on");
        }else{
            $one.removeClass("show-on").addClass("show-off");
        } 
       
    });
    $(".type-detail .kid li").click(function(){
        $(".type-detail .kid li").removeClass("active");
        $(this).addClass("active");
    })
    //隐藏菜单

    var $leftWidth = parseInt($(".left_c").css("width"));
    console.log("$leftWidth:"+$leftWidth);
    var $cWidth = parseInt($leftWidth) + 20;
    console.log("$cWidth:"+$cWidth);



    if($leftWidth <= 235){
        $(".right_c").css("left","235px");
    }

    
    $(".Conframe").css("left",$cWidth);
    var l = $('.left_c');
    var r = $('.right_c');
    var c = $('.Conframe');
    $('.nav-tip').click(function () {
        if (l.css('left') == '0px') {
            l.animate({
                left: -$leftWidth
            }, 500);
            r.animate({
                left: 0
            }, 500);
            c.animate({
                left: 20
            }, 500);
            $(this).animate({
                left: 4,
                // "background-position-x": "-12"
                "background" : "url(../img/6.png)"
            }, 500);
            $(this).css('background',"url(img/6.png)  no-repeat");
        } else {
            l.animate({
                left: 0
            }, 500);
            r.animate({
                left: $leftWidth
            }, 500);
            c.animate({
                left: $cWidth
            }, 500);
            $(this).animate({
                // "background-position-x": "0",
                left : 4,
                "background" : "url(../img/5.png)"
            }, 500);
            $(this).css('background',"url(img/5.png)  no-repeat");
        };
    })
    //横向菜单
    $('.top-menu-nav li').click(function () {
        $('.kidc').hide();
        $(this).find('.kidc').show();
        
    })
    $('.kidc').bind('mouseleave', function () {
        $('.kidc').hide();
    })


    // 页面tab的设置
    $(function(){
       $('.type-service-tab').click(function(){
            $('.type-service-tab').addClass("active");
            $('.statistical-query-tab').removeClass("active");
            $('.inventory-tab').removeClass("active");
            $('.system-management-tab').removeClass("active");
            $(".type-service").show();
            $(".inventory").hide();
            $(".statistical-query").hide();
            $(".system-management").hide();
       })
       $(".inventory-tab").click(function(){
            $('.inventory-tab').addClass("active");
            $('.type-service-tab').removeClass("active");
            $('.statistical-query-tab').removeClass("active");
            $('.system-management-tab').removeClass("active");
            $(".inventory").show();
            $(".type-service").hide();
            $(".statistical-query").hide();
            $(".system-management").hide();

       })
       $(".statistical-query-tab").click(function(){
            $('.statistical-query-tab').addClass("active");
            $('.type-service-tab').removeClass("active");
            $('.inventory-tab').removeClass("active");
            $('.system-management-tab').removeClass("active");

            $(".statistical-query").show();
            $(".type-service").hide();
            $(".inventory").hide();
            $(".system-management").hide();
       })
       $(".system-management-tab").click(function(){
            $('.system-management-tab').addClass("active");
            $('.type-service-tab').removeClass("active");
            $('.inventory-tab').removeClass("active");
            $('.statistical-query-tab').removeClass("active");

            $(".system-management").show();
            $(".type-service").hide();
            $(".statistical-query").hide();
            $(".inventory").hide();
       })
    }) 

})