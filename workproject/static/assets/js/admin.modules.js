
var _SideMenu = [
  {
    node:{
      name:'research',
      url:'/research',
      title:'研究模块'
    },
    list:[
      {
        name:'research.lazy',
        url:'/lazy',
        title:'动态加载',
        templateUrl:'../assets/tpls/home.html',
        controller:'AppCtrl',
        controllerUrl:'../assets/js/module/AppCtrl.js'
      },
      {
        name:'research.baidu',
        url:'/baidu',
        title:'百度',
        template:'<div class="iframe-wrapper"><iframe src="http://www.baidu.com" frameborder="0" class="iframe"></iframe></div>',
        controller:function(){}
      },
      {
        name:'research.fun',
        url:'/fun',
        title:'搞笑',
        template:'<div class="iframe-wrapper"><iframe src="http://121.199.36.124/majinhui/lab/comment/fun.html" frameborder="0" class="iframe"></iframe></div>',
        controller:function(){}
      }
    ]
  }
];
if(sessionStorage.getItem('admin')) {
  SideMenu = _SideMenu.concat(SideMenu)
}



var SideMenuStates = (function(SideMenu){
  var result ={};
  for(var i =0;i<SideMenu.length;i++){
    var module =  SideMenu[i];
    var node = module.node;
    var list = module.list;
    node.abstract = true;
    node.template = '<div ui-view></div>';
    result[node.name] = node;
    for(var j = 0;j<list.length;j++){
      var obj = list[j];
      
      if(obj.controllerUrl){
        (function(controllerUrl){
          obj.resolve = {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                // you can lazy load files for an existing module
                return $ocLazyLoad.load(controllerUrl);
            }]
          };
        })(obj.controllerUrl);
          
      }
      result[obj.name] = obj;
    }

  }
  
  return result;

})(SideMenu);

// 路由
window.STATES = {
  "lazy2":{
    url:'/lazy2',
    templateUrl: '../assets/tpls/home.html',
    controller:'AppCtrl',
    resolve: { 
        // Any property in resolve should return a promise and is executed before the view is loaded
        loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
            // you can lazy load files for an existing module
            return $ocLazyLoad.load('../assets/js/module/AppCtrl.js');
        }]
    }
  },
  "notfound":{
      url:'/notfound',
      template: '<div class="well well-sm" style="margin: 20px;"> <i class="fa fa-frown-o fa-4x pull-left"></i><h3>404 - Sorry! Not found your page.</h3> </div>'
  },
  "home":{
    url:'/home',
    templateUrl: '../assets/tpls/home.html',
    controller: ['$scope', '$state',
      function ($scope, $state) {
        console.log('home');
        
        
      }
    ]
  }
};
angular.extend(window.STATES,SideMenuStates);
console.log('window.STATES',window.STATES)
// APP
var app = angular.module('awApp', [
    'ngRoute', 
    'ui.router',
    'ui.bootstrap',
    'oc.lazyLoad',
    'angular-core',
    'aw.admin'
]);
app.config(['$ocLazyLoadProvider',"$provide","$compileProvider","$controllerProvider","$filterProvider",function($ocLazyLoadProvider,$provide,$compileProvider,$controllerProvider,$filterProvider){
    
    $ocLazyLoadProvider.config({
        event:true,
        debug: true,
        modules: [{
          name: 'TestModule',
          files: ['js/TestModule.js']
        }]
    });
    // will load the predefined configuration
    // $ocLazyLoad.load('TestModule'); 
    app.controller = $controllerProvider.register;
    app.directive = $compileProvider.register;
    app.filter = $filterProvider.register;
    app.factory = $provide.factory;
    app.service = $provide.service;
    app.constant = $provide.constant;

}]);

// 数据请求监听器
app.factory('UserInterceptor', ["$q","$rootScope",function ($q,$rootScope) {
  return {
        // 所有请求配置参数 XMLHttpRequest
        request:function(config){
          
            config.headers["X-Requested-With"] = 'XMLHttpRequest';
           
          return config;
        },
        responseError: function (response) {
            
            var data = response.data || {};
            // 判断错误码，如果是未登录
            if(data["errorCode"] == "500999"){
                // 清空用户本地token存储的信息，如果
                $rootScope.user = {token:""};
                // 全局事件，方便其他view获取该事件，并给以相应的提示或处理
                $rootScope.$emit("userIntercepted","notLogin",response);
            }
            // 如果是登录超时
            if(data["errorCode"] == "500998"){
                $rootScope.$emit("userIntercepted","sessionOut",response);
            }
            if(response.status == 404) {
              console.warn(404,response)
            }
            return $q.reject(response);
        }
  };
}]);

