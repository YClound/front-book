$(function(){
  $('.login-form').attr("action",API.root + $('.login-form').attr("action"));
  $('.register-form').attr("action",API.root + $('.register-form').attr("action"))
  // 关闭显示公众号
  $(document).on("click",".close-QRcode",function(){
    $(".QRcode").animate({opacity: "0"},"slow");
  })
  //初始化短信接口
  window.smsHelper.init();
  //登录注册
  $('.banner-content .tab-title').hashTab();

  var relateUrl = location.href.split("?");
  //登录成功
  $('.login-form').on('success',function(e,res){
    if(res.code == 0){
      relateUrl.splice(0,1);
      if(relateUrl.length){
        var url = relateUrl.join("?");
        var location = url.substr(13,url.length);
        document.location.href = location;
      }else{
        document.location.href = "my-paper.html";
      } 
    }
  })
  // 注册成功
  $('.register-form').on('success',function(e,res){
    if(res.code == 0){
      document.location.href="my-guid.html";
    }
  })
})