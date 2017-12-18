// 左侧菜单
window.SideMenu = [
 {
    node:{
      icon:"fa fa-cog",
      text:"系统帐号",
      link:"admin.list"
    },
    list:[]
  },
  {
    node:{
      icon:"fa fa-user",
      text:"老师帐号",
      link:"teacher.list"
    },
    list:[]
  },
  {
    node:{
      icon:"fa fa-bank",
      text:"电子题库",
      link:"bank.list"
    },
    list:[]
  },
  {
    node:{
      icon:"fa fa-edit",
      text:"文章管理",
      link:"article.list"
    },
    list:[]
  },
  // {
  //   node:{
  //     icon:"fa fa-newspaper-o",
  //     text:"作业库",
  //     link:"homework"
  //   },
  //   list:[
  //   ]
  // },
  {
    node:{
      icon:"fa fa-paragraph",
      text:"系统设置"
    },
    list:[
      {
        text:"学科设置",
        link:"subject.list"
      },
      {
        text:"题型设置",
        link:"questiontype.list"
      },
      // {
      //   text:"版式设置",
      //   link:"layout.list"
      // },
      {
        text:"大纲设置",
        link:"syllabus.set"
      }
    ]
  }
  // {
  //   node:{
  //     icon:"fa fa-user",
  //     text:"权限管理"
  //   },
  //   list:[
  //     {
  //       text:"管理员",
  //       link:"admin.list"
  //     }
  //   ]
  // }
];
window.SideModule = [
	{
	      name:'admin',
	      url:'/admin',
	      abstract:true,
	      template:'<div ui-view></div>'
	    },
	    {
	      text:'系统帐号',
	      name:'admin.list',
	      url:'/list',
	      templateUrl:'/module/admin/admin-list.html',
	      controller:'articleListCtrl',
	      controllerUrl:'/module/admin/adminListCtrl.js'
	    },
	    {
	      text:'帐号编辑',
	      name:'admin.edit',
	      url:'/edit/{id}',
	      templateUrl:'/module/admin/admin-edit.html',
	      controller:'articleEditCtrl',
	      controllerUrl:'/module/admin/adminEditCtrl.js'
	    },
	    {
		      name:'teacher',
		      url:'/teacher',
		      abstract:true,
		      template:'<div ui-view></div>'
		    },
		    {
		      text:'老师帐号',
		      name:'teacher.list',
		      url:'/list',
		      templateUrl:'/module/teacher/teacher-list.html',
		      controller:'articleListCtrl',
		      controllerUrl:'/module/teacher/teacherListCtrl.js'
		    },
		    {
		      text:'帐号编辑',
		      name:'teacher.edit',
		      url:'/edit/{id}',
		      templateUrl:'/module/teacher/teacher-edit.html',
		      controller:'articleEditCtrl',
		      controllerUrl:'/module/teacher/teacherEditCtrl.js'
		    },
    {
      name:'article',
      url:'/article',
      abstract:true,
      template:'<div ui-view></div>'
    },
    {
      text:'文章',
      name:'article.list',
      url:'/list',
      templateUrl:'/module/article/article-list.html',
      controller:'articleListCtrl',
      controllerUrl:'/module/article/articleListCtrl.js'
    },
    {
      text:'文章编辑',
      name:'article.edit',
      url:'/edit/{id}',
      templateUrl:'/module/article/article-edit.html',
      controller:'articleEditCtrl',
      controllerUrl:'/module/article/articleEditCtrl.js'
    },
    {
      name:'subject',
      url:'/subject',
      abstract:true,
      template:'<div ui-view></div>'
    },
    {
      name:'bank',
      url:'/bank',
      abstract:true,
      template:'<div ui-view></div>'
    },
    {
      text:'电子题库',
      name:'bank.list',
      url:'/list',
      templateUrl:'/module/bank/bank-list.html',
      controller:'bankCtrl',
      controllerUrl:'/module/bank/bankCtrl.js'
    },
    {
      text:'创建题目',
      name:'bank.edit',
      url:'/edit/{subjectId}/{questionId}',
      templateUrl:'/module/bank/bank-edit.html',
      controller:'bankEditCtrl',
      controllerUrl:'/module/bank/bankEditCtrl.js'
    },
    {
      text:"学科管理",
      name:'subject.list',
      url:'/list',
      templateUrl:'/module/subject/subject-list.html',
      controller:'subjectCtrl',
      controllerUrl:'/module/subject/subjectCtrl.js'
    },
    {
      name:'questiontype',
      url:'/questiontype',
      abstract:true,
      template:'<div ui-view></div>'
    },
    {
      text:"题型设置",
      name:'questiontype.list',
      url:'/list',
      templateUrl:'/module/questionType/questionType-list.html',
      controller:'questionTypeCtrl',
      controllerUrl:'/module/questionType/questionTypeCtrl.js'
    },
    {
      name:'syllabus',
      url:'/syllabus',
      abstract:true,
      template:'<div ui-view></div>'
    },
    {
      text:"大纲设置",
      name:'syllabus.set',
      url:'/set',
      templateUrl:'/module/syllabus/syllabus-set.html',
      controller:'syllabusSetCtrl',
      controllerUrl:'/module/syllabus/syllabusSetCtrl.js'
    },
    {
      text:"会员设置",
      name:'member.set',
      url:'/set/{id}',
      templateUrl:'/assets/tpls/member/member-set.html',
      controller:'memberSetCtrl',
      controllerUrl:'/assets/js/module/member/memberSetCtrl.js'
    }
  ];