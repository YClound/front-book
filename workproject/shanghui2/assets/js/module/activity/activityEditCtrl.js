/* 
    活动编辑模块 
*/

angular.module('awApp').controller("activityEditCtrl", ["$scope", "$state", "$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var activityDao = resource('/activitySetting');
    var activityPersonDao = resource('/activityPerson');

    var memberDao = resource('/user');
    var activityId = stateParams.id;
    var failCallbacks = function(){}
    var init = function(){
        scope.formData = {};
        scope.isNew = true;
        // 权限
        scope.registerAuth = {id:0,text:'公开'};
        scope.readAuth = {id:0,text:'公开'};
        scope.activityAuthList = [{id:1,text:'同乡'},{id:2,text:'会员'},{id:3,text:'服务组'},{id:4,text:'到期会员'}];
        scope.activityRegisterAuth = $.extend(true, {}, scope.activityAuthList);
        scope.activityReadAuth = $.extend(true, {}, scope.activityAuthList);
        if(activityId) {
            scope.isNew = false;
            // 获取活动信息
            activityDao.invoke('get',{activityId:activityId}).then(function(res){
                var data = res.data || null;
                var _activityRegisterAuth = data.activityRegisterAuth ? data.activityRegisterAuth.split(',') : "";
                var _activityReadAuth = data.activityReadAuth ? data.activityReadAuth.split(',') : "";
                
                if(_activityReadAuth == "0"){
                    scope.readAuth._selected = true;
                }else{
                    scope.readAuth._selected = false;
                    angular.forEach(scope.activityReadAuth,function(value,key){
                        if(_activityReadAuth.indexOf(value.id+'')>-1){
                            value._selected = true;
                        }
                    });
                }

                if(_activityRegisterAuth == "0"){
                    scope.registerAuth._selected = true;
                }else{
                    scope.registerAuth._selected = false;
                    angular.forEach(scope.activityRegisterAuth,function(value,key){
                        if(_activityRegisterAuth.indexOf(value.id+'')>-1){
                            value._selected = true;
                        }
                    });
                }
                scope.activityRegisterAuthSync();
                scope.activityReadAuthSync();
                
                scope.formData = data;
            }, failCallbacks);
            // 获取活动报名人员
            activityPersonDao.invoke('list',{activityId:activityId,pagesize:20}).then(function(res){
                scope.activityMemberList = res.data.list;

            }, failCallbacks)
        }
    };
    init();

    
    
    console.log('stateParams',stateParams);
    scope.activityReadAuthSync = function(){
        angular.forEach(scope.activityReadAuth,function(value,key){
            if(value._selected){
                scope.readAuth._selected = false;
            }
        });
    };
    scope.activityRegisterAuthSync = function(){
        angular.forEach(scope.activityRegisterAuth,function(value,key){
            if(value._selected){
                scope.registerAuth._selected = false;
            }
        });
    };
    scope.activityReadAuthChange = function(target){
        var _selected = target._selected;
        if(_selected){
            angular.forEach(scope.activityReadAuth,function(value,key){
                value._selected = false;
            });
        }
    };
    scope.activityRegisterAuthChange = function(target){
        var _selected = target._selected;
        if(_selected){
            angular.forEach(scope.activityRegisterAuth,function(value,key){
                value._selected = false;
            });
        }
    };
    // 存为草稿
    scope.saveAsDraft = function(){
        scope.save();
        var formData = scope.formData;
        var method = scope.isNew ? "create" : "update";
        formData.activityStatus = 0;
        activityDao.invoke(method, formData).then(function (res) {
            scope.isNew = false;
            scope.formData = angular.extend({}, scope.formData, res.data );
            
            dm.alert('保存草稿成功',function(){
                history.go(-1)
            });
            
        });
    };
    // 保存并发布
    scope.saveAndPublish = function(){
        scope.save();
        var formData = scope.formData;
        var method = scope.isNew ? "create" : "update";
        formData.activityStatus = 1;
        activityDao.invoke(method, formData).then(function (res) {
            scope.isNew = false;
            scope.formData = angular.extend({}, scope.formData, res.data );
            dm.alert('发布成功',function(){
                history.go(-1)
            });
            
        });
    };
    // 保存并预览
    scope.saveAndPreview = function(){
        $(".activity-detail").show();
        $('#container').html(scope.formData.activityContent);
    };
    // 点击关闭预览
    scope.closeArticle = function(){
        $(".main-content").find('.activity-detail').hide();
    }
    scope.save = function () {
        var selected1 = [],selected2 = [],userIds = [];
        // 报名权限
        angular.forEach(scope.activityRegisterAuth,function(value,key){
            if(value._selected){
                selected1.push(value.id);
            }
        });
        // 查看权限
        angular.forEach(scope.activityReadAuth,function(value,key){
            if(value._selected){
                selected2.push(value.id);
            }
        });
        // 新增的报名人
        angular.forEach(scope.activityMemberList,function(value,key){
            if(!value._delete && value.personId){
                userIds.push(value.userId);
            }
        });
        if(scope.readAuth._selected){
            scope.formData.activityReadAuth = 0;
        }else{
            scope.formData.activityReadAuth = selected2.join(',');
        }

        if(scope.registerAuth._selected){
            scope.formData.activityRegisterAuth = 0;
        }else{
            scope.formData.activityRegisterAuth = selected1.join(',');
        }
        
        scope.formData.userIds = userIds.join(',');
            
    };



    scope.queryModel = {
        page:1
    };
    // 查询会员
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
    // 进入选择会员
    scope.addMemberView = function(){
        scope.search();
    };
    // 删除报名人员
    scope.removeMember = function(item){
        var personId = item.personId;
        if(personId) {
            // 删除
            activityPersonDao.invoke('delete',{personId:personId}).then(function(res){
                item._delete = true;
                window.Message.show('删除成功','success')

            }, failCallbacks)
        }else {
            item._delete = true;
        }
    };
    // 指定会员
    scope.addMember = function(item){
        console.log('指定',item);
        var userId = item.userId;
        activityPersonDao.invoke('create',{activityId:activityId,personUserId:userId}).then(function(res){
            item._delete = true;
            window.Message.show('添加成功','success')
            // 获取活动报名人员
            activityPersonDao.invoke('list',{activityId:activityId,pagesize:20}).then(function(res){
                scope.activityMemberList = res.data.list;

            }, failCallbacks);

        }, failCallbacks)
    }
    

}]);