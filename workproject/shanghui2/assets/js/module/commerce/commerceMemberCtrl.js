/* 
    会员列表模块 
*/
angular.module('awApp').controller("commerceMemberCtrl", ["$scope", "$state" ,"$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var memberDao = resource('/user');
    var sociaty_position_dao = resource('/sociatyPosition');
    var sociaty_group_dao = resource('/sociatyGroup');
    var sociatyId = stateParams.id;
    console.log('sociatyId',sociatyId)
    var init = function(){
        sociaty_position_dao.invoke('listAll',{}).then(function(res){
            var list = res.data.list || [];

            scope.sociaty_position_list = list;

        }, function(){});
        sociaty_group_dao.invoke('listAll',{}).then(function(res){
            var list = res.data.list || [];

            scope.sociaty_group_list = list;

        }, function(){});
    };
    init(); 
    scope.memberTypeList = MEMBER_TYPE;
    scope.queryModel = {
        sociatyId:sociatyId,
        page:1,
        pagesize:10
    };
    // 带删除的数组
    scope.batchDelArr = [];
    scope.cbAll = false;
    scope.cbAllChange = function(){
      //console.log(scope.cbAll);
      var dataList = scope.dataList || [];
      angular.forEach(dataList,function(value,key){
        //console.log(value)
        value._selected = scope.cbAll;
      });
    };
    scope.batchImport = function(){
      var dataList = scope.dataList || [];
      var ret = [];
      angular.forEach(dataList,function(value,key){
        if(value._selected) {
          ret.push(value.userId);
        }
      });
      //console.log(ret);
      if(ret.length){
        var link = 'http://www.baidu.com/?ids='+ret.join(',');
        dm.alert('<a href="'+link+'" target="_blank">点击此处下载</a>');
      }else {
        dm.alert('请选择要导出的会员');
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
