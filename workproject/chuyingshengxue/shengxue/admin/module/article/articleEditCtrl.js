/* 
    文章编辑模块 
*/

angular.module('awApp').controller("articleEditCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var articleDao = resource('/article');
    var articleTypeDao = resource('/articleType');
    var sociatyId = scope.currentUser.sociatyId;
    var ID = stateParams.id;
    var failCallbacks = function(){}
    var init = function(){
        console.log('stateParams',stateParams);
        scope.formData = {

        };
        scope.isNew = true;
        
        // 获取文章类别
        /*articleTypeDao.invoke('list',{sociatyId:sociatyId,pagesize:100}).then(function(res){
            scope.articleTypeList = res.data.list;

        }, failCallbacks);*/

        if(ID) {
            scope.isNew = false;
            // 获取活动信息
            articleDao.invoke('get/'+ID).then(function(res){
                var data = res.data || null;
               
                
                
                scope.formData = data;
                console.log(data)
                

            }, failCallbacks);
            
        }else {

        }
    };
    init();

  
    // 保存并发布
    scope.saveAndPublish = function(){
        
        var formData = scope.formData;
        var method = scope.isNew ? "save" : "save";
        var msg = scope.isNew ? "创建成功" : "修改成功";
        formData.articleStatus = 1;
        articleDao.invoke(method, formData).then(function (res) {
            scope.isNew = false;
            scope.formData = angular.extend({}, scope.formData, res.data );
            dm.alert(msg,function(){
                //history.go(-1)
            });
            
        });
    };
    


    

}]);