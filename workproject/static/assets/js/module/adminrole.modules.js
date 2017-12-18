/* 管理员角色模块 */
angular.module('awApp', ['angular-core'])
.controller("adminroleCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var adminDao = resource('/adminrole');
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
          ret.push(value.id);
        }
      });
      console.log(ret);
      if(ret.length){
        dm.confirm('确认删除这'+ret.length+'条数据',function(){
          adminDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
            dm.notice('批量删除成功');
            scope.query();
          })
        });
      }
        
    };
    scope.Catlogs = [{id:1,name:'超级管理员'},{id:0,name:'普通管理员'}];
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        adminDao.list(scope.queryModel).then(function(result){
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
            /*adminDao.invoke('del',{id:id}).then(function(){
              alert(0)
            },function(){
              alert(1)
            })*/
            adminDao._delete({id:id}).then(function(){
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
    

}]).controller("adminroleEditCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var adminDao = resource('/adminrole');

    console.log('stateParams',stateParams);

    if (stateParams.id) {
        scope.isNew = false;
        adminDao.get({id:stateParams.id}).then(function(result){
            scope.formData = result.data;
        });
    } else {
        scope.isNew = true;
        scope.formData = {
            role:0
        };
    };

    scope.save = function () {
        
        var method = scope.isNew ? "create" : "update";
        adminDao.invoke(method, scope.formData).then(function (data) {
        	if(data.code !==0){
        		dm.alert(data.message);
        	}else{
        		dm.alert("保存成功",function(){
        			state.go("adminrole.list")
        		});
        	}
            
        });
            
    };
    

}]);


