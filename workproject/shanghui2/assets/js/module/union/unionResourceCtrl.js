angular.module('awApp').controller("unionResourceCtrl", ["$scope", "$state" ,"$stateParams","resource",
    function(scope, state,stateParams,resource) {
        var articleDao = resource("/article");
        var sociatyDao = resource("/sociaty");
        var resourceDao = resource("/resourceLabel");
        var unionId = scope.currentUser.unionId;
        // 类目管理
        scope.articleTypeView = function(){
            scope.articleTypeModel = {};
        }
        scope.queryArticleType = function(){
            resourceDao.invoke("list",{unionId:unionId}).then(function(res){
                scope.articleTypeList = res.data.list;
            },function(){})
        }
        scope.delArticleType = function(typeId){
            dm.confirm("确定删除这条数据？",function(){
                resourceDao.invoke("delete",{typeId:typeId}).then(function(res){
                    window.Message.show('删除成功','success');
                    scope.queryArticleType();
                }, function(){})
            })
        }
        scope.selectArticleType = function(item){
            scope.articleTypeModel = angular.extend({}, item);
        }

        scope.saveArticleType = function(item){
            var method = scope.articleTypeModel.typeId?'update':'create';
            resourceDao.invoke(method,scope.articleTypeModel).then(function(){
                window.Message.show('操作成功','success');
                scope.queryArticleType();
            });
        }

        var init = function(){
            sociatyDao.invoke("list",{unionId:unionId}).then(function(res){
                scope.sociatyList = res.data.list;
            }, function(){})
            scope.queryArticleType();
        }
        init();

        scope.queryModel = {
            page : 1,
            pageSize : 10,
            unionId : unionId
        }
        scope.query = function(){
            $('.table-wrapper').blockLoading('absolute');
            articleDao.invoke("list",scope.queryModel).then(function(res){
                var data = res.data;
                var pager = data.pager;
                scope.queryModel.page = pager.page;
                scope.pagerOption = pager;
                scope.dataList = data.list || [];
                $('.table-wrapper').blockLoading('hide');
            }, function(){});
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
                pageSize:10,
                unionId: unionId
            }
        }
        scope.search();
        // 带删除的数组
        scope.batchDelArr = [];
        scope.cbAll = false;
        scope.cbAllChange = function(){
            var dataList = scope.dataList || [];
            angular.forEach(dataList,function(value,key){
                value._selected = scope.cbAll;
            });
        };
        
        scope.batchDel = function(){
            var dataList = scope.dataList || [];
            var ret = [];
            angular.forEach(dataList,function(value,key){
                if(value._selected) {
                  ret.push(value.articleId);
                }
            });
            if(ret.length){
                dm.confirm('确认删除这'+ret.length+'条数据',function(){
                    articleDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
                        dm.notice('批量删除成功');
                        scope.query();
                        scope.cbAll = false;
                    })
                });
            }    
        };
        // 删除
        scope.del = function(id){
            dm.confirm('确认删除?',function(){
                articleDao._delete({id:id}).then(function(){
                    window.Message.show('删除成功','success')
                    scope.query();
                });
            });
        }

        // 0初始值 1 置顶
        scope.topAction = function(item){
            var status,msg = '',action = true;
            // 准备发布
            if(item.top==0){
                status = 1;
                msg = '置顶成功';
            }else if (item.top==1){
                status = 0;
                msg = '取消置顶成功'
            }else {
                action = false;
            }
            
            action && articleDao.invoke('update',{articleId:item.articleId,top:status}).then(function(){
                window.Message.show(msg,'success')
                scope.query();
            });
        }
        // 发布 0 草稿 1 发布中 2 过期
        scope.articlePublish = function(item){
            var status,msg = '',action = true;
            // 准备发布
            if(item.articleStatus==0){
                status = 1;
                msg = '发布成功';
            }else if (item.articleStatus==1){
                status = 0;
                msg = '取消发布成功'
            }else {
                action = false;
            }
            
            action && articleDao.invoke('publish',{articleId:item.articleId,articleStatus:status}).then(function(){
                window.Message.show(msg,'success')
                scope.query();
            });
        }
}])