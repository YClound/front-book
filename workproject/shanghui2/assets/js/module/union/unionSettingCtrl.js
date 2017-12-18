/*
    管理商联信息
*/
angular.module('awApp').controller("unionSettingCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var sociatyDao = resource('/sociaty');
    var sociatyUnionDao = resource('/sociatyUnion');
    var unionFrameDao = resource('/unionFrame');
    var currentUser = scope.currentUser || {};
    var sociatyId = currentUser.sociatyId;
    var failCallbacks = function(){};
    
    console.log('currentUser',scope.currentUser);
    scope.queryModel = {};
    scope.formData = {
        content:''
    };
    
    // 获取商联信息 以及关联的商会和组织架构
    var init = function(){
        sociatyUnionDao.invoke('get',{sociatyId:sociatyId}).then(function(result){
            scope.formData = result.data;
            var unionId = scope.unionId = scope.formData.unionId || '';
            unionId && scope.mySociatyUnion();
            unionId && scope.myUnionFrame();
        });
        
    };
        
    // 保存
    scope.save = function () {
        
        sociatyUnionDao.invoke('update', scope.formData).then(function () {
            window.Message.show('保存成功','success');
        });
            
    };
    scope.updateUnionFrameView = function(item){
        scope.unionFrame = angular.extend({},item);
    };
    scope.saveUnionFrame = function(item){
        var formData = scope.unionFrame;
        var action = formData.frameId?'update':'create';
        unionFrameDao.invoke(action,formData).then(function(res){
            window.Message.show('保存成功','success');
            scope.myUnionFrame();
        }, failCallbacks);
    };
    // 删除商联组织架构
    scope.delUnionFrame = function(item){
        var frameId = item.frameId;
        dm.confirm('确认删除吗',function(){
            unionFrameDao.invoke('delete',{frameId:frameId}).then(function(res){
                window.Message.show('删除成功','success');
                item._delete = true;
            }, failCallbacks);
        });
            
    };
    // 获取商联组织架构
    scope.myUnionFrame = function(){
        var unionId = scope.unionId;
        unionFrameDao.invoke('list',{unionId:unionId}).then(function(res){
            scope.unionFrameList = res.data.list || [];

        }, failCallbacks);
    };
    // 获取商联商会
    scope.mySociatyUnion = function(){
        var unionId = scope.unionId;
        sociatyUnionDao.invoke('list',{unionId:unionId}).then(function(res){
            scope.sociatyUnionList = res.data.list || [];

        }, failCallbacks);
    };
    // 解绑商盟
    scope.unbindSociaty = function(item){
        sociatyUnionDao.invoke('unbind',{socId:item.sociatyId,unionId:scope.unionId}).then(function(){
            window.Message.show('解绑成功','success');
            item._done = true;
        }, function(){});
    };
    // 加入商盟
    scope.bindSociaty = function(item){
        sociatyUnionDao.invoke('bind',{socId:item.sociatyId,unionId:scope.unionId}).then(function(){
            window.Message.show('加入成功','success');
            item._done = true;
        }, function(){});
    };
    // 获取所有商会
    scope.addSociatyView = function(){
        scope.queryModel = {};
        scope.searchSociaty();
    };
    // 搜索商会
    scope.searchSociaty = function(){
        var key = scope.queryModel.key || ''
        sociatyDao.invoke('list',{key:key}).then(function(res){
            var list = res.data.list || [];

            scope.sociatyListAll = list;


        }, function(){});
    };
    // 清除之前的图片
    $('#unionFrameModal').on('hidden',function(){
        console.log('hidden')
        $('#unionFrameModal').find('.file-item .close').trigger('click');
    });
    $('#unionFrameModal').on('shown',function(){
        console.log('shown')
        
    });

    init();
}]);