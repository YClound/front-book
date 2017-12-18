/* 非登录状态的弹出层功能 */
//2015/12/18修改的js
function checkLogin(callback,act,obj,isAnchor) {
  $.ajax({
  type: "POST",
  async : false,
  url: groupUrl+"/Public/ajaxCheckUser",
  data:"",
  success: function(data){
    if (data=='1'){
       callback(act,obj);
    }else{
      if (isAnchor)
      {
        window.location=obj.attr('href');
      }
      else
      {
        popTips1();//显示登录弹窗
      }
    }
  }
  });
}


/* 需要有手机号或者第三方登录的弹出层功能 */
function checkHavePhoneOrThirdLogin(callback,act,obj,isAnchor) {
  $.ajax({
  type: "POST",
  async : false,
  url: groupUrl+"/Public/ajaxCheckHavePhoneOrThirdLogin",
  data:"",
  success: function(data){
    if (data.state=='1'){
       callback(act,obj);
    }else{
      if (isAnchor)
      {
        window.location=obj.attr('href');
      }
      else
      {
		$('#showed_id').hide();  // 临时解决方案，绑定手机框显示时，关闭之前的层
        //$('#lean_overlay').css({zIndex:'1002'});
        popTipBindPhone();//显示验证手机弹窗
		//alert($('#xy_yan').html());
      }
    }
  }
  });
}

/**题库新后台审核试题，录入试题和试题库列表页面需要的代码**/
//全选
$(".quanxuan").click(function(){
    $(".checkA").toggleClass("this");
})
$(".checkAll").click(function(){
    $(".checkA").toggleClass("this");
})
//删除选中
$(".shanchu").click(function(){
    /*获得试题评论ID，然后放入隐藏域中*/
    var cid_array = new Array();
    $(".checkA").each(function(){
        var is_selected = $(this).attr("class");
        if(is_selected=='checkA this'){
            var cid = $(this).html();
            cid_array.push(cid);
        }
    });
    //将数组换成字符串,并放入隐藏域中
    var cid_string = cid_array.join(',');
    $.ajax({
        type: "POST",
        url: groupUrl+"/Exam/delExam",
        data: {cid_string:cid_string},
        dataType: "text",
        success: function(data){
           if(data==1){
               location.reload();
           }else{
               alert("对不起，删除失败！");
           }
        }
    });
})

/******-------新后台搜索部分-----------*******/
/*科目关联题型*/
function ajax_kemu(sub_id)
{
    $.ajax({
        type: "post",
        url: tikuUrl+"/Public/examAddTx",
        data: {sub_id:sub_id},
        dataType: "json",
        success: function (data) {
			data = data['tixing'];
            var tixing_html = "<li value='0'>请选择</li>";
            for(var i =0;i<data.length;i++){
                tixing_html+="<li value='"+data[i].id+"'>"+data[i].name+"</li>";
            };
            $("#tixing").html(tixing_html);//清空父节点下的子节点，并且插入新的节点
        }
    });
}
//点击科目题型跟着换
$("#kemu li").live("click",function(){
	var id = $(this).attr("value");
    ajax_kemu(id);
});
//点击下拉单将ID值赋到隐藏域中
$(".select ul[class$=Txt] li").live("click",function(){
	var id = $(this).attr("value");
    $(this).parent().next("input[type='hidden']").val(id);
});


//权限弹框提示
function access_ajax_call(act,obj,js_act_id,cur_sub,exam_id){
  $.ajax({
    type:"get",
    async: false,
    url: "../api/window_ajax.json",
    data:{act:act,js_act_id:js_act_id,cur_sub:cur_sub,exam_id:exam_id},
    dataType:"json",
    success:function(data){
      if(data.login!=""){
        if(window.location.href=="http://"+window.location.host+"/"){
          $('#tipvip').skygqbox();
          $(".wrap_title span").html("提示");
        }else{
          popTips1();
        }
      }else{
        if(data.js_msg != ""){
          data.js_msg;
        }
        if(data.tishiyu_msg != ""){
            $("#tishiyu").html(data.tishiyu_msg);
				//答题卡
				if(data.act1 === 'down_datika'){
					$("form").submit(false);
				};
			
			  if(data.button_hide != ""){
            $("#becomeVip").hide();
        }else{
          if(data.button_msg != ""){
              $("#becomeVip").html(data.button_msg);
              if(data.button_msg=="确定"){
                  $("#becomeVip").live("click",function(){
                    var curUrl = top.location.href;
                    window.location=curUrl;
                  });
              }
              else if(data.button_msg=="成为vip"||data.button_msg=="升级vip"){
                $("#becomeVip").live("click",function(){
                  window.open(data.url);
                });
              }
              if(data.button_css != ""){
                  $("#becomeVip").css(data.button_css);
              }
          }
        }
        $('#tipContent1').skygqbox();
        $(".wrap_title span").html("提示");
        if(data.box_disappear != ""){
          $("#wrapClose").hide();
          $("#wrapOut").delay(3000).fadeOut("slow",function(){
              $.skygqbox.hide();
          });
        }
      }else{
        switch(act){
          case "create_test":switch(data.js_act_id){
            case 1:
              $('#znExamZ_form').submit();
              break;
            case 2:
              $.ajax({
                type: "POST",
                url: "/Tiku/ExamDiy/sg_wait",
                dataType: "html",
                success: function(html){
                  /*跳转到试卷页面*/
      						$.ajax({
      							type:"post",
      							url:"/Tiku/ExamDiy/wp_qshareintegral",
      							success:function(){
      								window.location.href="/shijuan/";
      							}
      						});  
                }
              });
              break;
              default:break;
            }
            break;
          case "collect_exam":collect_exam();
            break;

          case "save":whether_change_title2();
            break;

          case "down_shijuan":
            if(data.js_act_id==3){
              var exam_title = obj.parent().parent().parent().find("td:first").html();
                var exam_id = obj.attr("examid");//试卷id
                $("input[name='examId']").val(exam_id);
                $(".save_title_doc").html(exam_title+"_yitiku.doc");
                $(".save_title_docx").html(exam_title+"_yitiku.docx");
                $(".save_title_pdf").html(exam_title+"_yitiku.pdf");
                popTips10();
              }else{
                whether_change_title();
              }
              break;
          case "down_datika":popTips20();
            break;

          case "down_shiti": download_exam();
            break;

          case "buy_continue":window.location = data.url;
            break;

          case "start_online_test":
            $.ajax({
              type: "POST",
              url: "/Tiku/ExamDiy/check_save_000",
              dataType: "html",
              success: function(data){
      					if(data){
      						var integrationid = 10;//测评增加积分
      						$.ajax({
      							type: "POST",
      							url: "/Tiku/ExamDiy/wp_qshareintegral",
      							data : {integrationid:integrationid},
      							success: function(){
      								if(data=='1'){
      									savePaper();//保存
      								}else{
      									$('#save_tishi').html(data);
      									showTipsWindown("提示", 'tishiContent', 450, 120);
      								}
      							}
      						});
      					}
      				}
            });
            break;
          case "accept_online_test":window.location = data.url;
            break;
          case "online_lianxi":$("#znExamZ_form").submit();
            break;
          default:break;
          }
        }
      }
    }
  });
}
