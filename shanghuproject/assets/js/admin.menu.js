// 左侧菜单
var SideMenu = [
  {
    node:{
      icon:"fa fa-paragraph",
      text:"商会设置"
    },
    module:[
      {
        text:"商会简介",
        name:'commerce.intro',
        url:'/intro',
        templateUrl:'../assets/tpls/commerce/commerce-intro.html',
        controller:'commerceIntroCtrl',
        controllerUrl:'../assets/js/module/commerce/commerceSetCtrl2.js',
        dependency:['../vendor/umeditor/umeditor.min.js']
      },
      {
        text:"商会章程",
        name:'commerce.rules',
        url:'/rules',
        templateUrl:'../assets/tpls/commerce/commerce-rules.html',
        controller:'commerceRulesCtrl',
        controllerUrl:'../assets/js/module/commerce/commerceSetCtrl2.js',
        dependency:['../vendor/umeditor/umeditor.min.js']
      },
      {
        text:"组织架构",
        name:'commerce.organization',
        url:'/organization',
        templateUrl:'../assets/tpls/commerce/commerce-organization.html',
        controller:'commerceOrganizationCtrl',
        controllerUrl:'../assets/js/module/commerce/commerceSetCtrl2.js',
        dependency:['../vendor/umeditor/umeditor.min.js']
      }
      
    ],
    list:[
      {
        text:"商会简介",
        link:"commerce.intro"
      },
      {
        text:"商会章程",
        link:"commerce.rules"
      },
      {
        text:"组织架构",
        link:"commerce.organization"
      },
    ]
  },
  {
    node:{
      icon:"fa fa-users",
      text:"会员管理",
      link:"member.list"
    },
    module:[
      {
        name:'member',
        url:'/member',
        abstract:true,
        template:'<div ui-view></div>'
      },
      {
        text:"用户管理",
        name:'member.list',
        url:'/list',
        templateUrl:'../assets/tpls/member/member-list.html',
        controller:'memberCtrl',
        controllerUrl:'../assets/js/module/member/memberCtrl.js'
      },
      {
        text:"会员设置",
        name:'member.set',
        url:'/set/{id}',
        templateUrl:'../assets/tpls/member/member-set.html',
        controller:'memberSetCtrl',
        controllerUrl:'../assets/js/module/member/memberSetCtrl.js'
      },
      {
        text:"会员编辑",
        name:'member.edit',
        url:'/edit/{id}',
        templateUrl:'../assets/tpls/member/member-edit.html',
        controller:'memberEditCtrl',
        controllerUrl:'../assets/js/module/member/memberCtrl.js'
      },
      {
        text:"会员其他信息",
        name:'member.more',
        url:'/more/{id}',
        templateUrl:'../assets/tpls/member/member-more.html',
        controller:'memberMoreCtrl',
        controllerUrl:'../assets/js/module/member/memberMoreCtrl.js'
      },
      {
        text:"会员创建",
        name:'member.create',
        url:'/create',
        templateUrl:'../assets/tpls/member/member-edit.html',
        controller:'memberEditCtrl',
        controllerUrl:'../assets/js/module/member/memberCtrl.js'
      }
    ],
    list:[]
  },
  {
    node:{
      icon:"fa fa-user",
      text:"审核管理"
    },
    module:[
      {
        name:'apply',
        url:'/apply',
        abstract:true,
        template:'<div ui-view></div>'
      },
      {
        name:'entrepreneur',
        url:'/entrepreneur',
        abstract:true,
        template:'<div ui-view></div>'
      },
      {
        text:"老乡审核",
        name:'apply.villager',
        url:'/villager',
        templateUrl:'../assets/tpls/villager/villager-list.html',
        controller:'villagerCtrl',
        controllerUrl:'../assets/js/module/villager/villagerCtrl.js'
      },
      {
        text:"服务组审核",
        name:'apply.service',
        url:'/apply',
        templateUrl:'../assets/tpls/apply/service-list.html',
        controller:'villagerCtrl',
        controllerUrl:'../assets/js/module/villager/villagerCtrl.js'
      },
      {
        text:"企业家审核",
        name:'entrepreneur.apply',
        url:'/apply',
        templateUrl:'../assets/tpls/entrepreneur/entrepreneur-list.html',
        controller:'entrepreneurCtrl',
        controllerUrl:'../assets/js/module/entrepreneur/entrepreneurCtrl.js'
      },
      {
        text:"企业家详情",
        name:'entrepreneur.detail',
        url:'/detail/{id}',
        templateUrl:'../assets/tpls/entrepreneur/entrepreneur-detail.html',
        controller:'entrepreneurDetailCtrl',
        controllerUrl:'../assets/js/module/entrepreneur/entrepreneurDetailCtrl.js'
      }
    ],
    list:[
      {
        text:"老乡审核",
        link:"apply.villager"
      },
      {
        text:"服务组审核",
        link:"apply.service"
      },
      {
        text:"企业家审核",
        link:"entrepreneur.apply"
      }
    ]
  },
  {
    node:{
      icon:"fa fa-newspaper-o",
      text:"资料修改审核",
      link:"change.list"
    },
    module:[
      {
        name:'change',
        url:'/change',
        abstract:true,
        template:'<div ui-view></div>'
      },
      {
        text:"修改列表",
        name:'change.list',
        url:'/list',
        templateUrl:'../assets/tpls/change/list.html',
        controller:'changeCtrl',
        controllerUrl:'../assets/js/module/change/changeCtrl.js'
      },
      {
        text:"个人资料",
        name:'change.profile',
        url:'/profile/{applyId}/{id}',
        templateUrl:'../assets/tpls/change/profile.html',
        controller:'profileCtrl',
        controllerUrl:'../assets/js/module/change/changeCtrl.js'
      },
      {
        text:"企业信息",
        name:'change.company',
        url:'/company/{id}',
        templateUrl:'../assets/tpls/change/company.html',
        controller:'companyCtrl',
        controllerUrl:'../assets/js/module/change/changeCtrl.js'
      }
    ],
    list:[
      
    ]
  },

  {
    auth:"admin",
    node:{
      icon:"fa fa-gear",
      text:"管理商会"
    },
    module:[
      {
        name:'commerce',
        url:'/commerce',
        abstract:true,
        template:'<div ui-view></div>'
      },
      {
        text:"商会成员",
        name:'commerce.member',
        url:'/member/{id}',
        templateUrl:'../assets/tpls/commerce/commerce-member.html',
        controller:'commerceMemberCtrl',
        controllerUrl:'../assets/js/module/commerce/commerceMemberCtrl.js'
      },
      {
        text:"创建商会",
        name:'commerce.create',
        url:'/create',
        templateUrl:'../assets/tpls/commerce/commerce-edit.html',
        controller:'commerceEditCtrl',
        controllerUrl:'../assets/js/module/commerce/commerceEditCtrl.js'
      },
      {
        text:"商会列表",
        name:'commerce.list',
        url:'/list',
        templateUrl:'../assets/tpls/commerce/commerce-list.html',
        controller:'commerceCtrl',
        controllerUrl:'../assets/js/module/commerce/commerceCtrl.js'
      },
      {
        text:"商会设置",
        name:'commerce.set',
        url:'/set/{id}',
        templateUrl:'../assets/tpls/commerce/commerce-edit.html',
        controller:'commerceEditCtrl',
        controllerUrl:'../assets/js/module/commerce/commerceEditCtrl.js'
      }
    ],
    list:[{
      text:"商会列表",
      link:"commerce.list"
    },{
      text:"商会创建",
      link:"commerce.create"
    }]
  },
  {
    node : {
      text : "用户列表",
      icon:"fa fa-users",
      link : 'userInfo.list'
    },
    module : [
      {
        name : 'userInfo',
        url : '/userInfo',
        template : '<div ui-view></div>',
        abstract : true
      },
      {
        name : 'userInfo.list',
        url : '/list',
        text : '用户管理',
        templateUrl : '../assets/tpls/userInfo/userInfo-list.html',
        controller : 'userInfoCtrl',
        controllerUrl : '../assets/js/module/userInfo/userInfoCtrl.js'
      },
      {
        name : 'userInfo.edit',
        url : '/edit/{id}',
        text : '用户修改',
        templateUrl : '../assets/tpls/userInfo/userInfo-edit.html',
        controller : 'userInfoEditCtrl',
        controllerUrl : '../assets/js/module/userInfo/userInfoCtrl.js'
      },
      {
        name : 'userInfo.create',
        url : '/create',
        text : '用户增加',
        templateUrl : '../assets/tpls/userInfo/userInfo-edit.html',
        controller : 'userInfoEditCtrl',
        controllerUrl : '../assets/js/module/userInfo/userInfoCtrl.js'
      }
    ],
    list : []
  }
];