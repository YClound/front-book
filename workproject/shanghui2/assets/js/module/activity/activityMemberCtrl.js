/* 
    活动成员模块 
*/
angular.module('awApp').controller("activityMemberCtrl", ["$scope", "$state" ,"$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var activityDao = resource('/activitySetting');
    var activityPersonDao = resource('/activityPerson');
    var activityId = stateParams.id;
    var init = function(){
        activityDao.invoke('get',{activityId:activityId}).then(function(res){
            var data = res.data || {};
            scope.activityModel = data;
        });
        
    };
    init(); 
    
    scope.queryModel = {
        activityId:activityId,
        page:1,
        pagesize:10
    };
    
    
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        activityPersonDao.list(scope.queryModel).then(function(result){
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
            activityPersonDao._delete({id:id}).then(function(){
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
            activityId:activityId,
            page:1,
            pagesize:10
        };
    }
    /* personAuditStatus 0初始值 1同意 2拒绝 */
    scope.agreeRegister = function(item){
        var personAuditStatus = item.personAuditStatus;
        activityPersonDao.invoke('update',{personAuditStatus:1}).then(function(){
            window.Message.show('已同意','success');
            item.personAuditStatus = 1;
        })
    }
    scope.rejectRegister = function(item){
        var personAuditStatus = item.personAuditStatus;
        activityPersonDao.invoke('update',{personAuditStatus:1}).then(function(){
            window.Message.show('已拒绝','success');
            item.personAuditStatus = 2;
        })
    }
    
    scope.search();
    

}]);
