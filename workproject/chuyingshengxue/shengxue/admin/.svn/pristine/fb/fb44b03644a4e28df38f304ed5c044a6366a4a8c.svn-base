/* 
    系统帐号列表 
*/
angular.module('awApp').controller("articleListCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var Dao = resource('/admin');
    scope.queryModel = {
        page:1,
        pagesize:20
    };
    
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        Dao.list(scope.queryModel).then(function(result){
            var data = result.data;
            var list = data.list || [];
            var pager = data.pager;
            scope.pagerOption = pager;
            scope.dataList = list;
            scope.queryModel.page = pager.page;
           
            $('.table-wrapper').blockLoading('hide');
        });
    
    }
    scope.del = function(id){
        dm.confirm('确认删除?',function(){
            Dao.invoke('delete/'+id).then(function(){
                Message.show('删除成功','success')
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
            page:1,
            pagesize:10
        };
    }
    
    scope.search();

}]);