// 过滤请求错误
app.config(["$httpProvider" ,function($httpProvider) {
    $httpProvider.interceptors.push('UserInterceptor');
}]);


// 正式执行
app.run(["$rootScope","$state","resource" ,function(root,state,resource) {
  var adminDao = resource('/admin');

  root.SideMenu = SideMenu;
  // 获取当前管理员信息
  adminDao.invoke('current').then(function(result){
      var admin = result.data || {};
      root.admin =  admin;
  });
  root.imageUploadUrl = "http://file.qqmscf.com/upload/imageupload";

  root.logout = function(){
      //location.href = 'login.html'
	  adminDao.invoke('logout','')
  }
  root.$on('userIntercepted',function(errorType){
    // 跳转到登录界面，这里我记录了一个from，这样可以在登录后自动跳转到未登录之前的那个界面
    $state.go("login",{from:state.current.name,w:errorType});
  });
  /*  root.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
      if(toState.name=='login')return;// 如果是进入登录界面则允许
      // 如果用户不存在
      if(!$rootScope.user || !$rootScope.user.token){
        event.preventDefault();// 取消默认跳转行为
        $state.go("login",{from:fromState.name,w:'notLogin'});//跳转到登录界面
      }
  });*/

  root.$on('$stateChangeSuccess',function(event, toState, toParams, fromState, fromParams){
      var name = toState.name;
      
      var menu = $('.sidebar dd a[ui-sref="'+name+'"]');
      if(menu.length>0){
        $('.sidebar dd').removeClass('active');
        menu.closest('dd').addClass('active');

      }
      //console.log(window.STATES[name])
      $('#cur').text(window.STATES[name].title || name);
      //console.log('toState',toState)
  });
  root.$on('$stateNotFound', function(event, unfoundState, fromState, fromParams){
      event.preventDefault();
      //state.go('notfound');
  });
}]);
// 配置路由
app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      
      angular.forEach(window.STATES,function(value,key){
          //console.log(key,value);
          $stateProvider.state(key,value);
      });
      $urlRouterProvider.otherwise('/home');
      // $stateProvider.state('home', {
      //   url:'/home',
      //   template: '<h1>Welcome to UI-Router Demo</h1>',
      //   controller: ['$scope', '$state',
      //     function ($scope, $state,mLoading) {
      //       console.log('home',mLoading)
            
      //     }
      //   ]
      // }).state('hello', {
      //   url:'/hello',
      //   templateUrl: '../assets/tpls/hello.html',
      //   controller: 'helloCtrl'
      // }).state('world', {
      //   url:'/world',
      //   templateUrl: '../assets/tpls/world.html',
      //   controller: 'worldCtrl'
      // });

    }
]);


/* 管理员模块 */
angular.module('aw.admin', ['angular-core'])
.controller("adminCtrl", ["$scope", "$state" ,"$stateParams","resource",function(scope, state,stateParams,resource) {
    var adminDao = resource('/admin');
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
          adminDao.invoke('batchdel',{ids:ret.join(',')}).then(function(){
            dm.notice('批量删除成功');
            scope.query();
          })
        });
      }
        
    };
    scope.Catlogs = [{id:1,name:'超级管理员'},{id:0,name:'普通管理员'}];
    scope.query = function() {
        $('.table-wrapper').blockLoading('absolute');
        adminDao.list(scope.queryModel).then(function(result){
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
            /*adminDao.invoke('del',{id:id}).then(function(){
              alert(0)
            },function(){
              alert(1)
            })*/
            adminDao._delete({id:id}).then(function(){
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
    

}]).controller("adminEditCtrl", ["$scope", "$state", "$stateParams","resource",function(scope, state,stateParams,resource) {
    var adminDao = resource('/admin');

    console.log('stateParams',stateParams);

    if (stateParams.id) {
        scope.isNew = false;
        adminDao.get({id:stateParams.id}).then(function(result){
            scope.formData = result.data;
        });
    } else {
        scope.isNew = true;
        scope.formData = {
            role:0
        };
    };

    scope.save = function () {
        
        var method = scope.isNew ? "create" : "update";
        adminDao.invoke(method, scope.formData).then(function (data) {
        	console(data);
        	if(data.code !==0){
        		dm.alert(data.message);
        	}else{
        		   dm.alert("保存成功",function(){
                       state.go("admin.list")
                   });
        	}
        },function(data){
        	dm.alert(data.message);
        });
            
    };
    

}]);


