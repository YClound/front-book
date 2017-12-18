/* 会员编辑模块 */
angular.module('awApp').controller("unionMemberEditCtrl", ["$scope", "$state", "$stateParams","resource","MEMBER_TYPE","$rootScope",function(scope, state,stateParams,resource,MEMBER_TYPE,rootScope) {
    var memberDao = resource('/user');
    var sociatyDao = resource('/sociaty');
    var sociaty_position_dao = resource('/sociatyPosition');
    var sociaty_group_dao = resource('/sociatyGroup');
    var requestCount = 0;
    var unionId = scope.currentUser.unionId;
    var userId = stateParams.id;
    var sociatyId = scope.currentUser.sociatyId;
    var failCallbacks = function(){};
    var requestCount = 0;
    var selectedGroup = '';
    var ajaxOther = function(sociatyId){
        // 获取商会职务
        sociaty_position_dao.invoke('listAll',{sociatyId:sociatyId}).then(function(res){
            var list = res.data.list || [];

            scope.sociaty_position_list = list;
            

        }, function(){});
        // 获取商会分组
        sociaty_group_dao.invoke('listAll',{sociatyId:sociatyId}).then(function(res){
            var list = res.data.list || [];

            scope.sociaty_group_list = list;
            // 选中分组
            angular.forEach(scope.sociaty_group_list,function(value,key){
                if(selectedGroup.indexOf(value.groupId+'')>-1){
                    value._selected = true;
                }
            });
           
        }, function(){});
    };
    var init = function(){
        scope.formData = {
            role:0,
            memberType:2,// 默认同乡
            sociatyId:sociatyId
        };

        // 获取商联商会
        sociatyDao.invoke('list',{unionId:unionId}).then(function(res){
            var list = res.data.list || [];
            scope.sociatyList = list;

        }, failCallbacks);
        // 先获取省份然后获取商会
        scope.provinceList = scope.cityDataFn();
        if(userId) {
            scope.isNew = false;
            memberDao.get({userId:userId}).then(function(result){
                scope.formData = result.data;
                var groupIds = scope.formData.groupIds || '';
                selectedGroup = groupIds.split(',');
                ajaxOther(sociatyId);
                 // 获取管辖城市
                scope.formData.province && scope.provinceChange();
                scope.formData.city && scope.cityChange();
                scope.formData.district && scope.districtChange();
                scope.isInit = true;
            });

        } else {
            scope.isNew = true;
        }  
    };
    init();
    scope.getCityName = function(list,code){
        for(var i = 0;i<list.length;i++){
            if(list[i]['code'] == code) {
                return list[i];
            }
        }
        return null;
    };
    var myArea = {};
    // 省份变化获取新的城市
    scope.provinceChange = function(){
        var key = scope.formData.province || '';
        scope.cityList = scope.cityDataFn(key) || [];
        var target = scope.getCityName(scope.provinceList,key);
        myArea.province = target.text;
        console.log('target',target);
        myArea.city = '';
        myArea.district = '';
        if(scope.isInit){
            scope.formData.city = '';
            scope.formData.district = '';
        }  
    };
    // 城市变化获取新的地区
    scope.cityChange = function(){
        var key = scope.formData.city || '';
        if(key) {
            scope.districtList = scope.cityDataFn(key);
            var target = scope.getCityName(scope.cityList,key);
            myArea.city = target.text;
            console.log('target',target);
        }
        myArea.district = '';
        if(scope.isInit){
            scope.formData.district = '';
        }      
    };
    // 街道变化
    scope.districtChange = function(){
        var key = scope.formData.district || '';
        if(key){
            var target = scope.getCityName(scope.districtList,key);
            myArea.district = target.text;
            console.log('target',target);
        }      
    };

    // 商会变化获取职位和分组
    scope.sociatyChange = function(){
        var sociatyId = scope.formData.sociatyId;
        ajaxOther(sociatyId);
    };
    // 根据身份证号获取名下企业
    scope.enterpriseList = [];
    scope.selectEnterprise = function(enterprise){
        scope.enterprise = enterprise;
        scope.formData.registerCompany = enterprise.name;
        scope.formData.industry = enterprise.industry;
    };
    scope.searchEnterprise = function(){
        var value = scope.formData.userIdcard;

        if(value && value.length===18){
            resource('/enterprise/list').invoke('',{userIdcard:value}).then(function(res){
                scope.enterpriseList = res.data.list || []
            });
        }else {
            scope.enterpriseList = []
        }
    };
    
            
    scope.MEMBER_TYPE = MEMBER_TYPE;
    scope.save = function () {
        var selected = [];
        angular.forEach(scope.sociaty_group_list,function(value,key){
            if(value._selected){
                selected.push(value.groupId);
            }
        });
        scope.formData.groupIds = selected.join(',');
        var area = [];
        //现居地址
        var area = [];
        if(myArea.province){
            area.push(myArea.province);
        };
        if(myArea.city){
            area.push(myArea.city);
        };
        if(myArea.district){
            area.push(myArea.district);
        }
        scope.formData.area = area.join(",");
        var method = scope.isNew ? "create" : "update";
        memberDao.invoke(method, scope.formData).then(function () {

            dm.alert("保存成功",function(){
                history.go(-1);
            });
            
        });
            
    };

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
    // 选择介绍人
    scope.inviterView = function(){
        scope.search();
    };
    // 指定介绍人
    scope.setInviter = function(item){
        console.log('指定介绍人',item.userId,item);
        scope.inviterId = item.userId;
        scope.formData.inviterId = item.userId;
        scope.formData.inviter = angular.extend({}, item);
        
    }
    

}]);