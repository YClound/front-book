/* 
    商联会员列表模块 
*/
angular.module('awApp').controller("unionMemberCtrl", ["$scope", "$state" ,"$stateParams","resource","MEMBER_TYPE","$rootScope",function(scope, state,stateParams,resource,MEMBER_TYPE,rootScope) {
    var memberDao = resource('/user');
    var sociatyDao = resource('/sociaty');
    var unionId = rootScope.currentUser.unionId;
    var failCallbacks = function(){};
   
    var init = function(){

        scope.provinceList = rootScope.cityDataFn();
        sociatyDao.invoke('list',{unionId:unionId}).then(function(res){
            var list = res.data.list || [];
            scope.sociatyList = list;

        }, failCallbacks);
        
    };
    init(); 
    // 省份变化获取新的城市
    scope.provinceChange = function(){
        var key = scope.queryModel.province || '';
        scope.cityList = rootScope.cityDataFn(key);
    };
    // 城市变化获取新的地区
    scope.cityChange = function(){
        var key = scope.queryModel.city || '';
        scope.districtList = rootScope.cityDataFn(key);
        
    };
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
          memberDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
            dm.notice('批量删除成功');
            scope.query();
            scope.cbAll = false;
          })
        });
      }
        
    };
    
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        memberDao.list(scope.queryModel).then(function(result){
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
            /*memberDao.invoke('del',{id:id}).then(function(){
              alert(0)
            },function(){
              alert(1)
            })*/
            memberDao._delete({id:id}).then(function(){
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
