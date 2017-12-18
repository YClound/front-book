
var all_config = {
        initialFrameWidth:800,
        initialFrameHeight:300,
        toolbar:[
            'source | undo redo | bold italic underline strikethrough | forecolor backcolor | removeformat |',
            'insertorderedlist insertunorderedlist | cleardoc  | fontfamily fontsize' ,
            '| justifyleft justifycenter justifyright justifyjustify |',
            'link unlink |  image  ',
            '| horizontal print preview  fullscreen'
        ]
    };
/*
    商会简介
*/
angular.module('awApp').controller("commerceIntroCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var sociatyDao = resource('/sociaty');
    var currentUser = scope.currentUser || {};
    var sociatyId = currentUser.sociatyId;
    
    console.log('currentUser',scope.currentUser);
    scope.all_config = all_config;
    scope.formData = {
        content:''
    };
    sociatyDao.invoke('intro',{sociatyId:sociatyId}).then(function(result){
        scope.formData = result.data;
    });
    // 保存
    scope.save = function () {
        
        sociatyDao.invoke('intro', scope.formData).then(function () {

            dm.alert("保存成功",function(){
                state.reload();
            });
            
        });
            
    }; 
}]);

/* 商会章程 */
angular.module('awApp').controller("commerceRulesCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var sociatyDao = resource('/sociaty');
    var currentUser = scope.currentUser || {};
    var sociatyId = currentUser.sociatyId;
    
    console.log('currentUser',scope.currentUser);
    scope.all_config = all_config;
    scope.formData = {
        content:''
    };
    sociatyDao.invoke('rules',{sociatyId:sociatyId}).then(function(result){
        scope.formData = result.data;
    });
    // 保存
    scope.save = function () {
        
        sociatyDao.invoke('rules', scope.formData).then(function () {

            dm.alert("保存成功",function(){
                state.reload();
            });
            
        });
            
    }; 
}]);

/* 商会组织架构 */
angular.module('awApp').controller("commerceOrganizationCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var sociatyDao = resource('/sociaty');
    var currentUser = scope.currentUser || {};
    var sociatyId = currentUser.sociatyId;
    
    console.log('currentUser',scope.currentUser);
    scope.all_config = all_config;
    scope.formData = {
        content:''
    };
    sociatyDao.invoke('rules',{sociatyId:sociatyId}).then(function(result){
        scope.formData = result.data;
    });
    // 保存
    scope.save = function () {
        
        sociatyDao.invoke('rules', scope.formData).then(function () {

            dm.alert("保存成功",function(){
                state.reload();
            });
            
        });
            
    }; 
}]);