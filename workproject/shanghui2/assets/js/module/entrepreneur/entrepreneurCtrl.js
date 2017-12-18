/* 
    老乡审核模块 
*/
angular.module('awApp').controller("entrepreneurCtrl", ["$scope", "$state" ,"$stateParams","resource","APPLY_STATUS",function(scope, state,stateParams,resource,APPLY_STATUS) {
    var applyDao = resource('/userApply');
    
    scope.applyStatusList = APPLY_STATUS; 
    scope.queryModel = {
        page:1,
        pagesize:10
    };
    // 带删除的数组
    scope.batchDelArr = [];
    scope.cbAll = false;
    scope.cbAllChange = function(){
      console.log(scope.cbAll);
      var dataList = scope.dataList || [];
      angular.forEach(dataList,function(value,key){
        console.log(value)
        value._selected = scope.cbAll;
      });
    };
    
    scope.batchDel = function(){
      var dataList = scope.dataList || [];
      var ret = [];
      angular.forEach(dataList,function(value,key){
        if(value._selected) {
          ret.push(value.userId);
        }
      });
      console.log(ret);
      if(ret.length){
        dm.confirm('确认删除这'+ret.length+'条数据',function(){
          applyDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
            dm.notice('批量删除成功');
            scope.query();
          })
        });
      }
        
    };
    
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        applyDao.list(scope.queryModel).then(function(result){
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
            applyDao._delete({id:id}).then(function(){
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
    // 审核 action: 'accept' or 'reject'; applyStatus: 0 待审核,1 已通过 ,2 已拒绝
    scope.verify = function(item,action){
        var applyStatus = item.applyStatus;
        applyDao.invoke('verify',{applyId:item.applyId,applyStatus:applyStatus,action:action}).then(function(res){
            if(action === 'accept') {
                item.applyStatus = '1';
            }else {
                item.applyStatus = '2';
            }
        }, function(){});
    }
    scope.search();
    

}]);
