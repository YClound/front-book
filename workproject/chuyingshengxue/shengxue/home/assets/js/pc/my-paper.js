$(function(){
  // tab转换
  $('.tab-title').hashTab();
  template.config("escape", false);
  var option = ["A","B","C","D","E","F","G","H","I","J","K"];
  var subjectId = serverData.userData.subjectId;
  // 我的题目查询条件
  var questionQueryData = {
    page : 1,
    pageSize: 10
  }
  // 我的试卷查询条件
  var paperQueryData = {
    page : 1,
    pageSize : 10
  }
  // 渲染页面
  var renderPage = function(){
    // 获取学科
    dm.get(API.root + '/api/subject/collectList').done(function(res){
      var list = res.data,html = "";
      $.each(list,function(i,subject){
        if(subject.subjectId == subjectId){
          html += '<a href="javascript:;" class="filter-type active" data-id="'+subject.subjectId+'">'+subject.subjectName+'(<var>'+subject.collectCount+'</var>)</a>'
        }else{
          html += '<a href="javascript:;" class="filter-type" data-id="'+subject.subjectId+'">'+subject.subjectName+'(<var>'+subject.collectCount+'</var>)</a>'
        }
      });
      questionQueryData.subjectId = subjectId;
      getQuestionList(questionQueryData)
      getQuestionType(subjectId);
      $(".question-subject").html(html);
    });

    // 获取来源
    dm.get(API.root + '/api/questionProperty/collectFroms').done(function(res){
      var data = res.data;
      var html = "",sumCount = 0;
      $.each(data,function(i,item){
        sumCount += parseInt(item.collectCount);
        html += '<a href="javascript:;" class="filter-type" data-name="'+item.name+'">'+item.name+'(<var>'+item.collectCount+'</var>)</a>'
      });
      var allHtml = '<a href="javascript:;" class="filter-type active" data-name="">全部(<var>'+sumCount+'</var>)</a>'
      $('.question-from').html(allHtml + html);
    });

    // 获取我的试卷
    getPaperList(paperQueryData)

  }

  // 获取题型
  var getQuestionType = function(subjectId){
    dm.get(API.root + '/api/questionType/collectList',{typeSubjectId:subjectId}).done(function(res){
      var data = res.data;
      var html = "",sumCount = 0;
      $.each(data,function(i,item){
        sumCount += parseInt(item.collectCount);
        html += '<a href="javascript:;" class="filter-type" data-id="'+item.typeId+'">'+item.typeName+'(<var>'+item.collectCount+'</var>)</a>'
      });
      var allHtml = '<a href="javascript:;" class="filter-type active" data-id="">全部(<var>'+sumCount+'</var>)</a>'
      $('.question-type').html(allHtml + html);
    });
  }

  // 获取题目列表
  var getQuestionList = function(questionQueryData){
    dm.loading("正在加载中........")
    dm.get(API.root + '/api/collect/question/list',questionQueryData).done(function(res){
      var data = res.data;
      var list = data.list,html = "";
      if(list.length){
        $(".pagination").show();
        $.each(list,function(index,item){
          if(item.typeCode == "SELECT"){
            item.optionIcon = option;
            html += template("questionSelectList",item);
          }else if (item.typeCode == "COMPLEX"){
            item.optionIcon = option;
            html += template("questionComplexList",item);
          }
          else{
            html += template("questionFillList",item);
          }
        })
        pagination(data.pager)
        $(".question-list-content").html(html);
        isNewline()
      }else{
        $(".question-list-content").html('<div class="none-question"><img src="./assets/images/pc/none-question.png"><div>您还未收藏该类型的题目哦~</div></div>');
        $(".pagination").hide();
      }
    }).always(function(){dm.loading('hide');})
  }

  var isNewline = function(){
    var optionItem = $('.question-option').find('.option-item');
    $.each(optionItem,function(index,item){
      var height = $(item).find('.text-in').height();
      var imgHeight = $(item).find('.text-in img').height();
      // console.log(height,imgHeight);
      if(height <= imgHeight){
        $(item).find(".option-icon").css({"top" : "50%","marginTop" : "-13.5px"})
      }else if(imgHeight){
        $(item).find(".option-icon").css({"top" : ((imgHeight-25)/2) + "px","marginTop" : "0px"})
      }
    })
  }
  // 分页
  var pagination = function(data){
    var pager = data;
    var Paging = $(".pagination .pagination-list").paging(pager.total, {
      format: '[< ncnnn >]',
      onSelect: function(page) {
        // add code which gets executed when user selects a page
        if(questionQueryData.page != page){
          questionQueryData.page = page;
          dm.loading("正在加载中........")
          dm.get(API.root + "/api/collect/question/list",questionQueryData).done(function(res){
            var data = res.data;
            var list = data.list,html = "";
            $.each(list,function(index,item){
              if(item.typeCode == "SELECT"){
                item.optionIcon = option;
                html += template("questionSelectList",item);
              }else if (item.typeCode == "COMPLEX"){
                item.optionIcon = option;
                html += template("questionComplexList",item);
              }
              else{
                html += template("questionFillList",item);
              }
            })
            $(".question-list-content").html(html);
          }).always(function(){dm.loading('hide');});
        }
      },
      onFormat: function(type) {
        switch (type) {
          case 'block': // n and c
            if (this["page"] !== this["value"]){
              return '<a href="javascript:;" class="pagination-item">'+this.value+'</a>';
            }
            return '<a href="javascript:;" class="pagination-item active">'+this.value+'</a>';
          case 'next': // >
            if (!this['active']){
              return '<a href="javascript:;" disabled class="btn pagination-item item-circle"><span class="fa fa-angle-right"></span></a>';
            }
              return '<a href="javascript:;" class="pagination-item item-circle active"><span class="fa fa-angle-right"></span></a>';
          case 'prev': // <
            if (!this['active']){
              return '<a href="javascript:;" disabled class="btn pagination-item item-circle"><span class="fa fa-angle-left"></span></a>';
            }
            return '<a href="javascript:;" class="pagination-item item-circle active"><span class="fa fa-angle-left"></span></a>';
          case 'first': // [
            if (!this['active']){
              return '<a href="javascript:;" class="pagination-item item-circle active">首页</a>';
            }
            return '<a href="javascript:;" class="pagination-item item-circle">首页</a>';
          case 'last': // ]
            if (!this['active']){
              return '<a href="javascript:;" class="pagination-item item-circle active">末页</a>';
            }
            return '<a href="javascript:;" class="pagination-item item-circle">末页</a>';
        }
      }
    });
    // 跳转到指定页面
    $('.pagination').on("click",".goToPage",function(){
      var pageNumber = $(this).siblings(".page-number").val();
      var page = $(".pagination").find(".pagination-list").find(".pagination-item:last-of-type").data("page");
      if(pageNumber > 0 && pageNumber <= page){
        Paging.setPage(pageNumber);
      }else if(pageNumber > page){
        dm.alert("页面只有"+page+"页");
      }else{
        dm.alert("请输入要跳转的页数");
      }
    });
  }

  // 获取试卷列表
  var getPaperList = function(paperQueryData){
    dm.loading("正在加载中........")
    dm.get(API.root + '/api/collect/paper/list',paperQueryData).done(function(res){
      var paper = res.data.pager;
      var list = res.data.list,html = "";
      if(list.length){
        $('.paper-pagination').show();
        $.each(list,function(i,item){
          html += template('paperList',item);
        })
        $('.paper-list-content').html(html);
        paperPagination(paper);
      }else{
        $('.paper-pagination').hide();
        $(".paper-list-content").html('<div class="none-question"><img src="./assets/images/pc/none.png"><div>您还未保存任何试卷哦~</div></div>');
      }
    }).always(function(){dm.loading('hide');})
  }

  //试卷的分页
  var paperPagination = function(data){
    var pager = data;
    var Paging = $(".paper-pagination .pagination-list").paging(pager.total, {
      format: '[< ncnnn >]',
      onSelect: function(page) {
        // add code which gets executed when user selects a page
        if(paperQueryData.page != page){
          paperQueryData.page = page;
          dm.loading("正在加载中........")
          dm.get(API.root + "/api/collect/paper/list",paperQueryData).done(function(res){
            var list = res.data.list,html = "";
            $.each(list,function(i,item){
              html += template('paperList',item);
            })
            $('.paper-list-content').html(html);
          }).always(function(){dm.loading('hide');});
        }
      },
      onFormat: function(type) {
        switch (type) {
          case 'block': // n and c
            if (this["page"] !== this["value"]){
              return '<a href="javascript:;" class="pagination-item">'+this.value+'</a>';
            }
            return '<a href="javascript:;" class="pagination-item active">'+this.value+'</a>';
          case 'next': // >
            if (!this['active']){
              return '<a href="javascript:;" disabled class="btn pagination-item item-circle"><span class="fa fa-angle-right"></span></a>';
            }
              return '<a href="javascript:;" class="pagination-item item-circle active"><span class="fa fa-angle-right"></span></a>';
          case 'prev': // <
            if (!this['active']){
              return '<a href="javascript:;" disabled class="btn pagination-item item-circle"><span class="fa fa-angle-left"></span></a>';
            }
            return '<a href="javascript:;" class="pagination-item item-circle active"><span class="fa fa-angle-left"></span></a>';
          case 'first': // [
            if (!this['active']){
              return '<a href="javascript:;" class="pagination-item item-circle active">首页</a>';
            }
            return '<a href="javascript:;" class="pagination-item item-circle">首页</a>';
          case 'last': // ]
            if (!this['active']){
              return '<a href="javascript:;" class="pagination-item item-circle active">末页</a>';
            }
            return '<a href="javascript:;" class="pagination-item item-circle">末页</a>';
        }
      }
    });
    // 跳转到指定页面
    $('.paper-pagination').on("click",".goToPage",function(){
      var pageNumber = $(this).siblings(".page-number").val();
      var page = $(".paper-pagination").find(".pagination-list").find(".pagination-item:last-of-type").data("page");
      console.log(page)
      if(pageNumber > 0 && pageNumber <= page){
        Paging.setPage(pageNumber);
      }else if(pageNumber > page){
        dm.alert("页面只有"+page+"页");
      }else{
        dm.alert("请输入要跳转的页数");
      }
    });
  }

  //绑定事件
  var bindEvent = function(){
    $(".question-list-content").on('mouseleave','.question-item',function(){
      $(this).children(".question-content").children(".question-answer").hide();
    })
    //点击学科
    $(".question-subject").on("click",".filter-type",function(){
      var count = parseInt($(this).children("var").text());
      var id = $(this).data("id");
      $(this).addClass("active");
      $(this).siblings(".filter-type").removeClass("active");
      getQuestionType(id);
      questionQueryData.typeId = "";
      questionQueryData.subjectId = id;
      questionQueryData.page = 1;
      getQuestionList(questionQueryData)
    })

    //点击来源
    $(".question-from").on("click",".filter-type",function(){
      var count = parseInt($(this).children("var").text());
      var name = $(this).data("name");
      $(this).addClass("active");
      $(this).siblings(".filter-type").removeClass("active");
      questionQueryData.from = name;
      questionQueryData.page = 1;
      getQuestionList(questionQueryData)
    });

    // 点击题型
    $(".question-type").on("click",".filter-type",function(){
      var count = parseInt($(this).children("var").text());
      var id = $(this).data("id");
      $(this).addClass("active");
      $(this).siblings(".filter-type").removeClass("active");
      questionQueryData.typeId = id;
      questionQueryData.page = 1;
      getQuestionList(questionQueryData)
    });

    // 点击查看解析
    $('.question-list-content').on('click','.look-detail',function(){
      var onthis = $(this).parent().parent().parent();
      var id = onthis.data("id"),html = "";
      dm.get(API.root + "/api/question/answerSyllabus/"+id,{id : id}).done(function(res){
        var data = res.data;
        if(data.typeCode == "SELECT"){
          var answer = data.questionAnswerJson.answer.split("|").join("");
          data.questionAnswerJson.answer = answer;
        }else if(data.typeCode == "FILLBLANK"){
          var answer = [];
          $.each(data.questionAnswerJson.answer,function(i,item){
            answer.push(item.split("#$%^&").join("或"));
          })
          data.questionAnswerJson.answer = "<div class='fill-blank'>"+answer.join("，")+ "</div>";
        }else if(data.typeCode == "COMPLEX"){
          var answer = "",analysis = "";
          $.each(data.questionAnswerJson.children,function(index,item){
            if(item.typeCode == "SELECT"){
              answer += "<div class='por'><div class='answer-number'>("+item.number+").</div><div class='text-in'>"+ item.question.answer.split("|").join('') + "</div></div>";
            }else if(item.typeCode == "FILLBLANK"){
              var fillBlank = [];
              $.each(item.question.answer,function(i,fill){
                fillBlank.push(fill.split("#$%^&").join("或"));
              })
              answer += "<div class='por'><div class='answer-number'>("+item.number+").</div><div class='text-in fill-blank'>" + fillBlank.join("，") +"</div></div>";
            }else{
              answer += "<div class='por'><div class='answer-number'>("+item.number+").</div><div class='text-in'>"+item.question.answer + "</div></div>";
            }
          })
          $.each(data.questionAnalysisJson.children,function(index,item){
            analysis += "<div class='por'><div class='analysis-number'>("+item.number+").</div><div class='text-in'>"+item.question.analysis + "</div></div>";
          })
          data.questionAnswerJson.answer = answer;
          data.questionAnalysisJson.analysis = analysis;
        }
        var syllabusName = [];
        $.each(data.syllabus,function(i,syllabus){
          syllabusName.push(syllabus.syllabusName);
        })
        data.syllabusName = syllabusName.join("，");
        html = template('questionAnswer',data);
        onthis.children('.question-content').children(".question-answer").html(html).slideToggle("fast");
        $('.question-content').find(".question-answer p").css("white-space","normal");
        $('.question-content').find(".question-answer span").css("white-space","normal");
      })
    });

    //点击删除试卷
    $(".paper-list-content").on('click',".paper-del",function(){
      var onthis = $(this).closest(".paper-item");
      var id = onthis.data("collect")
      dm.confirm("确定删除该试卷？",function(){
        dm.get(API.root + '/api/collect/paper/remove/'+id,{id : id}).done(function(res){
          getPaperList(paperQueryData);
        });
      });
    })

    //点击下载试卷
    $(".paper-list-content").on('click',".paper-print",function(){
      dm.loading("试卷下载中........");
      var onthis = $(this).closest(".paper-item");
      var paperId = onthis.data("id")
      dm.get(API.host + '/fileyun/paper/produceDoc/'+paperId).done(function(json){
        var link = json.data;
        location.href = link;
      }).always(function(){dm.loading("hide");});
    });

    // 查询我的试卷
    $(".search-paper-btn").on("click",function(){
      var startDate = $("#start-date").val();
      var endDate = $("#end-date").val();
      if(startDate && endDate){
        paperQueryData.startTime = startDate;
        paperQueryData.endTime = endDate;
        paperQueryData.page = 1;
        getPaperList(paperQueryData)
      }else{
        dm.alert("请输入起始日期或截止日期");
      }
    })
  }

  var getDate = function(){
    //配置日期插件
    var start = {
      elem: '#start-date',
      format: 'YYYY-MM-DD', // 分隔符可以任意定义
      max: laydate.now(),
      choose: function(datas){ //选择日期完毕的回调
        $("#start-date").val(datas);
        end.min = datas;
      }
    }
    var end = {
      elem: '#end-date',
      format: 'YYYY-MM-DD', // 分隔符可以任意定义
      max: laydate.now(),
      choose: function(datas){ //选择日期完毕的回调
        $("#end-date").val(datas);
        start.max = datas;
      }
    }
    laydate(start);
    laydate(end);
  }

  var init = function(){
    var grades = ["高一","高二","高三"]
    // 获取老师信息
    $(".user-info").find('.user-name').text(serverData.userData.name);
    $('.user-info').find('.user-subject').text(serverData.userData.subjectName + "老师");
    $('.user-info').find('.location-address').text(serverData.userData.location.split(",").join(""));
    var teachGrades = [];
    $.each(serverData.userData.teachGrades.split(","),function(i,item){
      teachGrades.push(grades[item - 1]);
    })
    $('.user-info').find('.teach-grades').text("执教："+teachGrades.join("、"));
    renderPage();
    getDate();
    bindEvent();
  }

  init();
})