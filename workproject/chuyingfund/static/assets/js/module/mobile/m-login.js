define(function(require, exports, module) {
    require('m_common');
    require('dm.validate');


    $(function(){

        $("input[type=password]").each(function() {
            var t = $(this),
            e = t.siblings(".show-password");
            e.length || (e = $('<div class="show-password poa r-0 t-0 p-6 c-c3 fz-medium status-off"><i class="if icon-eye-fill on"></i> <i class="if icon-eye-close off"></i></div>').insertAfter(t)),
            e.on("click",
            function() {
                "text" === t.attr("type") ? (t.attr("type", "password"), e.removeClass("status-on").addClass("status-off")) : (t.attr("type", "text"), e.removeClass("status-off").addClass("status-on"))
            })
        });
    
        //异步登录
        $("#login-form").on("success",function(e,res){
            var redirect = $("#referer").val();
            if(redirect) {
                location.href = $("#referer").val();
            }
            
        })
    });


    
      
});