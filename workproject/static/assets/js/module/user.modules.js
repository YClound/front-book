/* 用户模块 */
angular.module('awApp')
.controller("userListCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var userDao = resource('/user');
    console.log(stateParams);
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
          userDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
            dm.notice('批量删除成功');
            scope.query();
          })
        });
      }
        
    };
    scope.Catlogs = [{id:1,name:'超级管理员'},{id:2,name:'普通管理员'}];
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        userDao.list(scope.queryModel).then(function(result){
            var data = result.data;
            var list = data.list || [];
            var pager = data.pager;
            scope.pagerOption = pager;
            scope.dataList = list;
            scope.queryModel.page = pager.page;
           
            $('.table-wrapper').blockLoading('hide');
        });
    
    }
    /*scope.del = function(id){
        dm.confirm('确认删除?',function(){
           userDao.invoke('del',{id:id}).then(function(){
              
            },function(){
              
            });
        });
        
    }*/
    /**
     * 锁定
     */
    scope.lock = function(id,enable){
        dm.confirm('确认锁定会员?',function(){
        	if(enable==1){
        		enable=0;
        	}else{
        		enable=1;
        	}
            userDao.invoke('lock',{userid:id,enable:enable},{method:'get'}).then(function(data){
            	scope.query();
            },function(data){
            	alert("失败");
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

angular.module('awApp').controller("userEditCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var userDao = resource('/user');

    if (stateParams.id) {
        scope.isNew = false;
        userDao.invoke('getuser',{userid:stateParams.id},{method:'get'}).then(function(result){
        	scope.formData = result.data;
        },function(data){
        	dm.alert(data.message);
        });
    } else {
        scope.isNew = true;
        scope.formData = {
            role:0
        };
    };

    scope.save = function () {
        
//        var method = scope.isNew ? "create" : "update";
        userDao.invoke("edit", scope.formData).then(function () {
            dm.alert("保存成功",function(){
                state.go("user.list");
            });
        });
            
    };
    

}]);


