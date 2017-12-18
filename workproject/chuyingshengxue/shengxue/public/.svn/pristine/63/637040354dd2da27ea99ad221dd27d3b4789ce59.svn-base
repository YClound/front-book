$(function(){
  // 关闭显示公众号
  $(document).on("click",".close-QRcode",function(){
    $(".QRcode").animate({
      opacity: "0"},
      "slow");
  })
  // 判断是否登录
  if(serverData.user.id){
    $('.banner-content').hide();
  }else{
    $('.banner-content').show();
  }
  //初始化短信接口
  window.smsHelper.init();
  //登录注册
  $('.banner-content .tab-title').hashTab();

  //登录成功
  $('.login-form').on('success',function(e,res){
    if(res.code == 0){
      location.href="my-paper.html";
    }
  })
  // 注册成功
  $('.register-form').on('success',function(e,res){
    if(res.code == 0){
      document.location.href="my-guid.html";
    }
  })
})