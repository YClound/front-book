angular.module('awApp').controller("bankCtrl", ["$scope", "$sce", "$state" ,"$stateParams","resource",
  function(scope,sce,state,stateParams,resource) {
    var subjectDao = resource('/subject');
    var questionTypeDao = resource('/questionType');
    var questionDao = resource('/question');
    var syllabusDao = resource('/syllabus');
    var subjectId,typeId;
    scope.sce = sce;
    scope.choiceIcon = ["A","B","C","D","E","F","G","H","I","J","K"];
    scope.treeOptions = {
      nodeChildren: "children",
      dirSelectable: true,
      injectClasses: {
          li: "outline-item",
          liSelected: "a7",
          iExpanded: "outline-menu fa fa-minus",
          iCollapsed: "outline-menu add fa fa-plus",
          iLeaf: "",
          label: "a6",
          labelSelected: "c-blue"
      }
    };

    scope.queryModel = {
      page:1,
      pagesize:5
    };

    // 选择科目
    scope.selectSubject = function(target){
      subjectId = target.subjectId;
      scope.subjectId = subjectId;
      scope.queryModel = {
        page:1,
        pagesize:10,
        questionSubjectId: subjectId
      }
      angular.forEach(scope.subjectList,function(item){
        item._selected = false;
      })
      target._selected = true;
      // 获取题型
      questionTypeDao.invoke('list',{"typeSubjectId":subjectId}).then(function(result){
        scope.questionTypeList = result.data || [];
      },function(){});
      // 获取大纲
      syllabusDao.invoke('listAll',{"syllabusSubjectId":subjectId}).then(function(result){
        scope.dataForTheTree = result.data || [];
      },function(){});
      // 获取题目
      scope.query();
    }

    // 选择题型
    scope.selectQuestionType = function(questionType){
      angular.forEach(scope.questionTypeList,function(item){
        item._selected = false;
      })
      questionType._selected = true;
      typeId = questionType.typeId;
      scope.queryModel.questionTypeId = typeId;
      // 获取题目
      scope.query();
    }

    //选择大纲
    scope.showSelected = function(node){
      var syllabusId = [];
      if(node.children.length > 0){
        angular.forEach(node.children,function(item){
          syllabusId.push(item.syllabusId.toString());
        })
      }else{
        syllabusId.push(node.syllabusId.toString())
      }
      scope.queryModel.questionSyllabus = JSON.stringify(syllabusId);
      // 获取题目
      scope.query();
    }
    // 加载学科
    var init = function(){
      subjectDao.invoke('list',{}).then(function(result){
        var list = result.data.list;
        angular.forEach(list,function(item){
          item._selected = false;
        })
        scope.subjectList = list;
        scope.selectSubject(list[0]);
      },function(){});
    }

    //删除题目
    scope.questionDel = function(item){
      var id = item.question.questionId
      dm.confirm('确认删除?',function(){
        questionDao.invoke('delete/'+id).then(function(){
          Message.show('删除成功','success')
          scope.query();
        },function(){})})
    }

    // 判断题目类型
    var renderModule = function(list){
      angular.forEach(list,function(item){
        if(item.typeCode =="SELECT"){
          item.selectQuestion = item.question.questionContentJson;
        }
        if(item.typeCode =="FILLBLANK"){
          item.fillBankQuestion = item.question.questionContentJson;
        }
        if(item.typeCode =="QANDA"){
          item.qandaQuestion = item.question.questionContentJson;
        }
        if(item.typeCode =="COMPLEX"){
          angular.forEach(item.question.questionContentJson.children,function(children){
            if(children.typeCode=="SELECT"){
              children.selectQuestion = children.question;
            }
            if(children.typeCode=="FILLBLANK"){
              children.fillBankQuestion = children.question;
            }
            if(children.typeCode=="QANDA"){
              children.qandaQuestion = children.question;
            }
          })

          console.log(item.question.questionContentJson.children)
        }
      });
      console.log(list)
      scope.questionList = list;
    }

    // 题目查询
    scope.query = function(){
      questionDao.invoke('list',scope.queryModel).then(function(result){
        var pager = result.data.pager;
        var list = result.data.list || [];
        scope.pagerOption = pager;
        scope.queryModel.page = pager.page;
        scope.questionList = list || [];
        renderModule(list);
      },function(){})
    }

    init();
}]
)

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