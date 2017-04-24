/* 
    会员列表模块 
*/
angular.module('awApp').controller("memberCtrl", ["$scope", "$state" ,"$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var memberDao = resource('/user');
    var sociaty_position_dao = resource('/sociatyPosition');
    var sociaty_group_dao = resource('/sociatyGroup');

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
          memberDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
            dm.notice('批量删除成功');
            scope.query();
            scope.cbAll = false;
          })
        });
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
    scope.del = function(id){
        dm.confirm('确认删除?',function(){
            /*memberDao.invoke('del',{id:id}).then(function(){
              alert(0)
            },function(){
              alert(1)
            })*/
            memberDao._delete({id:id}).then(function(){
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
    scope.search();
    

}]);
/* 会员编辑模块 */
angular.module('awApp').controller("memberEditCtrl", ["$scope", "$state", "$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var memberDao = resource('/user');
    var sociaty_position_dao = resource('/sociatyPosition');
    var sociaty_group_dao = resource('/sociatyGroup');
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
    };
    init();

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
    
    var next = function(){
        requestCount++;
        if(requestCount>=2) {
            if (stateParams.id) {
                scope.isNew = false;
                memberDao.get({userId:stateParams.id}).then(function(result){
                    scope.formData = result.data;
                    var groupIds = scope.formData.groupIds || '';
                    var selected = groupIds.split(',');
                    angular.forEach(scope.sociaty_group_list,function(value,key){
                        if(selected.indexOf(value.groupId+'')>-1){
                            value._selected = true;
                        }
                    });
                   
                    
                });
            } else {
                scope.isNew = true;
                scope.formData = {
                    role:0
                };
            };
        }
    };
    console.log('stateParams',stateParams);

            
    scope.MEMBER_TYPE = MEMBER_TYPE;
    scope.save = function () {
        var selected = [];
        angular.forEach(scope.sociaty_group_list,function(value,key){
            if(value._selected){
                selected.push(value.groupId);
            }
        });
        scope.formData.groupIds = selected.join(',');
        var method = scope.isNew ? "create" : "update";
        memberDao.invoke(method, scope.formData).then(function () {

            dm.alert("保存成功",function(){
                state.go("member.list")
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