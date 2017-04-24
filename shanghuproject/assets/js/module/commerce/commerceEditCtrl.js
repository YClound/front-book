/*
    商会编辑和创建
*/
angular.module('awApp').controller("commerceEditCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var sociatyDao = resource('/sociaty');
    var sociatyPositionDao = resource('/sociatyPosition');
    var sociatyGroupDao = resource('/sociatyGroup');
    var ID = stateParams.id;
    

    
    console.log('stateParams 11',stateParams);
    function _update(key,target,list){
        var keyArr = key.split(',');
        var key1 = keyArr[0],key2 = keyArr[1] || -1;
        var ret = false;
        var isBreak = false;
        for (var i = list.length - 1; i >= 0; i--) {
            var item = list[i];
            for(var j = 0;j<keyArr.length; j++ ){
                var k = keyArr[j];
                if(item[k] && (item[k] == target[k])){
                    isBreak = true;
                    list[i] = angular.extend({},list[i],target);
                    break;
                }
            }
            if(isBreak){
                break;
            }
        }
    }
    function _delete(key,target,list){
        var keyArr = key.split(',');
        var key1 = keyArr[0],key2 = keyArr[1] || -1;
        var ret = false;
        var isBreak = false;
        for (var i = list.length - 1; i >= 0; i--) {
            var item = list[i];
            for(var j = 0;j<keyArr.length; j++ ){
                var k = keyArr[j];
                if(item[k] && (item[k] == target[k])){
                    isBreak = true;
                    list.splice(i,1);
                    break;
                }
            }
            if(isBreak){
                break;
            }
        }
    }
    scope.groupList = [];
    scope.positionList = [];
    if (ID) {
        scope.isNew = false;
        sociatyDao.get({sociatyId:ID}).then(function(result){
            scope.formData = result.data;
            //scope.formData.sociatyLogo = 'http://www.chuyingfund.com/files/account/headImg/120/2016/20160612131503913_1280x960.jpg';
            //获取职位
            sociatyPositionDao.invoke('listAll',{sociatyId:ID}).then(function(res){
                scope.positionList = res.data.list || [];
            }, function(){});
            // 获取分组
            sociatyGroupDao.invoke('listAll',{sociatyId:ID}).then(function(res){
                scope.groupList = res.data.list || [];
            }, function(){});
        });
    } else {
        scope.isNew = true;
        scope.formData = {
            
        };
        scope.positionList = [];
        scope.groupList = [];

    };

    /*
        编辑分组
    */
    // 进入添加视图
    scope.addGroupView = function(){
        scope.isNewGroup = true;
        scope.groupModel = {
            _id:new Date().valueOf()
        };    
    }
    // 进入编辑视图
    scope.editGroupView = function(item){
        scope.isNewGroup = false;
        scope.groupModel = angular.extend({}, item);  
    };
    // 添加分组
    scope.addGroup = function(){
        scope.groupList.push(scope.groupModel);
    };
    scope.updateGroup = function(){
        if(scope.groupModel.groupId){
            sociatyGroupDao.invoke('update',scope.groupModel).then(function(){
                _update('groupId,_id',scope.groupModel,scope.groupList);
            }, function(){});
        }else {
            _update('groupId,_id',scope.groupModel,scope.groupList);
        }
            
    };
    scope.saveGroup = function(){
        
        if(scope.isNewGroup){
            scope.addGroup();
        }else {
            scope.updateGroup();
        }
    };
    scope.delGroup = function(item){
        if(item.groupId) {
            dm.confirm('确认删除?',function(){
              sociatyGroupDao.invoke('delete',{groupId:item.groupId}).then(function(res){
                _delete('groupId,_id',item,scope.groupList);
              })
            });
        }else {
            _delete('groupId,_id',item,scope.groupList);
        }
    };


    /*
        编辑职位
    */
    // 进入添加视图
    scope.addPositionView = function(){
        scope.isNewPosition = true;
        scope.positionModel = {
            _id:new Date().valueOf()
        };    
    }
    // 进入编辑视图
    scope.editPositionView = function(item){
        scope.isNewPosition = false;
        scope.positionModel = angular.extend({}, item);  
    };
    // 添加分组
    scope.addPosition = function(){
        scope.positionList.push(scope.positionModel);
    };
    scope.updatePosition = function(){
        if(scope.positionModel.positionId){
            sociatyPositionDao.invoke('update',scope.positionModel).then(function(){
                _update('positionId,_id',scope.positionModel,scope.positionList);
            }, function(){});
        }else {
            _update('positionId,_id',scope.positionModel,scope.positionList);
        }
            
    };
    scope.savePosition = function(){
        
        if(scope.isNewPosition){
            scope.addPosition();
        }else {
            scope.updatePosition();
            
        }
    };
    scope.delPosition = function(item){
        console.log(item)
        if(item.positionId) {
            dm.confirm('确认删除?',function(){
              sociatyPositionDao.invoke('delete',{positionId:item.positionId}).then(function(){
                _delete('positionId,_id',item,scope.positionList);
              })
            });
        }else {
            _delete('positionId,_id',item,scope.positionList);
        }
    };





    // 保存商会信息
    scope.save = function () {
        var _groupList = [],_positionList = [];
        angular.forEach(scope.groupList,function(value,key){
            if(value._id){
                delete value.$$hashKey;
                _groupList.push(value)
            }
        });
        angular.forEach(scope.positionList,function(value,key){
            if(value._id){
                delete value.$$hashKey;
                _positionList.push(value)
            }
        });
        scope.formData.groupList = JSON.stringify(_groupList);
        scope.formData.positionList = JSON.stringify(_positionList);
        var method = scope.isNew ? "create" : "update";
        sociatyDao.invoke(method, scope.formData).then(function () {

            dm.alert("保存成功",function(){
                state.go("commerce.list")
            });
            
        });
            
    };
    

}]);