/*
    会员企业信息、工作经历以及教育经历的编辑和创建
*/
angular.module('awApp').controller("memberMoreCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var user = resource('/user');
    var userCompany = resource('/userCompany');
    var userEducation = resource('/userEducation');
    var userWork = resource('/userWork');
   
    var userId = stateParams.id;
    
    

    console.log('userId',userId);
    
    var init = function() {
        scope.listCompany();
        scope.listEducation();
        scope.listWork();
    }
    /*
        编辑公司
    */
    // 罗列所有
    scope.listCompany = function(){
        userCompany.invoke('listAll',{companyUserId:userId}).then(function(res){
            scope.userCompanyList = res.data.list || [];
        }, function(){});
    };
    // 进入删除视图
    scope.delCompany = function(item){
        dm.confirm('确认删除吗',function(){
            userCompany.invoke('delete',{companyId:item.companyId}).then(function(){
                scope.listCompany();
            }, function(){});
        })
    }
    // 进入添加视图
    scope.addView = function(){
        scope.isNew = true;  
        scope.formModel = {};
    }
    // 进入编辑视图
    scope.editView = function(item){
        scope.isNew = false;
        scope.formModel = angular.extend({}, item);
    };
    // 保存
    scope.saveCompany = function(){
        var action = scope.isNew?'create':'update';
        userCompany.invoke(action,scope.formModel).then(function(){
            scope.listCompany();
        }, function(){});
    };

    /*
        编辑教育经历
    */
    // 罗列所有
    scope.listEducation = function(){
        userEducation.invoke('listAll',{educationUserId:userId}).then(function(res){
            scope.userEducationList = res.data.list || [];
        }, function(){});
    };
    // 进入删除视图
    scope.delEducation = function(item){
        dm.confirm('确认删除吗',function(){
            userEducation.invoke('delete',{educationId:item.educationId}).then(function(){
                scope.listEducation();
            }, function(){});
        })
    }
    // 保存
    scope.saveEducation = function(){
        var action = scope.isNew?'create':'update';
        userEducation.invoke(action,scope.formModel).then(function(){
            scope.listEducation();
        }, function(){});
    };

    /*
        编辑工作经历
    */
    // 罗列所有
    scope.listWork = function(){
        userWork.invoke('listAll',{workUserId:userId}).then(function(res){
            scope.userWorkList = res.data.list || [];
        }, function(){});
    };
    // 进入删除视图
    scope.delWork = function(item){
        dm.confirm('确认删除吗',function(){
            userWork.invoke('delete',{workId:item.workId}).then(function(){
                scope.listWork();
            }, function(){});
        })
    }
    // 保存
    scope.saveWork = function(){
        var action = scope.isNew?'create':'update';
        userWork.invoke(action,scope.formModel).then(function(){
            scope.listWork();
        }, function(){});
    };

    init();
    

    
    

}]);