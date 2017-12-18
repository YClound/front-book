/* 
    商会资源文章编辑模块 
*/

angular.module('awApp').controller("commercePaymentEditCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var articleDao = resource('/article');
    var sociatyId = stateParams.id;
    var failCallbacks = function(){}
    var init = function(){
        console.log('state',state)
        scope.formData = {

        };
        articleDao.invoke('get',{sociatyId:sociatyId}).then(function(res){
            var data = res.data || null;
            scope.formData = data;
        }, failCallbacks);   
    };
    init();
    // 保存
    scope.save = function(){
        var formData = {
            sociatyId : sociatyId,
            articleTitle : scope.formData.articleTitle,
            articleContent : scope.formData.articleContent,
        };
        articleDao.invoke('update', formData).then(function (res) {
            scope.formData = angular.extend({}, scope.formData, res.data );
            dm.alert('编辑成功',function(){
                history.go(-1)
            });
            
        });
    };  

}]);