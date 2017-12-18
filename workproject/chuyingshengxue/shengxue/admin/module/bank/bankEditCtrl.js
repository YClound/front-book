angular.module('awApp').controller("bankEditCtrl", ["$scope", "$state" ,"$stateParams","resource",
  function(scope, state,stateParams,resource) {
    var questionTypeDao = resource('/questionType');
    var questionDao = resource('/question');
    var syllabusDao = resource('/syllabus')
    var subjectDao = resource('/subject');
    var questionProDao = resource('/questionProperty');
    var questionId = stateParams.questionId,
        subjectId = stateParams.subjectId,
        questionIdList = [];
    var syllabus,selectedNode;
    var choiceIcon = ["A","B","C","D","E","F","G","H","I","J","K"];
    // 题目模板
    scope.templetList = [{'value':"SELECT","name":"选择题"},{"value":"FILLBLANK","name":"填空题"},{"value":"QANDA","name":"问答题"}];
    // 编辑器配置
    scope.config = {
      initialFrameWidth:500,
      initialFrameHeight:38,
      toolbar:[
      'source | undo redo | removeformat |',
          'cleardoc  | fontfamily fontsize' ,
          'image  ',
          '| horizontal preview  fullscreen'
      ]
    };
    scope.all_config = {
      initialFrameWidth:800,
      initialFrameHeight:300,
      toolbar:[
      'source | undo redo | removeformat |',
          'cleardoc  | fontfamily fontsize' ,
          'image  ',
          '| horizontal preview  fullscreen'
      ]
    };
    scope.min_config = {
      initialFrameWidth:400,
      initialFrameHeight:38,
      toolbar:[
      'source | undo redo | removeformat |',
          'cleardoc  | fontfamily fontsize' ,
          'image  ',
          '| horizontal preview  fullscreen'
      ]
    };
    //大纲配置
    scope.treeOptions = {
      nodeChildren: "children",
      multiSelection: true,
      dirSelectable: true,
      injectClasses: {
        li: "outline-item",
        liSelected: "a7",
        iExpanded: "outline-menu fa fa-minus",
        iCollapsed: "outline-menu add fa fa-plus",
        iLeaf: "",
        label: "a6",
        labelSelected: "c-blue"
      },
      isSelectable: function(node) {
        console.log(node.syllabusLevel)
        return node.syllabusLevel !== 1;
      }
    };
    //选择考点
    var choiceSyllabus = function(list,questionSyllabus){
      angular.forEach(list,function(item,index){
        angular.forEach(questionSyllabus,function(syllabusId,i){
          if(syllabusId == item.syllabusId){
            scope.selectedNodes.push(item);
          }else{
            if(item.children){
              angular.forEach(item.children,function(childrenItem){
                if(syllabusId == childrenItem.syllabusId){
                  scope.expandedNodes.push(item);
                  scope.selectedNodes.push(childrenItem);
                  console.log(scope.selectedNodes)
                }else{
                  if(childrenItem.children){
                    angular.forEach(childrenItem.children,function(subItem){
                      if(syllabusId == subItem.syllabusId){
                        scope.expandedNodes.push(item);
                        scope.expandedNodes.push(childrenItem);
                        scope.selectedNodes.push(subItem);
                        console.log(scope.expandedNodes);
                      }
                    })
                  }
                }
              })
            }
          }
        })
      })
    }

    //编辑时判断题型
    var judgeQuestionType = function(data){
      //选择题
      console.log(data.question)
      if(data.typeCode == 'SELECT'){
        var option,answer;
        if(data.questionJson){
          option = data.questionJson.option;
          answer = data.questionJson.answer.split("|");
          data.questionJson.option = [];
        }else{
          option = data.question.option;
          answer = data.question.answer.split("|");
          data.question.option = [];
        }
        // 单选题
        angular.forEach(option,function(item,index){
          var choice ={
            name : item,
            icon : choiceIcon[index],
            _selected:false
          }
          angular.forEach(answer,function(answerIndex){
            if(answerIndex == choice.icon){
              choice._selected = true;
            }
          })
          if(data.questionJson){
            data.questionJson.option.push(choice)
          }else{
            data.question.option.push(choice);
          }
        })
      }

      //填空题
      if(data.typeCode == "FILLBLANK"){
        var answer;
        if(data.questionJson){
          answer = data.questionJson.answer;
          data.questionJson.answer = [];
        }else{
          answer = data.question.answer;
          data.question.answer =[];
        }
        angular.forEach(answer,function(item,index){
          if(data.questionJson){
            data.questionJson.answer.push({index:index+1,name:item});
          }else{
            data.question.answer.push({index:index+1,name:item});
          }
        })
      }

      //组合题
      if(data.typeCode =="COMPLEX"){
        angular.forEach(data.questionJson.children,function(item){
          judgeQuestionType(item);
        })
      }
    }

    // 增加选择题的选项
    scope.addOption = function(option){
      var length;
      if(option){
        length = option.length
      }else{
        option = [];
        length = 0;
      }
      var choice ={
        name : "",
        _selected: false,
        icon : choiceIcon[length]
      }
      option.push(choice);
      scope.formData.questionJson.option = option;
    }

    // 删除选择题选项
    scope.delQestionAnswer = function(item){
      var index = choiceIcon.indexOf(item.icon)
      scope.formData.questionJson.option.splice(index,1);
      angular.forEach(scope.formData.questionJson.option,function(option,i){
        option.icon = choiceIcon[i];
      })
    }

    //填空题添加多个参考答案
    scope.addAnswer = function(item){
      var breakSign = "#$%^&";
      item.name = item.name + breakSign;
    }

    // 题型变化
    scope.changeTypeModule = function(item){
      angular.forEach(scope.questionTypeList,function(itemType){
        itemType._selected = false;
      })
      item._selected = true;
      angular.forEach(scope.questionTypeList,function(item){
        if(item._selected){
          scope.formData.typeCode = item.typeModule;
          scope.formData.questionTypeId = item.typeId;
        }
      })
      initModule();
    }

    // 填空空格选择
    scope.choiceBlanks = function(questionJson){
      var blanks = questionJson.blanks;
      questionJson.answer = [];
      for(var i =1;i<=blanks;i++){
        questionJson.answer.push({index:i});
      }
    }

    //添加子模版
    scope.addChildrenQuestion = function(){
      var length = scope.formData.questionJson.children.length + 1;
      scope.formData.questionJson.children.push({
        number: length,
        typeCode:"QANDA",
      })
      initChildrenModule();
    }

    // 改变子模版
    scope.changeChildrenModule = function(){
      initChildrenModule();
    }
    // 初始化子模版
    var initChildrenModule = function(){
      angular.forEach(scope.formData.questionJson.children,function(item){
        var typeCode = item.typeCode,option = [];
        // 选择题
        if(typeCode == "SELECT"){
          for(var i=0;i<4;i++){
            var choice ={
              icon : choiceIcon[i],
              _selected:false
            }
            option.push(choice);
          }
          console.log(item.question)
          if(item.question.option){
            item.question.option = item.question.option;
          }else{
            item.question={
              option : option
            };
          } 
        }
        // 填空题
        if(typeCode == "FILLBLANK"){
          if(!item.question.blanks){
            item.question ={
              blanks : "2",
              answer : [{index:1},{index:2}]
            }
          }
        }
        // 问答题
        if(typeCode == "QANDA"){
          item.question = item.question || "";
        } 
      })  
    }

    //初始化模板
    var initModule = function(){
      var typeCode = scope.formData.typeCode,option = [];
      $(".edui-body-container").html("<p></p>");
      scope.selectedNodes = [];
      // 选择题
      if(typeCode == "SELECT"){
        for(var i=0;i<4;i++){
          var choice ={
            icon : choiceIcon[i],
            _selected:false
          }
          option.push(choice);
        }
        scope.formData.questionJson = {
          option : option
        };
      }
      if(typeCode == "FILLBLANK"){
        scope.formData.questionJson = {
          blanks:"2",
          answer : [{index:1},{index:2}]
        }
      }
      if(typeCode == "QANDA"){
        scope.formData.questionJson = {};
      }
      if(typeCode == "COMPLEX"){
        scope.formData.questionJson = {
          children : []
        }
        initChildrenModule();
      }
    }

    // 删除子题目
    scope.removeChildrenQues = function(childrenItem){
      var index = childrenItem.number-1;
      scope.formData.questionJson.children.splice(index,1);
      angular.forEach(scope.formData.questionJson.children,function(item,index){
        item.number = index+1;
      })
    }

    // 初始化
    var init = function(){
      // 获取填空题空格数据
      scope.blankList = [];
      for(var i=1; i<=20;i++){
        blankNum = {
          id:i.toString(),
          value:i
        }
        scope.blankList.push(blankNum);
      }
      // 获取题型
      questionTypeDao.invoke('list',{typeSubjectId:subjectId}).then(function(result){
        var list = result.data || [];
        var questionTypeId = "";
        // 编辑题目
        if(questionId){
          // 获取题目
          questionDao.invoke('get/'+questionId,{questionId : questionId}).then(function(res){
            var data = res.data || [];
            questionTypeId = data.questionTypeId || "";
            judgeQuestionType(data);
            scope.formData = data;
            // 选择题型
            angular.forEach(list,function(item){
              if(item.typeId == questionTypeId){
                item._selected = true;
              }else{
                item._selected = false;
              }
            });
            // 获取大纲
            syllabusDao.invoke('listAll',{"syllabusSubjectId":subjectId}).then(function(result){
              var list = result.data || [];
              var questionSyllabus = data.questionSyllabus;
              var syllabusList = []
              angular.forEach(list,function(item){
                if(item.children.length > 0){
                  syllabusList.push(item);
                }
              })
              scope.dataForTheTree = syllabusList;
              choiceSyllabus(list,questionSyllabus);
            });
          });
        }else{//创建题目
          angular.forEach(list,function(item,index){
            if(index == 0){
              item._selected = true;
              scope.formData={
                typeCode:item.typeModule,
                questionTypeId: item.typeId
              }
            }else{
              item._selected = false;
            }
          });
          initModule();
          // 获取大纲
          syllabusDao.invoke('listAll',{"syllabusSubjectId":subjectId}).then(function(result){
            var list = result.data || [];
            var syllabusList = []
            angular.forEach(list,function(item){
              if(item.children.length > 0){
                syllabusList.push(item);
              }
            })
            scope.dataForTheTree = syllabusList;
          },function(){});
        }
        // 获取题型列表
        scope.questionTypeList = list; 
      },function(){});

      // 获取题目来源
      questionProDao.invoke('froms',{}).then(function(res){
        scope.questionFromList = res.data || [];
      })
      //获取题目难度
      questionProDao.invoke('difficults',{}).then(function(res){
        scope.questionDifficultList = res.data || [];
      })
      //获取学科名称
      subjectDao.invoke('list',{}).then(function(res){
        angular.forEach(res.data.list,function(item){
          if(item.subjectId == subjectId){
            scope.subjectName = item.subjectName;
          }
        })
      })
    }

    // 数组去重
    var isRepeat = function(array){
      var arrayObject = {};
      var result = false;
      angular.forEach(array,function(item){
        if(arrayObject[item]){
          result = true;
        }else{
          arrayObject[item] = 1;
        }
      });
      return result;
    };

    //判断数组是否为空
    var isEmpty = function(array){
      if(array.length == 0){
        return true;
      }else{
        for(var i=0;i<array.length;i++){
          if(!array[i]){
            return true;
          }
        }
        return false;
      }  
    }

    //上一题
    var currentId,nextId,beforeId;
    scope.preQues = false;
    scope.nextQues = false;
    scope.preQuestion = function(){
      console.log(questionIdList);
      var index = questionIdList.indexOf(currentId);
      if(currentId){
        if(questionIdList[index-1]){
          if(index == 1){
            scope.preQues = false;
          }else{
            scope.preQues = true;
            scope.nextQues = true;
          }
          currentId = questionIdList[index -1];
        }else{
          scope.preQues = false;
          scope.nextQues = true;
        }
      }else{
        scope.nextQues = true;
        currentId = questionIdList[questionIdList.length -1];
      }
      questionId = currentId;
      getQuestionDetail(currentId);
    }
    //下一题
    scope.nextQuestion = function(){
      var index = questionIdList.indexOf(currentId);
      console.log(questionIdList[index + 1])
      if(questionIdList[index + 1]){
        scope.preQues = true;
        scope.nextQues = true;
        currentId = questionIdList[index + 1];
        questionId = currentId;
        getQuestionDetail(currentId)
      }else{
        scope.preQues = true;
        scope.formData.questionJson = "";
        questionId = "";
        initModule();
      } 
    }

    // 获取上一题下一题的题目
    var getQuestionDetail = function(currentId){
      questionDao.invoke('get/'+ currentId,{id:currentId}).then(function(res){
        var data = res.data || '';
        judgeQuestionType(data);
        scope.formData = data;
        // 选择题型
        angular.forEach(scope.questionTypeList,function(item){
          if(item.typeId == data.questionId){
            item._selected = true;
          }else{
            item._selected = false;
          }
        });
        var questionSyllabus = data.questionSyllabus;
        choiceSyllabus(scope.dataForTheTree,questionSyllabus);
      })
    }

    // 保存题目
    scope.saveQuestion = function(){
      var questionSyllabus= [];
      var formData;
      var validateData = function(){
        // 验证考纲
        var result = false;
        if(isEmpty(scope.selectedNodes)){
          result = true;
          Message.show("请选择考点","warning");
          return result;
        }
        if(!scope.formData.questionFrom){
          result = true;
          Message.show("请选择题目来源","warning");
          return result;
        }
        if(!scope.formData.questionDifficult){
          result = true;
          Message.show("请选择题目的难度","warning");
          return result;
        }
        if(!scope.formData.questionFromSource){
          result = true;
          Message.show("请填写试题的详细来源","warning");
          return result;
        }
      }
      // 大纲集
      angular.forEach(scope.selectedNodes,function(item){
        questionSyllabus.push(item.syllabusId.toString());
      })
      // 选择题
      if(scope.formData.typeCode == "SELECT"){
        var questionJson;
        var answer = [],option = [];
        angular.forEach(scope.formData.questionJson.option,function(item){
          if(item._selected){
            answer.push(item.icon);
          }
          option.push(item.name);
        });
        if(isEmpty(option)){
          Message.show("选项不能为空","warning");
          return;
        }
        if(isRepeat(option)){
          Message.show("选项不能重复","warning");
          return;
        }
        if(isEmpty(answer)){
          Message.show("请选择答案","warning");
          return;
        }
        if(validateData()){
          return;
        }
        scope.formData.questionJson.option = angular.extend([],option);
        scope.formData.questionJson.answer = answer.join("|");
      }

      // 解答题
      if(scope.formData.typeCode == "QANDA"){
        if(validateData()){
          return;
        }
        scope.formData.questionJson.option = [];
      }

      // 填空题
      if(scope.formData.typeCode == "FILLBLANK"){
        scope.formData.questionJson.option = [];
        var answer = [];
        angular.forEach(scope.formData.questionJson.answer,function(item){
          answer.push(item.name);
        });

        if(isEmpty(answer)){
          Message.show("答案不能为空","warning");
          return;
        }
        if(isRepeat(answer)){
          Message.show("答案不能重复","warning");
          return;
        }
        if(validateData()){
          return;
        }
        scope.formData.questionJson.answer = answer;
      }

      //组合题
      if(scope.formData.typeCode == "COMPLEX"){
        var questionJson = scope.formData.questionJson;
        var questionChildren = [];
        for(var i =0;i<questionJson.children.length;i++){
          var childrenItem = questionJson.children[i];
          var answer = [],option = [];
          // 选择题
          if(childrenItem.typeCode == "SELECT"){
            angular.forEach(childrenItem.question.option,function(item){
              if(item._selected){
                answer.push(item.icon);
              }
              option.push(item.name);
            });
            if(isEmpty(option)){
              Message.show("第"+childrenItem.number+"小题的选项不能为空","warning");
              return;
            }
            if(isRepeat(option)){
              Message.show("第"+childrenItem.number+"小题的选项不能重复","warning");
              return;
            }
            if(isEmpty(answer)){
              Message.show("请选择第"+childrenItem.number+"小题的答案","warning");
              return;
            }
            answer = answer.join("|");
          }

          // 解答题
          if(childrenItem.typeCode == "QANDA"){
            option = [];
            answer = childrenItem.question.answer;
            if(!childrenItem.question.answer){
              Message.show("第"+childrenItem.number+"小题的答案不能为空","warning");
              return;
            }
          }

          // 填空题
          if(childrenItem.typeCode == "FILLBLANK"){
            option = [];
            angular.forEach(childrenItem.question.answer,function(item){
              answer.push(item.name);
            });
            if(isEmpty(answer)){
              Message.show("第"+childrenItem.number+"小题的答案不能为空","warning");
              return;
            }
            if(isRepeat(answer)){
              Message.show("第"+childrenItem.number+"小题的答案不能重复","warning");
              return;
            }
          }
          if(validateData()){
            return;
          }
          questionChildren[i]={
            number:childrenItem.number,
            typeCode:childrenItem.typeCode,
            question:{
              title: childrenItem.question.title,
              answer: answer,
              option : option,
              blanks : childrenItem.question.blanks,
              analysis : childrenItem.question.analysis
            }
          }
          console.log(questionChildren[i],childrenItem)
        };
        scope.formData.questionJson = {
          title:questionJson.title,
          children:questionChildren
        }
      }
      
      // 传递的参数
      formData = {
        typeCode: scope.formData.typeCode,
        questionSyllabus: JSON.stringify(questionSyllabus),
        questionDifficult: scope.formData.questionDifficult,
        questionFrom: scope.formData.questionFrom,
        questionFromSource : scope.formData.questionFromSource,
        questionJson: JSON.stringify(scope.formData.questionJson),
        questionId: questionId,
        questionSubjectId: subjectId,
        questionTypeId:scope.formData.questionTypeId
      }

      var msg = !questionId ? "创建成功" : "修改成功";
      questionDao.invoke('save',formData,"post").then(function(res){
        Message.show(msg,'success');
        questionIdList.push(res.data);
        // 去除重复的题目id
        if(isRepeat(questionIdList)){
          var index = questionIdList.indexOf(res.data);
          questionIdList.splice(index,1);
          currentId = "";
        }
        scope.preQues = true;
        scope.nextQues = false;
        initModule();
      });
    }
    init();
  }]
);

$(function(){
  // 页面滚动时
  $(window).scroll(function () {
    if ($('.wrapper-left').length == 0 ) {
      return;
    }
    // 设置或返回当前页面相对于窗口显示区左上角的 Y 位置。
    var scrollTop = (document.all ? document.scrollTop : window.pageYOffset);
    //获得位移高度
    var offset = $('.wrapper-left').offset().top;
    //页面滚动时设置右侧页面滚定在页面上
    if (scrollTop > 331 ) {
      $('.wrapper-left').css({
        position: 'fixed',
        top: 0,
        left: '190px'
      });
    }
    else {
      $('.wrapper-left').css({
        position: 'absolute',
        top: 0,
        left: 0
      });
    }
  });
})
