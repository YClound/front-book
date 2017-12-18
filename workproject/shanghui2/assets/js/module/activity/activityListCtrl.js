/* 
    活动列表模块 
*/
angular.module('awApp').controller("activityListCtrl", ["$scope", "$state" ,"$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var activityDao = resource('/activitySetting');
    
    var init = function(){
        
        
    };
    init(); 
    
    scope.queryModel = {
        page:1,
        pagesize:10
    };
    
    
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        activityDao.list(scope.queryModel).then(function(result){
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
            activityDao._delete({id:id}).then(function(){
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
            page:1,
            pagesize:10
        };
    }
    // 发布 0 草稿 1 发布中 2 过期
    scope.activityPublish = function(item){
        var activityStatus,msg = '',action = true;
        // 准备发布
        if(item.activityStatus==0){
            activityStatus = 1;
            msg = '发布成功';
        }else if (item.activityStatus==1){
            activityStatus = 0;
            msg = '取消发布成功'
        }else {
            action = false;
        }
        
        action && activityDao.invoke('publish',{activityStatus:activityStatus}).then(function(){
            window.Message.show(msg,'success')
            scope.query();
        });
        
        
    }
    scope.search();
    

}]);
