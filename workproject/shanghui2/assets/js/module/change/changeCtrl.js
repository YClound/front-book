/* 
    信息审核列表模块 
*/
angular.module('awApp').controller("changeCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var applyDao = resource('/infoApply');
    
    scope.queryModel = {
        page:1,
        pagesize:10
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

/* 
    个人信息审核 
*/
angular.module('awApp').controller("profileCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var applyDao = resource('/infoApply');
    var changeDao = resource('/userRefresh');
    var applyId = stateParams.applyId;
    var refreshId = stateParams.id;
    
    // 审核表
    applyDao.get({applyId:applyId}).then(function(result){
        scope.applyModel = result.data;
    });
    changeDao.get({refreshId:refreshId}).then(function(result){
        scope.formData = result.data;
    });

    scope.verify = function(action){
        var applyStatus = '';
        if('yes' === action) {
            applyStatus = 1;
        }else {
            applyStatus = 2;
        }
        applyDao.invoke('update',{applyId:applyId,applyStatus:applyStatus}).then(function(){
            window.Message.show('操作成功','success');
            history.go(-1);
        }, function(){

        });
    }

}]);

/* 
    公司信息审核 
*/
angular.module('awApp').controller("companyCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var applyDao = resource('/infoApply');
    var changeDao = resource('/userCompanyRefresh');
    var applyId = stateParams.applyId;
    var refreshId = stateParams.id;
    
    // 审核表
    applyDao.get({applyId:applyId}).then(function(result){
        scope.applyModel = result.data;
    });
    changeDao.get({refreshId:refreshId}).then(function(result){
        scope.formData = result.data;
    });

    scope.verify = function(action){
        var applyStatus = '';
        if('yes' === action) {
            applyStatus = 1;
        }else {
            applyStatus = 2;
        }
        applyDao.invoke('update',{applyId:applyId,applyStatus:applyStatus}).then(function(){
            window.Message.show('操作成功','success');
            history.go(-1);
        }, function(){

        });
    }

}]);
