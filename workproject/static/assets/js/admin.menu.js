// 左侧菜单
var SideMenu = [
  {
    node:{
      name:'admin',
      url:'/admin',
      title:'管理员模块'
    },
    list:[
      {
        name:'admin.list',
        url:'/list',
        title:'管理员列表',
        templateUrl:'../assets/tpls/admin-list.html',
        controller:'adminCtrl'
      },
      {
        name:'admin.edit',
        url:'/edit/{id}',
        title:'管理员编辑',
        templateUrl:'../assets/tpls/admin-edit.html',
        controller:'adminEditCtrl',
        display:false
      },
      {
        name:'admin.create',
        url:'/create',
        title:'管理员创建',
        templateUrl:'../assets/tpls/admin-edit.html',
        controller:'adminEditCtrl'
      }
    ]
  },
  {
    node:{
      name:'adminrole',
      url:'/adminrole',
      title:'管理员角色模块'
    },
    list:[
      {
        name:'adminrole.list',
        url:'/list',
        title:'管理员角色列表',
        templateUrl:'../assets/tpls/adminrole-list.html',
        controller:'adminroleCtrl',
        controllerUrl:'../assets/js/module/adminrole.modules.js'
      },
      {
        name:'adminrole.edit',
        url:'/edit/{id}',
        title:'管理员角色编辑',
        templateUrl:'../assets/tpls/adminrole-edit.html',
        controller:'adminroleEditCtrl',
        controllerUrl:'../assets/js/module/adminrole.modules.js',
        display:false
      },
      {
        name:'adminrole.create',
        url:'/create',
        title:'管理员角色创建',
        templateUrl:'../assets/tpls/adminrole-edit.html',
        controller:'adminroleEditCtrl',
        controllerUrl:'../assets/js/module/adminrole.modules.js'
      }
    ]
  },
  {
    node:{
      name:'user',
      url:'/user',
      title:'会员管理'
    },
    list:[
      {
        name:'user.list',
        url:'/list',
        title:'会员列表',
        templateUrl:'../assets/tpls/user-list.html',
        controllerUrl:'../assets/js/module/user.modules.js',
        controller:'userListCtrl'
      },
      {
        name:'user.edit',
        url:'/edit/{id}',
        title:'会员编辑',
        templateUrl:'../assets/tpls/user-edit.html',
        controller:'userEditCtrl',
        controllerUrl:'../assets/js/module/user.modules.js',
        display:false
        },
      {
        name:'user.create',
        url:'/edit',
        title:'会员创建',
        templateUrl:'../assets/tpls/user-edit.html',
        controllerUrl:'../assets/js/module/user.modules.js',
        controller:'userEditCtrl'
      }
    ]
  },
  {
	    node:{
	      name:'module',
	      url:'/module',
	      title:'版块管理'
	    },
	    list:[
	      {
	        name:'module.list',
	        url:'/list',
	        title:'版块列表',
	        templateUrl:'../assets/tpls/module-list.html',
	        controllerUrl:'../assets/js/module/module.modules.js',
	        controller:'moduleListCtrl'
	      },
	      {
	        name:'module.edit',
	        url:'/edit/{id}',
	        title:'编辑版块',
	        templateUrl:'../assets/tpls/module-edit.html',
	        controller:'moduleEditCtrl',
	        controllerUrl:'../assets/js/module/module.modules.js',
	        display:false
	        },
	      {
	        name:'module.create',
	        url:'/edit',
	        title:'创建版块',
	        templateUrl:'../assets/tpls/module-edit.html',
	        controllerUrl:'../assets/js/module/module.modules.js',
	        controller:'moduleEditCtrl'
	      }
	    ]
	  }
];