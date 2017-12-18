$(function(){
  var paperData = serverData.paperData;
  var typeConfig = paperData.typeConfig;
  var paperId = paperData.paperId;
  var typeNumber = ["一","二","三","四","五","六","七","八","九"];
  var option = ["A","B","C","D","E","F","G","H","I","J","K"];
  template.config("escape", false);
  var localhost = API.root;
  // 绑定事件
  var bindEvent = function(){
    // 绑定题目事件
    questionEvent();
    // 关闭我的试卷
    $('.my-paper').on('click','.close-paper-btn',function(){
      $('.my-paper').css('right','-180px');
      $('.my-paper').find('.open-paper-btn').css('display',"block");
      $(this).hide();
    })
    $('.my-paper').on('click','.open-paper-btn',function(){
      $(this).hide();
      $('.my-paper').find('.close-paper-btn').css('display',"block");
      $('.my-paper').css('right','0px');
    });

    //点击我的试卷题号
    $('.my-paper').on('click',".number",function(){
      $(".my-paper").find('.question-title').removeClass('active');
      $(".my-paper").find('.number').removeClass('active');
      $(this).addClass('active');
      $(this).closest('.question-number').siblings(".question-title").addClass('active');
    })

    //修改题型
    questionTypeEvent();

    //修改试卷名称
    $('.paper-container').on('click',".edit-title",function(){
      $('[name="title"]').val($('.paper-title').find("h2").text());
    })

    // 保存修改的试卷名称
    $(document).on("click",'.save-edit-title',function(){
      $('.paper-title').find("h2").text($('[name="title"]').val());
    })
    //保存试卷
    $('.save-paper').on('click',function(){
      var data = getPaperData();
      dm.post(localhost + '/api/paper/save',data).done(function(res){
        paperId = res.data;
        dm.alert("保存成功");
      })
    });

    //下载试卷
    $(".paper-download").on('click',function(){
      var data = getPaperData();
      dm.post(localhost + '/api/paper/save',data).done(function(res){
        // console.log(res);
        paperId = res.data;
        dm.loading("试卷下载中........");
        if(res.code == 0){
          var host = API.host+'/fileyun/paper/produceDoc/'+paperId;
          //host = "http://127.0.0.1:8080/paper/produceDoc/"+paperId;
          dm.get(host).done(function(json){
            var link = json.data;
          //link = "http://www.des-club.com/fileyun//files/default/-1/20171/png/2017011610096321HB.png";
            location.href = link;
          }).always(function(){dm.loading("hide");});
        }
      });
    });
  }

  //渲染题目模板
  var renderModule = function(){
    var partOneData = [],partTwoData = [], number = 0,typeNum = 0;
    // 试卷第一部分的数据
    $.each(typeConfig,function(i,item){
      if(item.typeCode === "SELECT"){
        partOneData.push(item)
      }else{
        partTwoData.push(item);
      }
    });
    paperData.typeConfig = [];
    var renderPartOne = function(){
      var sumNumber = 0;
      var html = "";
      var partOne = [];
      if(paperId !=0 && paperId){
        $.each(partOneData,function(i,item){
          item.number = typeNumber[parseInt(item.number) - 1];
          var singleScore = parseInt(item.score) / parseInt(item.count);
          item.singleScore = '每题<var class="typeScore">'+singleScore+'</var>分,';
          sumNumber += parseInt(item.count);
          paperData.typeConfig.push(item);
          partOne.push(item);
          // console.log(item);
        });
      }else{
        paperId = "";
        $.each(partOneData,function(i,item){
          item.number = typeNumber[typeNum];
          typeNum++;
          var singleScore = parseInt(item.score) / parseInt(item.count);
          item.singleScore = '每题<var class="typeScore">'+singleScore+'</var>分,';
          $.each(item.ids,function(index,ele){
            number ++;
            ele.number = number;
            ele.score = singleScore;
          })
          sumNumber += parseInt(item.count);
          paperData.typeConfig.push(item);
          partOne.push(item);
          // console.log(item);
        });
      }
      paperData.numberOne = sumNumber;
      paperData.partOneData = partOne;
    }
    var renderPartTwo = function(){
      var sumNumber = 0;
      var html = "";
      var partTwo = [];
      if(paperId !=0 && paperId){
        $.each(partTwoData,function(i,item){
          item.number = typeNumber[parseInt(item.number) - 1];
          var isSame = isTheSame(item);
          // 判断分数是否一样
          if(item.typeCode == "FILLBLANK" || isSame){
            var singleScore = parseInt(item.score) / parseInt(item.count);
            item.singleScore = '每题<var class="typeScore">'+singleScore+'</var>分,';
          }else{
            var html = "";
            $.each(item.ids,function(index,question){
              html += "第"+question.number+"题"+question.score+"分，"
            });
            item.singleScore = html;
          }
          
          sumNumber += parseInt(item.count);
          partTwo.push(item);
          paperData.typeConfig.push(item);
          // console.log(item);
        });
      }else{
        paperId = "";
        $.each(partTwoData,function(i,item){
          item.number = typeNumber[typeNum];typeNum++;
          var singleScore = parseInt(item.score) / parseInt(item.count);
          item.singleScore = '每题<var class="typeScore">'+singleScore+'</var>分,';
          $.each(item.ids,function(index,ele){
            number ++;
            ele.number = number;
            ele.score = singleScore;
          })
          sumNumber += parseInt(item.count);
          partTwo.push(item);
          paperData.typeConfig.push(item);
          // console.log(item);
        });
      }
      paperData.numberTwo = sumNumber;
      paperData.partTwoData = partTwo;
    }

    renderPartOne();
    renderPartTwo();
    paperData.optionIcon = option;
    html = template('paperContent',paperData);
    $('.paper-container').html(html);
    getMyPaper();
    dm.loading("hide");
  }

  // 判断一类题型里面的题目的分数是否一致
  var isTheSame = function(item){
    var ids = item.ids;
    var count = 0, singleScore = parseInt(item.score) / parseInt(item.count);
    $.each(ids,function(i,id){
      if(singleScore == parseInt(id.score)){
        count++;
      }
    });
    if(count == item.count){
      return true;
    }else{
      return false;
    }
  }

  // 获取我的试卷
  var getMyPaper = function(){
    // 获取试卷题目
    var totalScore = 0;
    var html = "";
    $.each(paperData.typeConfig,function(i,item){
      totalScore += parseInt(item.score);
      html += template('paperTypeList',item);
    })
    $('.question-type-list').html(html);
    $('.paper-score').children('var').text(totalScore);
  }
  // 选择题选项布局
  var selectRender = function(){
    $.each($('.question-option'),function(i,ele){
      var width = $(this).width();
      var isBranch = 0,sumWidth = 0;
      var optionItem = $(this).find('.option-item');
      $.each(optionItem,function(index,item){
        sumWidth += ($(item).width() + 20);
        // console.log('item:'+ $(item).width(),'width:'+width)
        if($(item).width() > width /2){
          isBranch = 1;
        }
      })
      if(sumWidth < width){isBranch = 2}
      if(isBranch == 1){
        optionItem.css('width',"100%");
      }else if(isBranch == 2){
        optionItem.css({"width":"auto","paddingRight" : "20px"});
      }else{
        optionItem.css('width',"48%");
      }
    });
    isNewline();
  }
  // 判断选项的行数
  var isNewline = function(){
    var optionItem = $('.question-option').find('.option-item');
    $.each(optionItem,function(index,item){
      var height = $(item).find('.text-in').height();
      var imgHeight = $(item).find('.text-in img').height();
      // console.log(height,imgHeight);
      if(height <= imgHeight){
        $(item).find(".option-icon").css({"top" : "50%","marginTop" : "-15.5px"})
      }else if(imgHeight){
        $(item).find(".option-icon").css({"top" : ((imgHeight-25)/2) + "px","marginTop" : "0px"})
      }
    })
  }

  // 题目相关的事件
  var questionEvent = function(){
    // 题目hover效果
    $(".paper-container").on('mouseover','.question-item',function(){
      $(this).children(".question-menu").show();
      $(this).css("borderColor","#1f7ace");
    })
    $(".paper-container").on('mouseleave','.question-item',function(){
      $(this).children(".question-menu").hide();
      $(this).css("borderColor","transparent");
      $(this).children(".question-content").children(".question-answer").hide();
    })

    //题目上移下移
    is_top_down();
    //题目上移
    $('.paper-container').on('click',".menu-up",function(){
      var onthis = $(this).parent().parent();
      var spanNum1 = onthis.find(".question-number").html();
      var spanNum2 = onthis.prev().find(".question-number").html();
      var getUp = onthis.prev();
      if (getUp.html()==null){return;}
      onthis.prev().find(".question-number").html(spanNum1);
      onthis.find(".question-number").html(spanNum2);
      $(onthis).after(getUp);
      onthis.fadeOut(500).fadeIn(500);
      changeHref(spanNum1,spanNum2,"up");
      is_top_down();//题型的第一题不出上移，最后一个不出下移 
    })
    // 题目下移
    $('.paper-container').on('click',".menu-down",function(){
      var onthis = $(this).parent().parent();
      var spanNum1 = onthis.find(".question-number").html();
      var spanNum2 = onthis.next().find(".question-number").html();
      var getDown = onthis.next();
      if (getDown.html()==null){return;}
      onthis.next().find(".question-number").html(spanNum1);
      onthis.find(".question-number").html(spanNum2);
      $(getDown).after(onthis);
      onthis.fadeOut(500).fadeIn(500);
      changeHref(spanNum1,spanNum2,"down");
      is_top_down();//题型的第一题不出上移，最后一个不出下移 
    })

    // 换题
    $('.paper-container').on('click',".menu-replace",function(){
      var onthis = $(this).parent().parent();
      var currentId = onthis.attr('id'),index = 0;
      var replaceId, html = "",
      number = onthis.children('.question-content').children('.question-title').find('.question-number').text();
      var getQuestion = function(currentId){
        dm.get(localhost +'/api/question/replace/'+ currentId,{id:currentId}).done(function(res){
          var data = res.data;
          var questionId = data.questionId;
          var questionIdList = [];
          var questionItem = $(".question-list").find(".question-item");
          $.each(questionItem,function(index,item){
            questionIdList.push($(item).attr("id"));
          })
          // console.log(isRepeat(questionId,questionIdList))
          if(isRepeat(questionId,questionIdList)){
            index++;
            if(index >= 3){
              dm.alert("没有相似的题目了")
              return;
            }else{
              getQuestion(currentId);
            }
          }else{
            onthis.attr('id',questionId);
            data.number = number;
            if(data.isCollect != 0){
              onthis.find(".menu-collect").removeClass("menu-collect").addClass("menu-cancle-collect").find("span").text("取消收藏");
              onthis.data("collect",data.isCollect);
              // console.log(onthis.data("collect"))
            }
            if(data.typeCode == "SELECT"){
              data.optionIcon = option;
              html += template('selectModule',data);
            }else if(data.typeCode == "QANDA" || data.typeCode == "FILLBLANK"){
              html += template('fillModule',data);
            }else{
              data.optionIcon = option;
              html += template('complexModule',data);
            }
            onthis.children(".question-content").html(html);
            selectRender();
            replaceHref(number,questionId)
            onthis.fadeOut(500).fadeIn(500);
          }
        })
      } 
      getQuestion(currentId)
    })

    var isRepeat = function(id,ids){
      // console.log(id,ids)
      var isSame = false;
      $.each(ids,function(index,item){
        if(id == item){
          isSame = true;
        }
      })
      return isSame;
    }

    // 点击答案
    $('.paper-container').on('click','.menu-answer',function(){
      var onthis = $(this).parent().parent();
      var id = onthis.attr("id"),html = "";
      dm.get(localhost + "/api/question/answerSyllabus/"+id,{id : id}).done(function(res){
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
        $('.question-content').find(".question-answer span").css("white-space","normal")
      })
    });

    //收藏题目
    $('.paper-container').on('click',".menu-collect",function(){
      var id = $(this).closest(".question-item").attr("id");
      var $this = $(this);
      dm.post(API.root + '/api/collect/question/add/'+id,{id : id}).done(function(res){
        var collectId = res.data;
        $this.closest(".question-item").data('collect',collectId);
        $this.find("span").text("取消收藏");
        $this.removeClass('menu-collect');
        $this.addClass('menu-cancle-collect');
      })
    })

    //取消收藏
    $('.paper-container').on('click',".menu-cancle-collect",function(){
      var id = $(this).closest(".question-item").data("collect");
      var $this = $(this);
      dm.post(API.root + '/api/collect/question/remove/'+id,{id : id}).done(function(){
        $this.closest(".question-item").data('collect',"0");
        $this.find("span").text("收藏题目");
        $this.removeClass('menu-cancle-collect');
        $this.addClass('menu-collect');
      })
    })
  }

  var questionTypeEvent = function(){
    // 修改题型的分数
    $('.paper-container').on('click',".edit-type-title",function(){
      var onthis = $(this).parent();
      var number = typeNumber.indexOf(onthis.children('.title-content').find('.type-number var').text());
      var count = parseInt(onthis.find('.count').text());
      var score = parseInt(onthis.find(".single-score .typeScore").text()) || "";
      var id = onthis.parent().data("id");
      editTypeTitle(id,number,count,score);
    })

    // 编辑题型名称
    var editTypeTitle = function(id,number,count,score){
      console.log(id,number,count,score)
      var paperType = $('.paper-container').find('.paper-type'),onthis;
      $.each(paperType,function(i,item){
        console.log(id);
        if($(item).data("id") == id){
          onthis = $(item).children(".type-title");
          console.log(onthis);
        }
      })
      $('[name="typeScore"]').val(score);
      console.log(onthis);
      $('.save-type-score').off("click").on('click',function(){
        var typeScore = parseInt($('[name="typeScore"]').val());
        var sumScore = parseInt(typeScore) * parseInt(count);
        onthis.children(".title-content").find(".single-score").html('每题<var class="typeScore">'+typeScore+'</var>分');
        onthis.children(".title-content").find(".sum-score var").text(sumScore);
        changeScore(number,sumScore)
        var questionItem = onthis.siblings('.question-list').find('.question-item');
        $.each(questionItem,function(i,item){
          $(item).data("score",typeScore);
        })
      })
    }

    // 修改每个小题的分数
    $('.paper-container').on('click',".menu-score",function(){
      var onthis = $(this).parent().parent();
      var paperType = onthis.parent().parent();
      $('[name="questionScore"]').val(onthis.data("score"));
      editQuestionScore(onthis,paperType);
    })
    var editQuestionScore = function(onthis,papertype){
      $('.save-question-score').off("click").on('click',function(){
        var questionScore = parseInt($('[name="questionScore"]').val());
        var sumScore = 0, html = "";
        onthis.data("score",questionScore)
        var questionItem = onthis.parent('.question-list').find('.question-item');
        $.each(questionItem,function(i,item){
          var number = parseInt($(item).children(".question-content").children(".question-title").find(".question-number").text());
          var score = $(item).data("score");
          sumScore += score;
          html += "第"+number+"题"+score+"分，"
        });
        console.log(html,papertype);
        papertype.children(".type-title").find(".single-score").html(html);
        papertype.children(".type-title").find(".sum-score var").text(sumScore);
        var number = typeNumber.indexOf(papertype.children('.type-title').find('.type-number var').text());
        changeScore(number,sumScore)
      })
    }
  }

  /*如果是第一个则不显示上移，如果是最后一个则不显示下移*/
  var is_top_down = function (){
    $(".question-list").each(function(){
        var _this = $(this);
        //默认全部显示
        _this.find(".question-item").find(".menu-up").show();
        _this.find(".question-item").find(".menu-down").show();
        //判断如果是第一个或最后一个则让其隐藏
        _this.find(".question-item:first-child").find(".menu-up").hide();
        _this.find(".question-item:last-child").find(".menu-down").hide(); 
    });
  }

  // 题目上移下移时交换a的链接
  var changeHref = function(num1,num2,opt){
    var currentNum = parseInt(num1);
    var changeNum = parseInt(num2)
    console.log(currentNum,changeNum)
    $.each($('.number'),function(i,item){
      var number = parseInt($(item).find('span').text());
      var href = $(item).attr("href");
      if(number == currentNum){
        if(opt == "up"){
          var getUpHref = $(item).prev().attr("href");
          $(item).attr("href",getUpHref)
          $(item).prev().attr("href",href);
        }else{
          var getUpHref = $(item).next().attr("href");
          $(item).attr("href",getUpHref)
          $(item).next().attr("href",href);
        }
      }
    })
  }

  //题目换题时更改a链接
  var replaceHref = function(num,id){
    var currentNum = parseInt(num);
    $.each($('.number'),function(i,item){
      var number = parseInt($(item).find('span').text());
      if(currentNum == number){
        $(item).attr("href","#"+id);
      }
    })
  }

  // 修改我的试卷的分数
  var changeScore = function(number,sumScore){
    var papeType = $('.my-paper').find('.question-type');
    var totalScore = 0;
    $.each(papeType,function(i,item){
      if( i == number){
        $(item).find('.type-total-score').text(sumScore);
      }
      var score = parseInt($(item).find('.type-total-score').text());
      totalScore += score;
    })
    $('.paper-score').children('var').text(totalScore);
  }

  var getPaperData = function(){
    var title = $('.paper-title').children('h2').text();
    var paperScore = $(".paper-container").find(".paper-score var").text();
    var typeConfig = [];
    $.each($('.paper-type'),function(i,paperType){
      var typeId = $(paperType).data("id");
      var typeTitle = $(paperType).children('.type-title');
      var number = parseInt(typeNumber.indexOf(typeTitle.find('.type-number var').text()) + 1);
      var sumScore = parseInt(typeTitle.find('.sum-score var').text());
      var count = parseInt(typeTitle.find('.count').text());
      var ids = [];
      var questionItem = $(paperType).children('.question-list').find('.question-item');
      $.each(questionItem,function(index,item){
        var questionTitle = $(item).children(".question-content").children('.question-title');
        var number = parseInt(questionTitle.find(".question-number").text());
        var score = $(item).data("score");
        var id = $(item).attr('id');
        ids.push({
          id : id,
          score : score,
          number : number
        });
        // console.log(ids);
      });
      typeConfig.push({
        typeId : typeId,
        number : number,
        score : sumScore,
        count : count,
        ids : ids
      })
    });
    var data = {
      paperId : paperId,
      subjectId : paperData.subjectId,
      title : title,
      score : paperScore,
      templetId : paperData.templetId,
      syllabus : JSON.stringify(paperData.syllabus),
      difficult : paperData.difficult,
      typeConfig : JSON.stringify(typeConfig)
    }
    return data;
  }

  var init = function(){
    dm.loading("正在加载数据........");
    renderModule();
    // 选择题选项布局
    selectRender();
    bindEvent();
  }

  init();
})