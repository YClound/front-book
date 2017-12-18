/* 
    统计文章会员阅读情况模块 
*/
angular.module('awApp').controller("articleMemberCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var articleDao = resource('/article');
    var articleReadDao = resource('/articleRead');
    var sociatyDao = resource('/sociaty');
    var unionId = scope.currentUser.unionId;
    var readActicleId = stateParams.id;
    var init = function(){
        articleDao.invoke('get',{readActicleId:readActicleId}).then(function(res){
            var data = res.data || {};
            scope.activityModel = data;
        });
        sociatyDao.invoke("list",{unionId : unionId}).then(function(res){
            scope.sociatyList = res.data.list || {};
        }, function(){})
    };
    init(); 
    
    scope.queryModel = {
        readActicleId:readActicleId,
        page:1,
        pagesize:10
    };
    
    
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        articleReadDao.list(scope.queryModel).then(function(result){
            var data = result.data;
            var list = data.list || [];
            var pager = data.pager;
            scope.pagerOption = pager;

            scope.dataList = list;
            scope.queryModel.page = pager.page;
           
            $('.table-wrapper').blockLoading('hide');
        });
    
    }
    // 删除
    scope.del = function(id){
        dm.confirm('确认删除?',function(){
            articleReadDao._delete({readActicleId:id}).then(function(){
                window.Message.show('删除成功','success')
                scope.query();
            });
        });
        
    }
    scope.search = function(){
        scope.batchDelArr = [];
        scope.cbAll = false;
        scope.queryModel.page = 1;
        scope.query();
    }
    scope.clear = function(){
        scope.queryModel = {
            readActicleId:readActicleId,
            page:1,
            pagesize:10
        };
    }
    
    
    scope.search();
    

}]);
