/* 
    互帮互助模块 
*/
angular.module('awApp').controller("helpListCtrl", ["$scope", "$state" ,"$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var helpDao = resource('/helpApply');
    var sociatyId = scope.currentUser.sociatyId;
    
    scope.queryModel = {
        sociatyId:sociatyId,
        page:1,
        pagesize:10
    };
   
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        helpDao.list(scope.queryModel).then(function(result){
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
            helpDao._delete({id:id}).then(function(){
                window.Message.show('删除成功','success')
                scope.query();
            });
        }); 
    }
    scope.search = function(){
        scope.queryModel.page = 1;
        scope.query();
    }
    scope.clear = function(){
        scope.queryModel = {
            sociatyId:sociatyId,
            page:1,
            pagesize:10
        }
    }
    
    /* helpStatus 0初始值 1同意 2拒绝 */
    scope.agreeHelp = function(item){
        var helpId = item.helpId;
        helpDao.invoke('update',{helpId:helpId,helpStatus:1}).then(function(){
            window.Message.show('已同意','success');
            item.helpStatus = 1;
        })
    }
    scope.rejectHelp = function(item){
        var helpId = item.helpId;
        helpDao.invoke('update',{helpId:helpId,helpStatus:2}).then(function(){
            window.Message.show('已拒绝','success');
            item.helpStatus = 2;
        })
    }
    scope.deleteHelp = function(id){
        helpDao.invoke('delete',{helpId:id}).then(function(){
            window.Message.show('删除成功','success');
            scope.query();
        })
    } 
    scope.search(); 
}]);
