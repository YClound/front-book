angular.module('awApp').controller("questionTypeCtrl", 
  ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var subjectDao = resource('/subject');
    var questionTypeDao = resource('/questionType');
    var typeSubjectId;
    var init = function(){
      subjectDao.list(scope.queryModel).then(function(result){
        var data = result.data;
        var list = data.list || [];
        scope.subjectList = list;
        scope.selectSubject(scope.subjectList[0])
      });
      questionTypeDao.invoke('templetlist',{}).then(function(res){
        scope.templetlist = res.data || [];
      })
    }
    scope.query = function() {
      $('.table-wrapper').blockLoading('absolute');
      questionTypeDao.invoke('list',{typeSubjectId:typeSubjectId}).then(function(res){
        var data = res.data;
        scope.dataList = data;
        $('.table-wrapper').blockLoading('hide');
      })
    }
    scope.selectSubject = function(target){
      typeSubjectId = target.subjectId;
      angular.forEach(scope.subjectList,function(item){
        item._selected = false;
      });
      target._selected = true;
      scope.query();
    };

    // 添加编辑题型
    scope.toEditView = function(item){
      if(item){
        scope.formData = angular.extend({},item);
      }else{
        scope.formData = "";
      }
    }
    // 保存
    scope.save = function () {
      var msg = !scope.formData.typeId ? "创建成功" : "修改成功";
      scope.formData.typeSubjectId = typeSubjectId;
      var formData = {
        typeSubjectId : typeSubjectId,
        typeModule : scope.formData.typeModule,
        typeId:scope.formData.typeId,
        typeName:scope.formData.typeName
      };
      questionTypeDao.invoke("save",formData,"post").then(function () {
        Message.show(msg,'success');
        scope.query();
      });       
    };
    // 删除
    scope.del = function(id){
      dm.confirm('确认删除?',function(){
        questionTypeDao.invoke('delete/'+id).then(function(){
          Message.show('删除成功','success')
          scope.query();
        });
      });
    }
    init();
}])
