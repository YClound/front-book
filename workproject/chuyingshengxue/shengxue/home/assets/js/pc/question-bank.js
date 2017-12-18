$(function(){
  $('.paper-form').attr("action",API.root + $(".paper-form").attr("action"));
  var option = ["A","B","C","D","E","F","G","H","I","J","K"];
  template.config("escape", false); 
  // 查询条件
  var queryData = {
    page : 1,
    pageSize : 10,
    filterUsed : -1,
    onlyCollection : -1,
    orderBy:1
  }
  var typeData;
  var subjectId;
  var templateId;
  var subjectName;
  var renderBank = function(subjectId){
    // 获取学科
    dm.get(API.root + "/api/subject/list").done(function(res){
      var list = res.data.list;
      $.each(list,function(i,subject){
        if(subjectId == subject.subjectId){
          subjectName = subject.subjectName;
        }
      });
      $(".bank-container").find('.subject-title').text("学科："+subjectName);
    });

    //获取来源
    dm.get(API.root + "/api/questionProperty/froms").done(function(res){
      var html = '<a href="javascript:;" class="filter-item active" data-id="">全部</a>',data = res.data;
      $.each(data,function(i,froms){
        html += '<a href="javascript:;" class="filter-item" data-id="'+froms.name+'">'+froms.name+'</a>';
      })
      $(".from-list").html(html);
    });

    // 获取难易程度
    dm.get(API.root + "/api/questionProperty/difficults").done(function(res){
      var html = '<a href="javascript:;" class="filter-item active" data-id="">全部</a>', data = res.data;
      $.each(data,function(i,difficult){
        html += '<a href="javascript:;" class="filter-item" data-id="'+difficult.value+'">'+difficult.label+'</a>';
      })
      $(".difficult-list").html(html);
    });

    // 获取题型
    dm.get(API.root + "/api/questionType/list",{typeSubjectId : subjectId}).done(function(res){
      var html = '<a href="javascript:;" class="filter-item active" data-id="">全部</a>', data = res.data;
      typeData = data;
      // console.log(data);
      $.each(data,function(i,type){
        html += '<a href="javascript:;" class="filter-item" data-id="'+type.typeId+'">'+type.typeName+'</a>';
      })
      $(".type-list").html(html);
    });

    // 获取大纲
    dm.get(API.root + "/api/syllabus/listAll",{syllabusSubjectId:subjectId}).done(function(res){
      var data = res.data;
      $(".outline-content").html("");
      var renderMenu = function(menu, parent) {
        // console.log(menu.children);
        var el = $('<a data-toggle="collapse" href="javascript:void(0);" class="collapse-trigger collapsed link-collapse"><span class="outline-menu add fa fa-plus"></span></a><span class="por"><input type="checkbox" name="outlineName" value="'+menu.syllabusId+'" /><div class="checkbox"></span></div><a href="javascript:;" class="choose-syllabus" data-id="'+menu.syllabusId+'">'+menu.syllabusName+'<span class="c-yellow number"></span></a>');
        if(!menu.children || menu.children.length == 0){
          el = $('<input type="checkbox" name="outlineName" value="'+menu.syllabusId+'" /><div class="checkbox"></div><a href="javascript:;" class="choose-syllabus" data-id="'+menu.syllabusId+'">'+menu.syllabusName+'</a>')
        }
        $(parent).append(el);
        if (!menu.children || menu.children.length == 0) {
          return parent;
        }
        var ul = $('<ul class="collapse-content collapse"></ul>');
        for (var i = 0; i < menu.children.length; ++i) {
          var li = $('<li class="por" />');
          renderMenu(menu.children[i], li);
          ul.append(li);
        }
        $(parent).append(ul);
        return parent;
      }
      var render = function(json) {
        // console.log(json)
        var div = $('<div class="outline-item"></div>');
        return renderMenu(json, div);
      }
      var outline = "";
      $.each(data,function(i,item){
        var menu= render(item);
        $(".outline-content").append(menu);
      })
    });

    //获取题目列表
    queryData.questionSubjectId = subjectId;
    getQuestionList(queryData);
  }

  //获取题目列表
  var getQuestionList = function(queryData){
    dm.get(API.root + "/api/question/list",queryData).done(function(res){
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
      $(".question-list").html(html);
      isJoin();
      pagination(data.pager);
      isNewline();
    });
  }

  //绑定查询事件
  var bindEvent = function(){
    // 考试大纲
    $(document).on('click','[data-toggle="collapse"]',function(){
      var $this = $(this);
      if(!$this.hasClass('collapsed')){
        $this.children('span').removeClass('fa-plus').removeClass('add').addClass('fa-minus');
      }else{
        $this.children('span').addClass('fa-plus').addClass('add').removeClass('fa-minus');
      }
    });

    // 关闭我的试卷
    $('.my-paper').on('click','.close-paper-btn',function(){
      $('.my-paper').css('right','-180px');
      $('.my-paper').find('.open-paper-btn').css('display',"block");
      $(this).hide();
    });

    //打开我的试卷
    $('.my-paper').on('click','.open-paper-btn',function(){
      $(this).hide();
      $('.my-paper').find('.close-paper-btn').css('display',"block");
      $('.my-paper').css('right','0px');
    });

    // 显示解析
    $(".question-list").on('mouseleave','.question-item',function(){
      $(this).children(".question-content").children(".question-answer").hide();
    })

    // 选择题型
    $('.type-list').on('click',".filter-item",function(){
      var id = $(this).data("id") || "";
      queryData.questionTypeId = id;
      queryData.page = 1;
      $(this).addClass("active");
      $(this).siblings().removeClass("active");
      getQuestionList(queryData);
    });

    //选择题目难度
    $('.difficult-list').on('click',".filter-item",function(){
      var id = $(this).data("id") || "";
      queryData.questionDifficult = id;
      queryData.page = 1;
      $(this).addClass("active");
      $(this).siblings().removeClass("active");
      getQuestionList(queryData);
    });

    // 选择题目来源
    $('.from-list').on('click',".filter-item",function(){
      var id = $(this).data("id") || "";
      queryData.questionFrom = id;
      queryData.page = 1;
      $(this).addClass("active");
      $(this).siblings().removeClass("active");
      getQuestionList(queryData);
    });

    //过滤使用过的试题
    $('.filter-used').on("change",".used-question",function(){
      if($(this).is(':checked')){
        queryData.filterUsed = 1;
      }else{
        queryData.filterUsed = -1;
      }
      queryData.page = 1;
      getQuestionList(queryData);
    });

    //过滤收藏过的试题
    $('.fliter-collect').on("change",".collect-question",function(){
      if($(this).is(':checked')){
        queryData.onlyCollection = 1;
      }else{
        queryData.onlyCollection = -1;
      }
      queryData.page = 1;
      getQuestionList(queryData);
    });

    //题目显示默认顺序
    $(".filter-order").on("click",".default-order",function(){
      $(this).addClass("active");
      $(this).siblings().removeClass("active");
      $(this).siblings().find(".down-circle").removeClass("up");
      queryData.orderBy = 1;
      queryData.page = 1;
      getQuestionList(queryData);
    })

    //浏览次数显示
    $(".filter-order").on("click",".view-order",function(){
      if($(this).hasClass("active")){
        var isUp = $(this).find(".down-circle").hasClass("up");
        if(isUp){
          $(this).find(".down-circle").removeClass("up");
          queryData.orderBy = -2;
        }else{
          $(this).find(".down-circle").addClass("up");
          queryData.orderBy = 2;
        }
      }else{
        $(this).addClass("active");
        $(this).siblings().removeClass("active");
        $(this).siblings().find(".down-circle").removeClass("up");
        queryData.orderBy = -2;
      }
      queryData.page = 1;
      getQuestionList(queryData);
    });

    //题目的难易程度
    $(".filter-order").on("click",".difficult-order",function(){
      if($(this).hasClass("active")){
        var isUp = $(this).find(".down-circle").hasClass("up");
        if(isUp){
          $(this).find(".down-circle").removeClass("up");
          queryData.orderBy = -3;
        }else{
          $(this).find(".down-circle").addClass("up");
          queryData.orderBy = 3;
        }
      }else{
        $(this).addClass("active");
        $(this).siblings().removeClass("active");
        $(this).siblings().find(".down-circle").removeClass("up");
        queryData.orderBy = -3;
      }
      queryData.page = 1;
      getQuestionList(queryData);
    });

    // 切换多选
    $(".left-top").on("click",".changeMult",function(){
      $(this).hide();
      $('.choose-all-syllabus').hide();
      $('.changeSingl').css("display","block");
      $('.mult-button').css("display","flex");
      $(".outline-content").find(".checkbox").css("display","inline-block");
      $(".outline-content").find('[type="checkbox"]').show();
      $(".outline-content").find('[type="checkbox"]').removeAttr("checked");
      $('.outline-content').find(".choose-syllabus").each(function(){
        if($(this).hasClass("active")){
          $(this).parent().children('[type="checkbox"]').prop("checked","checked");
        }
      })
    })
    // 切换单选
    $(".left-top").on("click",".changeSingl",function(){
      $(".changeMult").css("display","block");
      $('.choose-all-syllabus').css("display","block");
      $(this).hide();
      $('.mult-button').hide();
      $(".outline-content").find(".checkbox").hide();
      $(".outline-content").find('[type="checkbox"]').hide();
    });

    // 全部知识点
    $('.left-top').on("click",".choose-all-syllabus",function(){
      var syllabusId = [];
      var syllabusItem = $(".outline-content").find(".choose-syllabus");
      $.each(syllabusItem,function(i,item){
        syllabusId.push($(item).data("id"));
      })
      queryData.questionSyllabus = JSON.stringify(syllabusId);
      queryData.page = 1;
      getQuestionList(queryData);
    })

    // 多选时全选
    $('.left-top').on("click",".choose-all",function(){
      $(".outline-content").find('[type="checkbox"]').each(function(){
        $(this).prop("checked","checked");
      })
    });

    //清空选择
    $('.left-top').on("click",".reset-choose",function(){
      $(".outline-content").find('[type="checkbox"]').each(function(){
        $(this).removeAttr("checked","checked");
      })
    });

    // 确定查询
    $('.left-top').on("click",".confirm-choose",function(){
      var syllabusId = [];
      $(".outline-content").find('[type="checkbox"]').each(function(){
        if($(this).is(":checked")){
          syllabusId.push($(this).val());
        }
      });
      queryData.questionSyllabus = JSON.stringify(syllabusId);
      queryData.page = 1;
      getQuestionList(queryData);
    });

    // 单选考纲
    $(".outline-content").on("click",".choose-syllabus",function(){
      var syllabusId = [];
      $(".outline-content").find('.choose-syllabus').removeClass("active");
      $(this).addClass("active");
      var syllabusContent = $(this).parent().siblings(".collapse-content");
      if(syllabusContent.length>0){
        var choiceSyllabus = syllabusContent.find(".choose-syllabus");
        $.each(choiceSyllabus,function(i,item){
          syllabusId.push($(item).data("id"))
        })
      }else{
        syllabusId.push($(this).data("id"));
      }
      console.log(syllabusId)
      queryData.questionSyllabus = JSON.stringify(syllabusId);
      queryData.page = 1;
      getQuestionList(queryData);
    });

    // 多选选择考点
    $(".outline-content").on("change",'[name="outlineName"]',function(){
      var outlineCollapse = $(this).parent().siblings(".collapse-content");
      if($(this).is(":checked")){
        outlineCollapse.find('[type="checkbox"]').prop("checked","checked");
      }else{
        outlineCollapse.find('[type="checkbox"]').removeAttr("checked");
      }
      var isChecked = true;
      var children = $(this).closest(".collapse-content").find('[type="checkbox"]');
      $.each(children,function(i,item){
        if(!$(item).is(":checked")){
          isChecked = false;
        }
      })
      if(isChecked){
        $(this).closest(".collapse-content").parent().children('span').find('[type="checkbox"]').prop("checked","checked");
      }else{
        $(this).closest(".outline-item").children('span').find('[type="checkbox"]').removeAttr("checked");
        $(this).closest(".collapse-content").parent().children('span').find('[type="checkbox"]').removeAttr("checked");
      } 
    });

    // 查看解析 
    $('.question-list').on('click','.look-detail',function(){
      var onthis = $(this).closest('.question-item');
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
              answer += "<div class='por'><div class='answer-number'>("+item.number+").</div><div class='text-in'>"+item.question.answer+ "</div></div>";
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
        $('.question-content').find(".question-answer span").css("white-space","normal")
      })
    })

    //收藏试卷
    $(".question-list").on("click",".collect-question",function(){
      if(!serverData.userData){
        location.href = 'index.html?redirect_url='+location.href;
      }
      var id = $(this).closest(".question-item").data('id');
      var $this = $(this);
      dm.post(API.root + '/api/collect/question/add/'+id,{id : id}).done(function(res){
        var collectId = res.data;
        $this.closest(".question-item").data('collect',collectId);
        $this.text("取消收藏");
        $this.removeClass('collect-question');
        $this.addClass('cancle-collect');
      })
    })

    //取消收藏
    $(".question-list").on("click",".cancle-collect",function(){
      var id = $(this).closest(".question-item").data('collect');
      var $this = $(this);
      dm.post(API.root + '/api/collect/question/remove/'+id,{id : id}).done(function(){
        $this.closest(".question-item").data('collect',"0");
        $this.text("收藏试题");
        $this.removeClass('cancle-collect');
        $this.addClass('collect-question');
      })
    })

    // 加入试卷
    $(".question-list").on("click",".join-paper",function(){
      if(!serverData.userData){
        location.href = 'index.html?redirect_url='+location.href;
      }
      $(this).removeClass("join-paper").addClass("remove-paper");
      $(this).text("移出试卷")
      var questionItem = $(this).closest('.question-item');
      var questionId = questionItem.data("id");
      var typeId = questionItem.data("type");
      var typeName = questionItem.data("typename");
      var html = "",data;
      $.each(typeData,function(i,type){
        if(type.typeId == typeId){
          data = {
            typeId : type.typeId,
            typeName : type.typeName,
            questionId : questionId
          }
        }
      });
      var questionTypeItem = $(".question-type-list").find(".question-type-item") || "";
      // 获取题目题型
      if(questionTypeItem.length){
        $.each(questionTypeItem,function(i,item){
          if($(item).data("type") == typeId){
            // console.log("typeId"+typeId)
            var id = $(item).data("id").toString();
            var questionIds = id + "," + questionId;
            var number = parseInt($(item).find(".number var").text()) + 1;
            $(item).find(".number var").text(number)
            $(item).data("id",questionIds);
            html = "";
            return false;
          }else{
            // console.log(html)
            data.count = 1,
            html = template("questionTypeList",data); 
          }
          return;
        })
        $(".question-type-list").append(html);
      }else{
        data.count = 1,
        html = template("questionTypeList",data); 
        $(".question-type-list").append(html);
      } 
    });

    //移出试卷
    $(".question-list").on("click",".remove-paper",function(){
      $(this).removeClass("remove-paper").addClass("join-paper");
      $(this).text("加入试卷")
      var questionItem = $(this).closest('.question-item');
      var questionId = questionItem.data("id").toString();
      var typeId = questionItem.data("type");
      var questionTypeItem = $(".question-type-list").find(".question-type-item") || "";
      // 获取题目题型
        $.each(questionTypeItem,function(i,item){
          if($(item).data("type") == typeId){
            var id = $(item).data("id").toString().split(",");
            // console.log(id.indexOf(questionId),questionId)
            id.splice(id.indexOf(questionId),1);
            var questionIds = id;
            // console.log(questionIds,id);
            var number = parseInt($(item).find(".number var").text()) - 1;
            $(item).find(".number var").text(number);
            $(item).data("id",questionIds);
            // console.log($(item).data("id"));
            if(number == 0){
              $(item).remove();
            }
          }
        })
    });

    //生成试卷
    $(".make-paper").on('click',function(){
      var typeConfig = getMyPaperQuestion();
      $('[name="typeConfig"]').val(JSON.stringify(typeConfig));
      $('[name="subjectId"]').val(subjectId);
      $('[name="templetId"]').val(templateId);
      $('[name="title"]').val("高中"+subjectName+"试卷");
      // console.log($('[name="title"]').val(),$('[name="typeConfig"]').val(),subjectId,templateId);
      $('.paper-form').submit();
    });
  }

  // 获取的我试卷中的题型内容
  var getMyPaperQuestion = function(){
    var typeConfig = []
    var questionTypeItem = $(".question-type-list").find(".question-type-item") || "";
    if(questionTypeItem.length){
      $.each(questionTypeItem,function(i,item){
        var typeId = $(item).data("type");
        var id = $(item).data("id").toString().split(",");
        var singleScore,score;
        var count = $(item).find(".number var").text();
        $.each(typeData,function(index,type){
          if(typeId == type.typeId){
            var typeCode = type.typeModule;
            if(typeCode == "SELECT"){
              singleScore = "2";
            }else if(typeCode == "FILLBLANK"){
              singleScore = "3";
            }else if(typeCode == "QANDA"){
              singleScore = "5";
            }else{
              singleScore = "10";
            }
            score = parseInt(count) * parseInt(singleScore);
          }
        })
        typeConfig.push({
          score : score,
          count : count,
          typeId : typeId,
          ids: id
        })
      })
      return typeConfig;
    }
  }

  // 判断题目是否已经加入试卷中
  var isJoin = function(){
    var questionItem = $(".question-list").find(".question-item");
    var paperQuestionId = getMyPaperQuestion() || "";
    var ids = [];
    if(paperQuestionId.length > 0){
      $.each(paperQuestionId,function(i,type){
        // console.log(type)
        $.each(type.ids,function(index,questionId){
          ids.push(questionId);
        })
      });
      $.each(questionItem,function(i,item){
        var itemId = $(item).data("id");
        $.each(ids,function(index,id){
          if(itemId == id){
            $(item).find(".operat-btn").find("a:first-of-type").removeClass("join-paper").addClass("remove-paper").text("移出试卷");
          }
        })
      })
    }
    // console.log(ids,questionItem);
  }

  // 分页
  var pagination = function(data){
    var pager = data;
    console.log(pager)
    var Paging = $(".pagination-list").paging(pager.total, {
      format: '[< ncnnn >]',
      onSelect: function(page) {
        // add code which gets executed when user selects a page
        if(queryData.page != page){
          queryData.page = page;
          dm.get(API.root + "/api/question/list",queryData).done(function(res){
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
            $(".question-list").html(html);
            isJoin();
          });
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

  //当页面滚动时固定左侧筛选条件
  var leftPosition = function(){
    var ht = $(document).height() - 80;
    var isScrollLocked = false;
    $(window).scroll(function(){
      if (!isScrollLocked) {
        isScrollLocked = true;
        var top = $(window).scrollTop();
        // console.log(ht,top,$('html').height(),document.body.clientHeight,window.screen.height);
        var Hei = 105;
        if (top <= Hei) {
          $(".bank-container").removeClass("container-position");
          $(".bank-right").css("margin-left","0px")  
        }
        if (top > Hei) {  
          $(".bank-container").addClass("container-position");
          $(".outline-content").css("max-height","400px");
          $(".outline-content").css("height","auto");
          $(".bank-right").css("margin-left","270px")  
        }
        // console.log($('html').height() - top)
        if(($('html').height() - top) <= ht){
          $(".bank-left").css({"bottom":"400px","top":"auto"});
           $(".outline-content").css("max-height","300px");
        }else{
          $(".bank-left").css({"bottom":"auto","top":"0px"});
           $(".outline-content").css("max-height","400px");
        }
        isScrollLocked = false;
      }
    });
  };

  // 判断选项的行数
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

  var init = function(){
    // 获取学科和版式
    var localhost = location.href.split("?")[1];
    if(localhost){
      var data = localhost.split("&");
      subjectId = data[0].split("=")[1];
      if(data[1]){
        templateId = data[1].split("=")[1]
      }else{
        templateId = "1";
      }
    }
    renderBank(subjectId);
    bindEvent();
    leftPosition();
  }

  // 初始化
  init();
})