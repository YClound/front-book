/*
    商会编辑和创建
*/
var GB2260 = {
    "330000": "浙江省",
    "330100": "浙江省杭州市",
    "330101": "浙江省杭州市市辖区",
    "330102": "浙江省杭州市上城区",
    "330103": "浙江省杭州市下城区",
    "330104": "浙江省杭州市江干区",
    "330105": "浙江省杭州市拱墅区",
    "330106": "浙江省杭州市西湖区",
    "330108": "浙江省杭州市滨江区",
    "330122": "浙江省杭州市桐庐县",
    "330127": "浙江省杭州市淳安县",
    "330181": "浙江省杭州市萧山市",
    "330182": "浙江省杭州市建德市",
    "330183": "浙江省杭州市富阳市",
    "330184": "浙江省杭州市余杭市",
    "330185": "浙江省杭州市临安市",
    "330200": "浙江省宁波市",
    "330201": "浙江省宁波市市辖区",
    "330203": "浙江省宁波市海曙区",
    "330204": "浙江省宁波市江东区",
    "330205": "浙江省宁波市江北区",
    "330206": "浙江省宁波市北仑区",
    "330211": "浙江省宁波市镇海区",
    "330225": "浙江省宁波市象山县",
    "330226": "浙江省宁波市宁海县",
    "330227": "浙江省宁波市鄞县",
    "330281": "浙江省宁波市余姚市",
    "330282": "浙江省宁波市慈溪市",
    "330283": "浙江省宁波市奉化市",
    "330300": "浙江省温州市",
    "330301": "浙江省温州市市辖区",
    "330302": "浙江省温州市鹿城区",
    "330303": "浙江省温州市龙湾区",
    "330304": "浙江省温州市瓯海区",
    "330322": "浙江省温州市洞头县",
    "330324": "浙江省温州市永嘉县",
    "330326": "浙江省温州市平阳县",
    "330327": "浙江省温州市苍南县",
    "330328": "浙江省温州市文成县",
    "330329": "浙江省温州市泰顺县",
    "330381": "浙江省温州市瑞安市",
    "330382": "浙江省温州市乐清市",
    "330400": "浙江省嘉兴市",
    "330401": "浙江省嘉兴市市辖区",
    "330402": "浙江省嘉兴市秀城区",
    "330411": "浙江省嘉兴市郊区",
    "330421": "浙江省嘉兴市嘉善县",
    "330424": "浙江省嘉兴市海盐县",
    "330481": "浙江省嘉兴市海宁市",
    "330482": "浙江省嘉兴市平湖市",
    "330483": "浙江省嘉兴市桐乡市",
    "330500": "浙江省湖州市",
    "330501": "浙江省湖州市市辖区",
    "330521": "浙江省湖州市德清县",
    "330522": "浙江省湖州市长兴县",
    "330523": "浙江省湖州市安吉县",
    "330600": "浙江省绍兴市",
    "330601": "浙江省绍兴市市辖区",
    "330602": "浙江省绍兴市越城区",
    "330621": "浙江省绍兴市绍兴县",
    "330624": "浙江省绍兴市新昌县",
    "330681": "浙江省绍兴市诸暨市",
    "330682": "浙江省绍兴市上虞市",
    "330683": "浙江省绍兴市嵊州市",
    "330700": "浙江省金华市",
    "330701": "浙江省金华市市辖区",
    "330702": "浙江省金华市婺城区",
    "330721": "浙江省金华市金华县",
    "330723": "浙江省金华市武义县",
    "330726": "浙江省金华市浦江县",
    "330727": "浙江省金华市磐安县",
    "330781": "浙江省金华市兰溪市",
    "330782": "浙江省金华市义乌市",
    "330783": "浙江省金华市东阳市",
    "330784": "浙江省金华市永康市",
    "330800": "浙江省衢州市",
    "330801": "浙江省衢州市市辖区",
    "330802": "浙江省衢州市柯城区",
    "330821": "浙江省衢州市衢县",
    "330822": "浙江省衢州市常山县",
    "330824": "浙江省衢州市开化县",
    "330825": "浙江省衢州市龙游县",
    "330881": "浙江省衢州市江山市",
    "330900": "浙江省舟山市",
    "330901": "浙江省舟山市市辖区",
    "330902": "浙江省舟山市定海区",
    "330903": "浙江省舟山市普陀区",
    "330921": "浙江省舟山市岱山县",
    "330922": "浙江省舟山市嵊泗县",
    "331000": "浙江省台州市",
    "331001": "浙江省台州市市辖区",
    "331002": "浙江省台州市椒江区",
    "331003": "浙江省台州市黄岩区",
    "331004": "浙江省台州市路桥区",
    "331021": "浙江省台州市玉环县",
    "331022": "浙江省台州市三门县",
    "331023": "浙江省台州市天台县",
    "331024": "浙江省台州市仙居县",
    "331081": "浙江省台州市温岭市",
    "331082": "浙江省台州市临海市",
    "332500": "浙江省丽水地区",
    "332501": "浙江省丽水地区丽水市",
    "332502": "浙江省丽水地区龙泉市",
    "332522": "浙江省丽水地区青田县",
    "332523": "浙江省丽水地区云和县",
    "332525": "浙江省丽水地区庆元县",
    "332526": "浙江省丽水地区缙云县",
    "332527": "浙江省丽水地区遂昌县",
    "332528": "浙江省丽水地区松阳县",
    "332529": "浙江省丽水地区景宁畲族自治县"
};
angular.module('awApp').controller("commerceEditCtrl", ["$scope", "$state", "$stateParams","resource","$rootScope",function(scope, state,stateParams,resource,rootScope) {
    var sociatyDao = resource('/sociaty');
    var sociatyPositionDao = resource('/sociatyPosition');
    var sociatyGroupDao = resource('/sociatyGroup');
    var ID = stateParams.id;
    var idcardAreaField = $('#idcardArea').data('field');
    var idcardAreaInit = function(){
        var buffer = ['<option value="">--请选择--</option>'],html = '';
        for(var key in GB2260){
            var value = GB2260[key];
            html='<option value="'+value+'">'+value+'</option>';
            buffer.push(html);
        }
        $('#idcardArea').html(buffer.join(''));
        
        
    };
    idcardAreaInit();
    
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
    // 同乡认证呢区域
    scope.selectedArea = {};
    
    

    var init = function(){
        // 先获取省份然后获取商会
        scope.provinceList = scope.cityDataFn();
        
        if (ID) {
            scope.isNew = false;
            sociatyDao.get({sociatyId:ID}).then(function(result){
                scope.formData = result.data;
                // 初始化同乡区域
                
                $('#idcardArea').val(scope.formData[idcardAreaField])
                $('#idcardArea').comboSelect();
                //scope.formData.sociatyLogo = 'http://www.chuyingfund.com/files/account/headImg/120/2016/20160612131503913_1280x960.jpg';
                //获取职位
                sociatyPositionDao.invoke('listAll',{sociatyId:ID}).then(function(res){
                    scope.positionList = res.data.list || [];
                }, function(){});
                // 获取分组
                sociatyGroupDao.invoke('listAll',{sociatyId:ID}).then(function(res){
                    scope.groupList = res.data.list || [];
                }, function(){});
                // 获取管辖城市
                scope.formData.province && scope.provinceChange();
                scope.formData.city && scope.cityChange();
                scope.formData.district && scope.districtChange();
                scope.isInit = true;

            });
        } else {
            scope.isNew = true;

            scope.formData = {
                
            };
            scope.positionList = [];
            scope.groupList = [];
            $('#idcardArea').comboSelect();

        };
    };
    
    scope.getCityName = function(list,code){
        for(var i = 0;i<list.length;i++){
            if(list[i]['code'] == code) {
                return list[i];
            }
        }
        return null;
    };
    init();
    var myArea = {};
    // 省份变化获取新的城市
    scope.provinceChange = function(){
        console.log('province',scope.formData.province)
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
        console.log('city',scope.formData.city)
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
        console.log('district',scope.formData.district)
        var key = scope.formData.district || '';
        if(key){
            var target = scope.getCityName(scope.districtList,key);
            myArea.district = target.text;
            console.log('target',target);
        }
            
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
        var area = [];
        // 管辖城市中文
        if(myArea.province) {
            area.push(myArea.province)
        }
        if(myArea.city){
            area.push(myArea.city)
        }
        if(myArea.district){
            area.push(myArea.district)
        }
        scope.formData.area = area.join(',');
        console.log('管辖区域',myArea)
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
        // 同乡区域
        if(!$('#idcardArea').val()){
            window.Message.show('请选择同乡认证区域','warning');
            return;
        }
        scope.formData[idcardAreaField] = $('#idcardArea').val();
        
        var method = scope.isNew ? "create" : "update";
        sociatyDao.invoke(method, scope.formData).then(function () {

            dm.alert("保存成功",function(){
                state.go("commerce.list")
            });
            
        });
            
    };
    

}]);