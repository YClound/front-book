$(function(){
  $(".ajax-form").attr("action",API.root + $(".ajax-form").attr("action"))
  var subjectId = serverData.userData.subjectId;
  // 获取学科
  dm.get(API.root+'/api/subject/list').done(function(res){
    var list = res.data.list;
    var html = "";
    for(var i=0; i<list.length; i++){
      html += template("subjectList",list[i]);
    };
    $('.subject-list').html(html);
    $('[data-subjectid='+subjectId+']').find("input").attr("checked","true");
  });

  $('.manual-form').on("success",function(){
    var subjectId;
    var templateId;
    $.each($(".format-item"),function(i,item){
      var checked = $(item).find('[name="templateId"]');
      if(checked.is(":checked")){
        templateId = checked.val();
      }
    })
    $.each($(".subject-item"),function(i,item){
      var checked = $(item).find('[name="subjectId"]');
      if(checked.is(":checked")){
        subjectId = checked.val();
      }
    })
    location.href = "question-bank.html?subjectId="+subjectId+"&templateId="+templateId;
  })
})