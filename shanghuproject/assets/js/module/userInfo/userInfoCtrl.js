angular.module('awApp').controller('userInfoCtrl',['$scope','$state','$stateParams','resource','MEMBER_TYPE',
    function(scope,state,stateParams,resource,MEMBER_TYPE){
        var userDemo = resource('/userInfo');
        var sociatyPosition = resource('/sociatyPosition');
        scope.memberType = MEMBER_TYPE;
        var init = function(){
            sociatyPosition.invoke('list',{}).then(function(result){
                var sociatyData = result.data.list || [];
                scope.sociaty_position_list = sociatyData;
            })
        }
        init();
        scope.queryModel = {};
        scope.query = function(){
            userDemo.invoke('list',scope.queryModel).then(function(result){
                var list = result.data.list;
                scope.dataList = list;
            },function(){});
        }

        scope.del = function(userId){
            dm.confirm("确认删除该信息？",function(){
                userDemo.invoke('delete',{userId : userId}).then(function(result){
                    console.log("delete")
                    scope.query();
                })
            });  
        }
        scope.cbAll = false;
        scope.cbAllChange = function(){
            var data = scope.dataList || [];
            angular.forEach(data,function(value,key){
                value._selected = scope.cbAll;
            })
        }

        scope.batchDel = function(){
            var dataList = scope.dataList || [];
            var delArr = [];
            angular.forEach(dataList,function(value,key){
                if(value._selected){
                    delArr.push(value.userId);
                }
            })
            if(delArr.length){
                 dm.confirm("确定删除这"+delArr.length+"条数据吗？",function(){
                    userDemo.invoke("delete",{delIds:delArr.join(",")}).then(function(){
                        dm.notice("删除成功！");
                        scope.cbAll = false;
                        scope.query();
                    },function(){})
                })
            }else{
                dm.notice("你还未选择要删除的数据")
            } 
        }

        // 查询
        scope.search = function(){
            scope.query();
        }
        scope.clear =function(){
            scope.queryModel = {

            };
        }

        scope.query();
    }
])

// 用户编辑
angular.module('awApp').controller("userInfoEditCtrl",['$scope','$state',"$stateParams",'resource',
    function(scope,state,stateParams,resource){
        var userDemo = resource('/userInfo');
        if(stateParams.id){
            userDemo.get({userId:stateParams.id}).then(function(result){
                scope.isNew = false;
                scope.formData = result.data;
            })
        }else{
            scope.isNew = true;
            scope.formData = [];
        }
        
        scope.save = function(){
            var method = scope.isNew ? "create":"update";
            userDemo.invoke(method,scope.formData).then(function(result){
                dm.alert("保存成功",function(){
                    state.go("userInfo.list");
                })
            })
        }
    }
])