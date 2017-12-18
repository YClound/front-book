angular.module('awApp').controller("unionHelpCtrl", ["$scope", "$state","resource",
    function(scope, state,resource) {
        var helpDao = resource('/helpApply');
        var sociatyDao = resource('/sociaty');
        var unionId =  scope.currentUser.unionId;
        scope.queryModel = {
            page : 1,
            pageSize : 10,
            unionId : unionId
        }
        var init = function(){
            sociatyDao.invoke("list",{unionId : unionId}).then(function(res){
                scope.sociatyList = res.data.list; 
            }, function(){})
        }
        init();

        scope.query = function(){
            $('.table-wrapper').blockLoading('absolute');
            helpDao.invoke("list",scope.queryModel).then(function(res){
                var data = res.data;
                var pager = data.pager;
                scope.dataList = data.list || [];
                console.log(data.list);
                scope.queryModel.page = pager.page;
                scope.pagerOption = pager;

                $('.table-wrapper').blockLoading('hide');
            })
        }
        // 查询
        scope.search = function(){
            scope.queryModel.page = 1;
            scope.query();
        }
        // 重置
        scope.clear = function(){
            scope.queryModel = {
                page : 1,
                pageSize : 10,
                unionId : unionId
            }
        }
        /* helpStatus 0初始值 1同意 2拒绝 */
        // 同意
        scope.agreeHelp = function(item){
            var helpStatus = 1;
            var helpId = item.helpId;
            helpDao.invoke("update",{helpId:helpId,helpStatus:helpStatus}).then(function(){
                window.Message.show('已同意','success');
                item.helpStatus = 1;
            })
        }
        // 拒绝
        scope.rejectHelp = function(item){
            var helpStatus = 2;
            var helpId = item.helpId;
            helpDao.invoke("update",{helpId:helpId,helpStatus:helpStatus}).then(function(){
                window.Message.show('已拒绝','success');
                item.helpStatus = 2;
            })
        }
        // 删除
        scope.deleteHelp = function(helpId){
            helpDao.invoke("delete",{helpId : helpId}).then(function(){
                window.Message.show("已删除","success");
                scope.query();
            })
        }
        scope.search();
}]);