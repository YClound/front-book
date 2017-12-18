
var SideMenuStates = (function(SideMenu){
  var result ={};
  var parseModules = function(modules){
    for(var j = 0;j<modules.length;j++){
      var obj = modules[j];


      if(!obj.controller){
        obj.controller = ['$scope',function(scope){}];
      }
      if(obj.templateUrl) {
        obj.templateUrl = API.root + obj.templateUrl + '?' + new Date().valueOf();
      }
      if(obj.controllerUrl || obj.dependency){
        obj.controllerUrl = API.root + obj.controllerUrl;
        (function(controllerUrl){
          var dependency = obj.dependency;
          obj.resolve = {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                var files = dependency || [];
                controllerUrl && files.push(controllerUrl + '?' + new Date().valueOf());
                // you can lazy load files for an existing module
                return $ocLazyLoad.load(files);
            }]
          };
        })(obj.controllerUrl);
          
      }
      //console.log(111,obj.name)
      result[obj.name] = obj;
    }
  };
  for(var i =0;i<SideMenu.length;i++){
    var panel =  SideMenu[i];
    var node = panel.node;
    var list = panel.module || [];
    parseModules(list)
  };

  console.log('SideMenuStates',result);
  parseModules(SideModule);
  return result;

})(SideMenu);

// 路由
window.STATES = {
  "notfound":{
      url:'/notfound',
      template: '<div class="well well-sm ovh" > <i class="fa fa-frown-o fa-4x pull-left"></i><h3>404 - Sorry! Not found your page.</h3> </div>'
  },
  "demos":{
    url:"/demos",
    template:'<div><div ui-view></div></div>',
    abstract:true
  },
  "demos.umeditor":{
    url:'/umeditor',
    templateUrl: '../assets/demos/umeditor.html?1',
    controller:"umeditorCtrl",
    resolve: { 
        // Any property in resolve should return a promise and is executed before the view is loaded
        loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
            // you can lazy load files for an existing module
            return $ocLazyLoad.load(['../vendor/umeditor/umeditor.min.js','../assets/demos/1.js','../assets/demos/umeditorCtrl.js']);
        }]
    }
  },
  "home":{
    url:'/home',
    templateUrl: API.root+'/module/home.html',
    controller: ['$scope', '$state',
      function ($scope, $state) {
        console.log('home');
        $scope.startDate  = '';
        $scope.endDate  = '';
        
      }
    ]
  }
};

angular.extend(window.STATES,SideMenuStates);
console.log('window.STATES',window.STATES)
// APP
var app = angular.module('awApp', [
    'ngRoute', 
    'ngSanitize',
    'ui.router',
    'oc.lazyLoad',
    'angular-core',
    'treeControl'
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
          
            config.headers["X-Requested-With"] = "XMLHttpRequest";
           
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

/*
  全局变量
*/ 
// 会员类型
app.value('MEMBER_TYPE',[{'id':2,name:'同乡'},{'id':3,name:'会员'}]);


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

// 正式执行
app.run(["$rootScope","$state","resource" ,"$templateCache",function(root,state,resource,$templateCache) {
  
  var sideMenuAuth = function(auth,SideMenu){
      var item = null;
      var ret = [];
      var _auth = auth.split(',');
      for(var i = 0;i<SideMenu.length;i++){
          item = SideMenu[i];
          var itemAuth = item.auth || '';
          if(_auth.indexOf('admin')>-1) {
              if('admin' === itemAuth) {
                ret.push(item)
              }
          }
          else if(!itemAuth || auth.indexOf(itemAuth)>-1) {
              ret.push(item)
          }

      }
      return ret;
  };
  
  root.all_config = {
      initialFrameWidth:800,
      initialFrameHeight:300,
      toolbar:[
          'source | undo redo | bold italic underline strikethrough | forecolor backcolor | removeformat |',
          'insertorderedlist insertunorderedlist | cleardoc  | fontfamily fontsize' ,
          '| justifyleft justifycenter justifyright justifyjustify |',
          'link unlink |  image  ',
          '| horizontal print preview  fullscreen'
      ]
  };
  // 当前用户信息
  root.currentUser = window.serverData.currentUser;
  
  root.serverData = window.serverData;
  
  // 左侧菜单权限控制
  root.SideMenu = sideMenuAuth(root.currentUser.auth,SideMenu);
  
  
  console.log('root.currentUser',root.currentUser)
  
  
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
      //$templateCache.removeAll();
      var name = toState.name;
      var obj;
      var menu = $('.sidebar a[ui-sref="'+name+'"]');
      if(menu.length>0){
        $('.sidebar a').removeClass('active');
        menu.addClass('active');
        // 假如连接本身不是父级，则向上寻找展开
        if(!menu.siblings('.nav-second-level ').length){

          menu.closest('.collapse-content').collapse('show');
          
        }
          
      }else {
        $('.sidebar a').removeClass('active');
      }
      // 移除模板拒绝缓存
      /*if(fromState && (obj = window.STATES[fromState.name])) {
        console.log('template',obj.templateUrl)
        obj.templateUrl && $templateCache.remove(template);
      }*/
      //console.log(window.STATES[name])
      $('#cur').text(window.STATES[name].text || name);
      //console.log('toState',toState)
  });
  root.$on('$stateNotFound', function(event, unfoundState, fromState, fromParams){
      event.preventDefault();
      //state.go('notfound');
  });
  /*root.safeApply = function (fn) {
      var phase = this.$root.$$phase;
      if (phase == '$apply' || phase == '$digest') {
          if (fn && (typeof (fn) === 'function')) {
              fn();
          }
      } else {
          this.$apply(fn);
      }
  };*/


  // 业务逻辑
  var init = function(){
    var adminDao = resource('/admin');
    // 获取当前管理员信息
    adminDao.invoke('current').then(function(result){
        var admin = result.data || {};
        root.admin =  admin;
    });
  };
  init();

}]);






