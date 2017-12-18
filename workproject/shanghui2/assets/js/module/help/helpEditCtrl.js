/* 互帮互助编辑模块 */
angular.module('awApp').controller("helpEditCtrl", ["$scope", "$state", "$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var helpDao = resource('/helpApply');
    var helpId = stateParams.id;
    
    var init = function(){
        helpDao.invoke('get',{helpId:helpId}).then(function(res){
            scope.formData = res.data;
        })
    };
    init();

    
    scope.save = function () {
        var data = {
            helpId : helpId,
            helpContent:scope.formData.helpContent,
            helpPhoto:scope.formData.helpPhoto,
            helpStatus:1
        }
        helpDao.invoke('update', data).then(function () {

            dm.alert("保存成功",function(){
                history.go(-1);
            });
            
        });
            
    };

   
    

}]);