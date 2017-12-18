angular.module('awApp').controller("unionActivityListCtrl", ["$scope", "$state" ,"$stateParams","resource",
    function(scope, state,stateParams,resource) {
        var articleDao = resource("/article");
        var unionId = scope.currentUser.unionId;
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

        // '0':'置顶','1':'取消置顶'
        scope.topAction = function(item){
            var status,msg="",action=true;
            if(item.top ==0){
                status = 1;
                msg = "置顶成功"
            }else if(item.top == 1){
                status = 0;
                msg = "取消置顶成功"
            }else{
                action = false;
            }

            action && articleDao.invoke("update",{id:item.articleId,top:status}).then(function(res){
                window.Message.show(msg,"success");
                scope.query();
            })
        };
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
    }
]);

angular.module('awApp').controller("unionActivityEditCtrl", ["$scope", "$state" ,"$stateParams","resource",
    function(scope, state,stateParams,resource) {
        var articleDao = resource("/article");
        var ID = stateParams.id;
        var failCallbacks = function(){}
        var init = function(){
            console.log('state',state)
            scope.formData = {
            };
            scope.isNew = true;
            // 权限
            scope.readAuth = {id:0,text:'公开'};
            scope.readAuthList = $.extend(true, [], scope.ReadAuthList);
            if(ID) {
                scope.isNew = false;
                // 获取活动信息
                articleDao.invoke('get',{articleId:ID}).then(function(res){
                    var data = res.data || null;
                    var _articleReadAuth = data.articleReadAuth.split(',');
                    if(_articleReadAuth == "0"){
                        scope.readAuth._selected = true;
                    }else{
                        scope.readAuth._selected = false;
                        angular.forEach(scope.readAuthList,function(value,key){  
                            if(_articleReadAuth.indexOf(value.id+'')>-1){
                                value._selected = true;
                            }
                        });
                    }
                    scope.formData = data;
                    console.log(data)
                }, failCallbacks);                
            }
        };
        init();
        scope.readAuthChange = function(target){
            var _selected = target._selected;
            if(_selected){
                angular.forEach(scope.readAuthList,function(value,key){
                    value._selected = false;
                });
            }
            
        };
        scope.readAuthSync = function(){
            angular.forEach(scope.readAuthList,function(value,key){
                if(value._selected){
                    scope.readAuth._selected = false;
                }
            });
        }

        // 存为草稿
        scope.saveAsDraft = function(){
            scope.save();
            var formData = scope.formData;
            var method = scope.isNew ? "create" : "update";
            formData.articleStatus = 0;
            articleDao.invoke(method, formData).then(function (res) {
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
            formData.articleStatus = 1;
            articleDao.invoke(method, formData).then(function (res) {
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
            $('#container').html(scope.formData.articleContent);
        };
        // 点击关闭预览
        scope.closeArticle = function(){
            $(".main-content").find('.activity-detail').hide();
        }

        scope.save = function () {
            var selected1 = [],selected2 = [],userIds = [];
            // 查看权限
            angular.forEach(scope.readAuthList,function(value,key){
                if(value._selected){
                    selected2.push(value.id);
                }
            });
            if(scope.readAuth._selected){
                scope.formData.articleReadAuth = 0;
            }else{
                scope.formData.articleReadAuth = selected2.join(',');
            }   
        };

    }
])