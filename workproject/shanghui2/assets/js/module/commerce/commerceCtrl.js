/* 
    商会设置 
*/
angular.module('awApp').controller("commerceCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var commerceDao = resource('/sociaty');

    
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
          commerceDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
            dm.notice('批量删除成功');
            scope.query();
          })
        });
      }
        
    };
    
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        commerceDao.list(scope.queryModel).then(function(result){
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
            /*commerceDao.invoke('del',{id:id}).then(function(){
              alert(0)
            },function(){
              alert(1)
            })*/
            commerceDao._delete({sociatyId:id}).then(function(){
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
    // 快速入口
    var hrefDao = resource('/sociatyHref');
    
    scope.saveHref = function(item){
      if(!item.hrefAddress){
        window.Message.show('请填写连接地址','warning')
        return;
      }
      hrefDao.invoke('update',item).then(function(){
        window.Message.show('保存成功','success')
      }, function(){});
    }
    scope.hrefView = function(item){
      var hrefSociatyId = item.sociatyId;

      hrefDao.invoke('listAll',{hrefSociatyId:hrefSociatyId}).then(function(res){
        console.log(111,res.data.list)
          scope.hrefList = res.data.list || [];


      }, function(){});
    }
    scope.search();
    

}]);
