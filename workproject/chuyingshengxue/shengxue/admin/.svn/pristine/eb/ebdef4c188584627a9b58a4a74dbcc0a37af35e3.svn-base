/* 
    学科模块 
*/
angular.module('awApp').controller("subjectCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var Dao = resource('/subject');
    scope.queryModel = {
        page:1,
        pagesize:10
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
            Dao.invoke('delete',{subjectId:id}).then(function(){
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
    scope.toEditView = function(item){
       
        if(item){
            scope.formData = angular.extend( {}, item);
        }else {
            scope.formData = {};
        }
        
        
    }
    scope.save = function () {
        
        var method = !scope.formData.subjectId ? "create" : "update";
        var msg = !scope.formData.subjectId ? "创建成功" : "修改成功";
        Dao.invoke(method, scope.formData).then(function () {
            Message.show(msg,'success');
            scope.search();
        });
            
    };
    scope.search();
    

}]);
