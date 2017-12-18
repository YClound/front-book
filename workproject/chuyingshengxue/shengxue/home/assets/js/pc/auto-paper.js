$(function(){
  $('.auto-form').attr("action",API.root + $(".auto-form").attr("action"));
  var location = API.root;
  var syllabusList;
  var getSumScore = function(){
    var score = 0;
    $.each($('.questionType-item'),function(){
      var singleSumScore = parseInt($(this).find(".score-sum").text())
      score += singleSumScore;
    })
    $('.tatol-score').find('var').text(score);
  }
  var getsubjectData = function(subjectId){
    // 获取题型
    dm.get(location+'/api/questionType/list',{typeSubjectId:subjectId}).done(function(res){
      var data = res.data || [];
      var html = "";
      $.each(data,function(index,item){
        if(item.typeModule === "SELECT"){
          item.recordCount = 10;
          item.recordScore = 2;
          item.scoreSum = item.recordCount * item.recordScore;
        }else if(item.typeModule === "FILLBLANK"){
          item.recordCount = 5;
          item.recordScore = 3;
          item.scoreSum = item.recordCount * item.recordScore;
        }else if(item.typeModule === "QANDA"){
          item.recordCount = 5;
          item.recordScore = 5;
          item.scoreSum = item.recordCount * item.recordScore;
        }else{
          item.recordCount = 3;
          item.recordScore = 10;
          item.scoreSum = item.recordCount * item.recordScore;
        }
      })
      for(var i=0; i<data.length; i++){
        html += template("questionTypeList",data[i]);
      };
      $('.questionType-list').html(html);
      var height = $('.questionType-list').height() + 100;
      $('.questionType-line').css("height",height+"px");
      getSumScore();
    });
    //获取考点
    dm.get(location+'/api/syllabus/listAll',{syllabusSubjectId:subjectId}).done(function(res){
      var list = res.data || [],html="",childrenHtml = "";
      syllabusList = list;
      $('.syllabus-number').children('var').text("0");
      $.each(list,function(index,item){
        if(item.children.length){
          html += '<li class="parent-item" data-id="'+item.syllabusId+'">'+item.syllabusName+'</li>';
        }else{
          html += '<li class="children-item"><label><input type="checkbox" name="syllabusId" value="'+item.syllabusId+'" class="syllabus"><div class="checkbox"></div>'+item.syllabusName+'</label></li>';
        }
        
        childrenHtml += template('childrenSyllabus',item);
      })
      $('.syllabus-parent').html(html);
      $('.syllabus-children').html(childrenHtml)
    })
  }

  var getSyllabusId = function(){
    var number = 0;
    var syllabusId = [];
    var syllabusList = $('.syllabus-content').find('.syllabus');
    // console.log(syllabusList);
    $.each(syllabusList,function(index,item){
      // console.log($(this).is(':checked'));
      if($(this).is(':checked')){
        syllabusId.push($(this).val());
        number++;
      }
    });
    $('.syllabus-number').children('var').text(number);
    console.log(syllabusId)
    $('.paper-syllabusId').val(JSON.stringify(syllabusId));
    // 是否选择考点
    if(number > 0){
      $('.paper-syllabus').find('.paper-type').addClass('active');
      $('.outline-processor').addClass('active');
    }else{
      $('.paper-syllabus').find('.paper-type').removeClass('active');
      $('.outline-processor').removeClass('active');
    }
  }

  var bindEvent = function(){
    // 点击打开或关闭考点列表
    $('.syllabus-number').on('click',function(){
      var isShow = $(".syllabus-content").hasClass('show');
      if(isShow){
        $(".syllabus-content").hide();
        $(".syllabus-content").removeClass("show");
      }else{
        $(".syllabus-content").show();
        $(".syllabus-content").addClass("show")
      }
    })
    // 获取子考点
    $('.paper-syllabus').on('click','.parent-item',function(){
      var index = $(this).index();
      $(this).siblings().removeClass('active');
      $(this).addClass("active");
      $('.paper-syllabus').find('.syllabus-children').css('display','inline-block');
      $('.paper-syllabus').find('.syllabus-children').children().hide()
      $('.paper-syllabus').find('.syllabus-children').children().eq(index).show();
    })

    // 获取考点ID
    $('.paper-syllabus').on('change','.syllabus',function(){
      getSyllabusId();
    })
    //改变学科
    $('.paper-subject').on('change','.subject-value',function(){
      var subjectId = $(this).val();
      $('.paper-syllabus').find('.paper-type').removeClass('active');
      $('.outline-processor').removeClass('active');
      getsubjectData(subjectId);
    })

    //填写试卷题目
    $('.paper-title').on('input','.title',function(){
      var isEmpty = $(this).val();
      // console.log(isEmpty)
      if(isEmpty){
        $('.paper-title').children('.paper-type').addClass('active');
        $('.title-processor').addClass('active');
      }else{
        $('.paper-title').children('.paper-type').removeClass('active');
        $('.title-processor').removeClass('active');
      }
    })

    // 总分
    $('.paper-questionType').on('input','input',function(){
      var item = $(this).closest('.questionType-item');
      var number = item.find('.count').val() || 0;
      var singleScore = item.find('.single-score').val() || 0;
      // console.log(item,number,singleScore)
      item.find('.score-sum').text(number * singleScore);
      getSumScore();
    })
  }

  var init = function(){ 
    var subjectId = serverData.userData.subjectId;
    // 获取学科
    dm.get(location+'/api/subject/list').done(function(res){
      var list = res.data.list;
      var html = "";
      for(var i=0; i<list.length; i++){
        html += template("subjectList",list[i]);
      };
      $('.subject-list').html(html);
      $('[data-subjectid='+subjectId+']').find("input").attr("checked","true");
    });
    // 获取难度
    dm.get(location+'/api/questionProperty/difficults').done(function(res){
      var data = res.data || [];
      var html = "";
      $.each(data,function(i,item){
        html += '<option value="'+item.value+'">'+item.label+'</option>';
      });
      $('[name="difficult"]').html(html);
      $('[name="difficult"]').val('0.63');
    })
    //获取数据
    getsubjectData(subjectId);
    // 绑定事件
    bindEvent();

    // 提交表单
    $('.make-paper').on("click",function(){
      var syllabusId = $('.syllabus-number').find('var').text();
      var questionType = [];
      var title = $('.paper-title').find(".title").val()
      console.log(syllabusId)
      if(syllabusId == 0){
        dm.notice("您还未选择考点");
        return;
      }else if(!title){
        dm.notice("请输入试卷的名称");
        return;
      }else{
        $.each($('.questionType-item'),function(){
          var typeConfig;
          var typeCount = $(this).find('.count').val() || 0;
          if(typeCount > 0){
            var typeId = $(this).data('id');
            var typeScore = $(this).find('.score-sum').text() || 0;
            typeConfig = {
              typeId : typeId,
              count : parseInt(typeCount),
              score : parseInt(typeScore)
            }
            questionType.push(typeConfig)
          }
        })
      }
      $('.question-type').val(JSON.stringify(questionType));
      // console.log($('.question-type').val())
      $(".auto-form").submit();
    })
  }
  init();
})