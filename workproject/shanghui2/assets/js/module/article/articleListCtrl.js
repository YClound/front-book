/* 
    商会资源模块 
*/
angular.module('awApp').controller("articleListCtrl", ["$scope", "$state" ,"$stateParams","resource","MEMBER_TYPE",function(scope, state,stateParams,resource,MEMBER_TYPE) {
    var articleDao = resource('/article');
    var articleTypeDao = resource('/articleType');
    var sociatyId = scope.currentUser.sociatyId;
    console.log(111,stateParams)
    // 获取类别列表
    scope.queryArticleType = function(){

        articleTypeDao.invoke('list',{sociatyId:sociatyId}).then(function(res){
            var list = res.data.list;

            scope.articleTypeList = list;
        });
    };
    // 进入类别编辑页面
    scope.articleTypeView = function(){
        scope.articleTypeModel = {};
    };
    // 
    scope.selectArticleType = function(item){
        scope.articleTypeModel = angular.extend({},item);
    };
    // del
    scope.delArticleType = function(id){

        dm.confirm('确认删除?',function(){
            //alert(1);
            articleTypeDao.invoke('delete',{typeId:id}).then(function(){
                window.Message.show('删除成功','success');
                scope.queryArticleType();
            });
        });
            
    };
    // 保存
    scope.saveArticleType = function(){
        var method = scope.articleTypeModel.typeId?'update':'create';

        articleTypeDao.invoke(method,scope.articleTypeModel).then(function(){
            window.Message.show('操作成功','success');
            scope.queryArticleType();
        });
    };

    scope.queryModel = {
        sociatyId:sociatyId,
        page:1,
        pagesize:10
    };
    scope.articleTypeChange = scope.articleReadAuthChange = function(){
        scope.search();
    };
    
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
          ret.push(value.userId);
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
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        articleDao.list(scope.queryModel).then(function(result){
            var data = result.data;
            var list = data.list || [];
            var pager = data.pager;
            scope.pagerOption = pager;

            scope.dataList = list;
            scope.queryModel.page = pager.page;
           
            $('.table-wrapper').blockLoading('hide');
        });
    
    }
    // 删除
    scope.del = function(id){
        dm.confirm('确认删除?',function(){
            articleDao._delete({id:id}).then(function(){
                window.Message.show('删除成功','success')
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
            sociatyId:sociatyId,
            page:1,
            pagesize:10
        };
    }
    // 0初始值 1 置顶
    scope.topAction = function(item){
        console.log(1)
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
        
        action && articleDao.invoke('update',{top:status}).then(function(){
            window.Message.show(msg,'success')
            scope.query();
        });
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
        
        action && articleDao.invoke('publish',{articleStatus:status}).then(function(){
            window.Message.show(msg,'success')
            scope.query();
        });
        
        
    }
    
    var init = function(){
        scope.search();
        scope.queryArticleType();
    };
    init(); 

}]);
