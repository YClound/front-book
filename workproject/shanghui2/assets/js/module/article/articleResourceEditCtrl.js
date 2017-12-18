/* 
    商会资源文章编辑模块 
*/

angular.module('awApp').controller("articleResourceEditCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var articleDao = resource('/article');
    var articleTypeDao = resource('/articleType');
    var sociatyId = scope.currentUser.sociatyId;
    var unionId = scope.currentUser.unionId;
    var ID = stateParams.id;
    // 判断是否是商联资源编辑
    var isUnion = state.current.name;
    var failCallbacks = function(){}
    var init = function(){
        console.log('state',state)
        scope.formData = {

        };
        scope.isNew = true;
        // 权限
        scope.readAuth = {id:0,text:'公开'};
        scope.readAuthList = $.extend(true, [], scope.ReadAuthList);
        // 获取商会文章类别,如果是商会资源编辑加载商会的文章类型列表和商联的文章类型列表，否则只加载商联的文章类型列表
        if(isUnion.indexOf("union") < 0){
            articleTypeDao.invoke('list',{sociatyId:sociatyId,pagesize:100}).then(function(res){
                scope.articleTypeList = res.data.list;
            }, failCallbacks);
        }
        
        // 获取商联文章类别
        articleTypeDao.invoke('list',{unionId:unionId,pagesize:100}).then(function(res){
            scope.unionArticleTypeList = res.data.list;

        }, failCallbacks);

        if(ID) {
            scope.isNew = false;
            // 获取活动信息
            articleDao.invoke('get',{articleId:ID}).then(function(res){
                var data = res.data || null;
               
                var _articleReadAuth = data.articleReadAuth ? data.articleReadAuth.split(',') : '';
                console.log(_articleReadAuth);
                if(_articleReadAuth == "0"){
                    console.log("查看权限");
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
            
        }else {

        }
    };
    init();

    
    
    console.log('stateParams',stateParams);
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


    

}]);