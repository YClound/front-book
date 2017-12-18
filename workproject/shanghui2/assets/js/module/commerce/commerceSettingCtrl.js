/*
    管理商会个别信息
*/
angular.module('awApp').controller("commerceSettingCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var sociatyDao = resource('/sociaty');
    var currentUser = scope.currentUser || {};
    var sociatyId = currentUser.sociatyId;
    
    console.log('currentUser',scope.currentUser);
    scope.queryModel = {};
    scope.formData = {
        content:''
    };
    sociatyDao.invoke('get',{sociatyId:sociatyId}).then(function(result){
        scope.formData = result.data;
    });
    // 保存
    scope.save = function () {
        
        sociatyDao.invoke('update', scope.formData).then(function () {

            dm.alert("保存成功",function(){
                state.reload();
            });
            
        });
            
    };
    scope.mySociatyUnion = function(){
        
    };
    // 加入商盟
    scope.bindSociaty = function(item){
        resource('/sociatyUnion').invoke('bind',{socId:item.sociatyId,unionId:''}).then(function(){
            window.Message.show('操作成功','success');
            item._done = true;
        }, function(){});
    };
    scope.addSociatyView = function(){
        scope.queryModel = {};
        scope.searchSociaty();
    };
    scope.searchSociaty = function(){
        var key = scope.queryModel.key || ''
        sociatyDao.invoke('list',{key:key}).then(function(res){
            var list = res.data.list || [];

            scope.sociatyListAll = list;


        }, function(){});
    };
}]);