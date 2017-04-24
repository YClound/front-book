
var SideMenuStates = (function(SideMenu){
  var result ={};
  for(var i =0;i<SideMenu.length;i++){
    var panel =  SideMenu[i];
    var node = panel.node;
    var list = panel.module;
  
    for(var j = 0;j<list.length;j++){
      var obj = list[j];


      if(!obj.controller){
        obj.controller = ['$scope',function(scope){}];
      }
      if(obj.templateUrl) {
        obj.templateUrl = obj.templateUrl + '?' + new Date().valueOf();
      }
      if(obj.controllerUrl || obj.dependency){
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

  }
  console.log('SideMenuStates',result)
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
  "demos.bootstrap":{
    url:'/bootstrap',
    templateUrl: '../assets/demos/bootstrap.html',
    controller: ['$scope', '$state',
      function ($scope, $state) {
        console.log('bootstrap');
        
      }
    ]
  },
  
  "home":{
    url:'/home',
    templateUrl: '../assets/tpls/home.html',
    controller: ['$scope', '$state',
      function ($scope, $state) {
        console.log('home');
        $scope.startDate  = '';
        $scope.endDate  = '';
        
      }
    ]
  }
};
/*
controller: ['$scope', '$state',
      function ($scope, $state) {
        console.log('umeditor controller')
      
        $scope.nothing = 'xx';
        $scope.all_config = {
          initialFrameHeight:200,
          toolbar:[
            'source | undo redo | bold italic underline strikethrough | superscript subscript | forecolor backcolor | removeformat |',
            'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize' ,
            '| justifyleft justifycenter justifyright justifyjustify |',
            'link unlink |  image  ',
            '| horizontal print preview  fullscreen', 'drafts', 'formula'
          ]
        };
        
      }
    ]*/
angular.extend(window.STATES,SideMenuStates);
console.log('window.STATES',window.STATES)
// APP
var app = angular.module('awApp', [
    'ngRoute', 
    'ui.router',
    'oc.lazyLoad',
    'angular-core'
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
app.value('MEMBER_TYPE',[{'id':1,name:'游客'},{'id':2,name:'同乡'},{'id':3,name:'企业家'}]);
app.value('APPLY_STATUS',[{'id':0,name:'待审核'},{'id':1,name:'已通过'},{'id':2,name:'已拒绝'}]);
app.value('CompanyScaleList', [{'id':'少于50人',name:'少于50人'},{'id':'50-150人',name:'50-150人'},{'id':'150-500人',name:'150-500人'},{'id':'500-1000人',name:'500-1000人'},{'id':'1000-5000人',name:'1000-5000人'},{'id':'5000-10000人',name:'5000-10000人'},{'id':'10000以上',name:'10000以上'}]);
app.value('INDUSTRY',[{"value": "教育", "registerTrade": "教育"}, {"value": "采矿业", "registerTrade": "采矿业"}, {"value": "制造业", "registerTrade": "制造业"}, {"value": "金融业", "registerTrade": "金融业"}, {"value": "建筑业", "registerTrade": "建筑业"}, {"value": "批发和零售业", "registerTrade": "批发和零售业"}, {"value": "房地产业", "registerTrade": "房地产业"}, {"value": "国际组织", "registerTrade": "国际组织"}, {"value": "住宿和餐饮业", "registerTrade": "住宿和餐饮业"}, {"value": "卫生和社会工作", "registerTrade": "卫生和社会工作"}, {"value": "农、林、牧、渔业", "registerTrade": "农、林、牧、渔业"}, {"value": "租赁和商业服务业", "registerTrade": "租赁和商业服务业"}, {"value": "文化、体育和娱乐业", "registerTrade": "文化、体育和娱乐业"}, {"value": "交通运输、仓储和邮政业", "registerTrade": "交通运输、仓储和邮政业"}, {"value": "科学研究和技术服务业", "registerTrade": "科学研究和技术服务业"}, {"value": "信息传输、软件和信息技术服务业", "registerTrade": "信息传输、软件和信息技术服务业"}, {"value": "居民服务、修理和其他服务业", "registerTrade": "居民服务、修理和其他服务业"}, {"value": "水利、环境和公共设施管理业", "registerTrade": "水利、环境和公共设施管理业"}, {"value": "公共管理、社会保障和社会组织", "registerTrade": "公共管理、社会保障和社会组织"}, {"value": "电力、热力、燃气及水生产和供应业", "registerTrade": "电力、热力、燃气及水生产和供应业"}]);
// 正式执行
app.run(["$rootScope","$state","resource" ,"$templateCache","CompanyScaleList","INDUSTRY",function(root,state,resource,$templateCache,CompanyScaleList,INDUSTRY) {
  var adminDao = resource('/admin');

  angular.forEach(SideMenu,function(val,key){
    var list = val.list || [];
    val.auth = val.auth || '';

  })
  root.SideMenu = SideMenu;
  // 当前用户信息
  root.currentUser = window.serverData.currentUser;
  root.CompanyScaleList = CompanyScaleList;
  root.INDUSTRY = INDUSTRY;
  root.serverData = window.serverData;
  console.log('root.currentUser',root.currentUser)
  // 获取当前管理员信息
  adminDao.invoke('current').then(function(result){
      var admin = result.data || {};
      root.admin =  admin;
  });
  
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
      //$templateCache.removeAll();
      var name = toState.name;
      var obj;
      var menu = $('.sidebar a[ui-sref="'+name+'"]');
      if(menu.length>0){
        $('.sidebar li').removeClass('active');
        menu.closest('li').addClass('active');
        // 假如连接本身不是父级，则向上寻找展开
        if(!menu.siblings('.nav-second-level ').length){
          var $plink = menu.closest('ul').parent().find('.link-collapse');
          if(!$plink.hasClass('expanded')){
            $plink.trigger('click');
          }
        }
          
      }else {
        $('.sidebar li').removeClass('active');
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
}]);
// 配置路由
app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      
      angular.forEach(window.STATES,function(value,key){
          console.log(key,value);
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





