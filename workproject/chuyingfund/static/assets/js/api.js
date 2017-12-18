var API = api = {
      // 身份城市接口
      cities:'/static/assets/data/city.json',
      // 收藏
      followproject:'/static/api/followproject.html',
      // 申请查看项目
      applyCheckProject:'/api/project/applyCheck.html',
      // 文件上传地址
      upload:'/api/upload.html',
      sms:'/sms/getSMSing.html',
      ucLogin: "/member/login?appid=chuying&redirect_url=" + encodeURIComponent(location.href),
      ucRegister:"/register/registerPhone.html",
      ucLogout: '/user/logout?callback=?',
      userProjectList: "/static/assets/data/userProjectList.json",
      userProjectRecommend: "/static/assets/data/userProjectRecommend.json"
};