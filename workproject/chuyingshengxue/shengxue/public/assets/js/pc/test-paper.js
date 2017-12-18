$(function(){
  var serverData = {
    paperData : {
      "subjectId": 1,
      "difficult" : 0.65,
      "syllabus": [
        "23",
        "24"
      ],
      "templetId": 1,
      "title": "高中语文随堂练习-20161230",
      "typeConfig": [
        {
          "score": 2,
          "count": 3,
          "ids": [
            {
              "questionId": "SXDXT00001",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是（）</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>",
                "option": [
                  "(2)(3)(1)(5)(4)",
                  "(5)(4)(2)(3)(1)",
                  "(3)(2)(1)(5)(4)",
                  "(5)(4)(3)(2)(1)"
                ]
              }
            },
            {
              "questionId": "SXDXT00002",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是（）</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>",
                "option": [
                  "这可以增加对于“洞庭波涌连天雪”的一点新的理解",
                  "这可以增加对于“洞庭波涌连天雪”的一点新的理解",
                  "(3)(2)(1)(5)(4)",
                  "(5)(4)(3)(2)(1)"
                ]
              }
            },
            {
              "questionId": "SXDXT00003",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是（）</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>",
                "option": [
                  "这可以增加对于“洞庭波涌连天雪”的一点新的理解这可以增加对于“洞庭波涌连天雪”的一点新的理解",
                  "这可以增加对于“洞庭波涌连天雪”的一点新的理解",
                  "这可以增加对于“洞庭波涌连天雪”的一点新的理解",
                  "这可以增加对于“洞庭波涌连天雪”的一点新的理解"
                ]
              }
            }
          ],
          "typeId": 1,
          "typeCode": "SELECT",
          "typeName": "选择题"
        },
        {
          "score": 2,
          "count": 3,
          "ids": [
            {
              "questionId": "SXDXT00004",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是（）</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>",
                "option": [
                  "(2)(3)(1)(5)(4)",
                  "(5)(4)(2)(3)(1)",
                  "(3)(2)(1)(5)(4)",
                  "(5)(4)(3)(2)(1)"
                ]
              }
            },
            {
              "questionId": "SXDXT00005",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是（）</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>",
                "option": [
                  "(2)(3)(1)(5)(4)",
                  "(5)(4)(2)(3)(1)",
                  "(3)(2)(1)(5)(4)",
                  "(5)(4)(3)(2)(1)"
                ]
              }
            },
            {
              "questionId": "SXDXT00006",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是（）</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>",
                "option": [
                  "(2)(3)(1)(5)(4)",
                  "(5)(4)(2)(3)(1)",
                  "(3)(2)(1)(5)(4)",
                  "(5)(4)(3)(2)(1)"
                ]
              }
            }
          ],
          "typeId": 2,
          "typeCode": "SELECT",
          "typeName": "多选题"
        },
        {
          "score": 3,
          "count": 3,
          "ids": [
            {
              "questionId": "SXDXT00007",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是___________</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>"
              }
            },
            {
              "questionId": "SXDXT00008",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是___________</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>"
              }
            },
            {
              "questionId": "SXDXT00009",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>下面的语段已被打乱，排列正确的一项是___________</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>"
              }
            }
          ],
          "typeId": 3,
          "typeCode": "FILLBANK",
          "typeName": "填空题"
        },
        {
          "score": 5,
          "count": 2,
          "ids": [
            {
              "questionId": "SXDXT00010",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>阅读下面一则寓言，用一个精练的句子概括其含意。（不超过20字）</span><br><span>“告诉我一片雪花的重量。”一只知更鸟问一只野鸽。野鸽回答“微不足道。”“那么，让我来给你讲述一个不平常的故事。”知更鸟说道，“我飞落在一棵冷杉的树枝上，紧挨着树干。这时下雪了，小瓣小瓣的雪花缓缓降落。我无事可做，于是就数起了飘落在一棵冷杉上的雪花，确切的数字是141254。当又一片你所讲的微不足道的雪花飘落在树枝上时，枝条折断了。”说完，知更鸟便飞走了。野鸽沉思了一会儿，自言自语道：“兴许只差一个人的力量，和平就会来临了。”</span>"
              }
            },
            {
              "questionId": "SXDXT00011",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>阅读下面一则寓言，用一个精练的句子概括其含意。（不超过20字）</span><br><span>“告诉我一片雪花的重量。”一只知更鸟问一只野鸽。野鸽回答“微不足道。”“那么，让我来给你讲述一个不平常的故事。”知更鸟说道，“我飞落在一棵冷杉的树枝上，紧挨着树干。这时下雪了，小瓣小瓣的雪花缓缓降落。我无事可做，于是就数起了飘落在一棵冷杉上的雪花，确切的数字是141254。当又一片你所讲的微不足道的雪花飘落在树枝上时，枝条折断了。”说完，知更鸟便飞走了。野鸽沉思了一会儿，自言自语道：“兴许只差一个人的力量，和平就会来临了。”</span>"
              }
            }
          ],
          "typeId": 4,
          "typeCode": "QANDA",
          "typeName": "问答题"
        },
        {
          "score": 10,
          "count": 1,
          "ids": [
            {
              "questionId": "SXDXT00012",
              "questionAttach": null,
              "questionStatus": 1,
              "questionEnable": 1,
              "questionContentJson": {
                "title": "<span>阅读下面一则寓言，用一个精练的句子概括其含意。（不超过20字）</span><br><span>“告诉我一片雪花的重量。”一只知更鸟问一只野鸽。野鸽回答“微不足道。”“那么，让我来给你讲述一个不平常的故事。”知更鸟说道，“我飞落在一棵冷杉的树枝上，紧挨着树干。这时下雪了，小瓣小瓣的雪花缓缓降落。我无事可做，于是就数起了飘落在一棵冷杉上的雪花，确切的数字是141254。当又一片你所讲的微不足道的雪花飘落在树枝上时，枝条折断了。”说完，知更鸟便飞走了。野鸽沉思了一会儿，自言自语道：“兴许只差一个人的力量，和平就会来临了。”</span>",
                "children": [
                  {
                    "typeCode": "SELECT",
                    "question": {
                      "title": "<span>下面的语段已被打乱，排列正确的一项是（）</span><br><span>(1).这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>",
                      "option": [
                        "(2)(3)(1)(5)(4)",
                        "(5)(4)(2)(3)(1)",
                        "(3)(2)(1)(5)(4)",
                        "(5)(4)(3)(2)(1)"
                      ]
                    }
                  },
                  {
                    "typeCode": "FILLBANK",
                    "question": {
                      "title": "<span>下面的语段已被打乱，排列正确的一项是___________</span><br><span>这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>"
                    }
                  },
                  {
                    "typeCode": "QANDA",
                    "question": {
                      "title": "<span>下面的语段已被打乱，排列正确的一项是</span><br><span>这可以增加对于“洞庭波涌连天雪”的一点新的理解</span><br><span>(2).本地人说不起白花的是“波”，起白花的是“涌”</span><br><span>(3).“波”和“涌”有这样的区别，我还是第一次听到</span><br><span>(4).有三四级风，洞庭湖里的浪不大，没有起白花</span><br><span>(5).我们登岳阳楼那天下雨，游人不多</span>"
                    }
                  }
                ]
              }
            }
          ],
          "typeId": 5,
          "typeCode": "COMPLEX",
          "typeName": "计算题"
        }
      ]
    }
  }
  var paperData = serverData.paperData;
  var typeConfig = paperData.typeConfig;
  var typeNumber = ["一","二","三","四","五","六","七","八","九"];
  var option = ["A","B","C","D","E","F","G","H","I","J","K"];
  template.config("escape", false);
  // 绑定事件
  var bindEvent = function(){
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

    //保存试卷
    $('.save-paper').on('click',function(){
      var data = getPaperData();
      dm.get('/api/question/save.json',data).done(function(){
        dm.alert("保存成功")
      })
    });

  }

  //渲染题目模板
  var renderModule = function(){
    var partOneData = [],partTwoData = [], number = 0;
    // 试卷第一部分的数据
    $.each(typeConfig,function(i,item){
      item.number = typeNumber[i];
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
      $.each(partOneData,function(i,item){
        item.sigleScore = item.count * item.score;
        $.each(item.ids,function(index,ele){
          number ++;
          ele.number = number;
        })
        sumNumber += item.count;
        paperData.typeConfig.push(item);
        partOne.push(item);
        // console.log(item);
      });
      paperData.numberOne = sumNumber;
      paperData.partOneData = partOne;
    }
    var renderPartTwo = function(){
      var sumNumber = 0;
      var html = "";
      var partTwo = [];
      $.each(partTwoData,function(i,item){
        item.sigleScore = item.count * item.score;
        $.each(item.ids,function(index,ele){
          number ++;
          ele.number = number;
        })
        sumNumber += item.count;
        partTwo.push(item);
        paperData.typeConfig.push(item);
        // console.log(item);
      });
      paperData.numberTwo = sumNumber;
      paperData.partTwoData = partTwo;
    }

    renderPartOne();
    renderPartTwo();
    paperData.optionIcon = option;
    html = template('paperContent',paperData);
    $('.paper-container').html(html);
    getMyPaper();
  }

  // 获取我的试卷
  var getMyPaper = function(){
    // 获取试卷题目
    var totalScore = 0;
    var html = "";
    $.each(paperData.typeConfig,function(i,item){
      item.score = item.count * item.score;
      totalScore += item.score;
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
        sumWidth += $(item).width();
        if($(item).width() > width /2){
          isBranch = 1;
        }
      })
      if(sumWidth < width){isBranch = 2}
      if(isBranch == 1){
        optionItem.css('width',"100%");
      }else if(isBranch == 2){
        optionItem.css("width","auto");
      }else{
        optionItem.css('width',"50%");
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
      var currentId = onthis.attr('id');
      var replaceId, html = "",
      number = onthis.children('.question-content').children('.question-title').find('.question-number').text();
      dm.get('/api/question/replace.json',{id:currentId}).done(function(res){
        var data = res.data;
        var questionId = data.questionId;
        onthis.attr('id',questionId);
        data.number = number;
        if(data.typeCode == "SELECT"){
          data.optionIcon = option;
          html += template('selectModule',data);
        }else if(data.typeCode == "QANDA" || data.typeCode == "FILLBLANK"){
          html += template('fillModule',data);
        }else{
          data.optionIcon = option;
          html += template('complexModule',data);
        }
        onthis.find(".question-content").html(html);
        selectRender();
        replaceHref(number,questionId)
        onthis.fadeOut(500).fadeIn(500);
      })
    })

    // 点击答案
    $('.paper-container').on('click','.menu-answer',function(){
      var onthis = $(this).parent().parent();
      var id = onthis.attr("id"),html = "";
      dm.get("/api/question/answer.json",{id : id}).done(function(res){
        var data = res.data;
        html = template('questionAnswer',data);
        onthis.children('.question-content').append(html);
      })
    })
  }

  var questionTypeEvent = function(){
    // 修改题型的分数
    $('.paper-container').on('input',".single-score",function(){
      var onthis = $(this).parent().parent();
      var number = typeNumber.indexOf(onthis.children('.type-number').find('var').text());
      var count = onthis.find('.count').text();
      var score = $(this).val();
      var sumScore = count * score;
      onthis.children('.sum-score').find('var').text(sumScore);
      changeScore(number,sumScore)
      var questionItem = onthis.siblings('.question-list').find('.question-item');
      $.each(questionItem,function(i,item){
        $(item).data("score",score);
      })
    })
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
      var sumScore = typeTitle.find('.sum-score var').text();
      var count = typeTitle.find('.count').text();
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
        console.log(ids);
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
    renderModule();
    // 选择题选项布局
    selectRender();
    bindEvent();
  }

  init();
})