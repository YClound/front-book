/*
    设置会员
*/
angular.module('awApp').controller("entrepreneurDetailCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var memberDao = resource('/user');
    var sociaty_position_dao = resource('/sociatyPosition');
    var sociaty_group_dao = resource('/sociatyGroup');
    var applyDao = resource('/userApply');
    var requestCount = 0;
    var init = function(){
        sociaty_position_dao.invoke('listAll',{}).then(function(res){
            var list = res.data.list || [];

            scope.sociaty_position_list = list;
            next()

        }, function(){});
        sociaty_group_dao.invoke('listAll',{}).then(function(res){
            var list = res.data.list || [];

            scope.sociaty_group_list = list;
            next()
        }, function(){});

        applyDao.invoke('get',{applyId:stateParams.id}).then(function(res){
            scope.applyModel = res.data;
            next();

        }, function(){});
    };
    init();

    var next = function(){
        requestCount++;
        if(requestCount>=3) {
                var userId = scope.applyModel.applyUserId;
                memberDao.get({userId:userId}).then(function(result){
                    scope.formData = result.data;
                    var groupIds = scope.formData.groupIds || '';
                    var selected = groupIds.split(',');
                    angular.forEach(scope.sociaty_group_list,function(value,key){
                        if(selected.indexOf(value.groupId)>-1){
                            value._selected = true;
                        }
                    });
                });
            
        }
    };
    console.log('stateParams',stateParams);

    scope.queryModel = {
        page:1
    };
    // 查询介绍人
    scope.query = function() {
        memberDao.list(scope.queryModel).then(function(result){
            var data = result.data;
            var list = data.list || [];
            var pager = data.pager;
            scope.pagerOption = pager;
            scope.dataList = list;
            scope.queryModel.page = pager.page;
        });
    
    }
    scope.search = function(){
        scope.queryModel.page = 1;
        scope.query();
    }
    // 保存会员设置
    scope.saveSetting = function () {
        var selected = [];
        angular.forEach(scope.sociaty_group_list,function(value,key){
            if(value._selected){
                selected.push(value.groupId);
            }
        });
        memberDao.invoke('update', scope.formData).then(function () {
            window.Message.show('保存成功','success');
        });
            
    };
    // 审核会员
    scope.saveApply = function(action){
        var applyModel  = scope.applyModel;
        var applyStatus = '';
        console.log(111,action)
        if('yes' === action) {
            applyStatus = '1'
        }else {
            applyStatus = '2';
        }

        applyDao.invoke('verify',{applyId:applyModel.applyId,applyStatus:applyStatus}).then(function(){
            window.Message.show('设置成功','success');
            scope.applyModel.applyStatus = applyStatus;
        }, function(){

        })
    };
    // 选择介绍人
    scope.inviterView = function(){
        scope.search();
    };
    // 指定介绍人
    scope.setInviter = function(item){
        console.log('指定介绍人',item.userId,item);
        scope.inviterId = item.userId;
        scope.formData.inviter = angular.extend({}, item);
        
    }
    

}]);