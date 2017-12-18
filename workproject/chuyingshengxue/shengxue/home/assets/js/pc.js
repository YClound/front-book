$(function(){
  // nav的hover效果
  $('.nav-title').hover(function(){
    $(this).children('a').addClass('active');
    $(this).children('.nav-content').show();
  },function(){
    $(this).children('a').removeClass('active');
    $(this).children('.nav-content').hide();
  })
  // 获取学科
  dm.get(API.root + "/api/subject/list").done(function(res){
    var list = res.data.list;
    var html = "";
    $.each(list,function(i,item){
      html += '<a href="question-bank.html?subjectId='+item.subjectId+'">'+item.subjectName+'</a>'
    })
    $(".nav-subject-list").children("div").html(html);
  });

  $('.back-top').backtop();
  // 判断是否登录
  if(serverData.userData.id){
    $('.header-user').attr("href","my-paper.html")
    $(".header-user").children(".user-info").html("欢迎您，"+serverData.userData.name+'<a href="logout" class="pl-6">退出</a>')
    $('.banner-content').hide();
  }else{
    $('.header-user').attr("href","javascript:;")
    $(".header-user").children(".user-info").html('<a href="index.html" href="javascript:;">登录/注册</a>')
    $('.banner-content').show();
  }
  
})