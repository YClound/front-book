/*
    全局 api 配置 
    请求地址:version + api
    @params
        demo:'统一本地资源前缀'
        host:'http://api.igrow.com'
        version:''  
        mode: 'server' or 'demo' or '' 。 'server':全局使用服务器接口,'demo':全局使用本地模拟数据,'':根据匹配到的api具体配置
 */
(function(){
    function getRootPath_web() {
        //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
        var curWwwPath = window.document.location.href;
        //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        //获取主机地址，如： http://localhost:8083
        var localhostPaht = curWwwPath.substring(0, pos);
        //获取带"/"的项目名，如：/uimcardprj
        var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        return (localhostPaht + projectName);
    }
    var root2 = getRootPath_web();
    if(location.href.indexOf('static')>-1 && location.href.indexOf('localhost')>-1){
        root2 = root2 + '/static';
    }
    console.log(getRootPath_web())
    window.API = {
        host:'http://' + location.host,
        version:'/api/1.1b',
        root:root2,
        demo:'/api',
        mode:'server',
        map:{
            '/user/get':'',
            '/admin/current':'/current',
            '/admin/login':'/login',
            '/admin/logout':'/logout',
            '/admin/create':'/admin/sys/createSysuser',
            '/admin/list':'/admin/sys/sysuserlist',
            '/admin/get':'/admin/sys/getSysUser',
            '/admin/update':'/admin/sys/editSysUser',
            '/admin/delete':'/admin/sys/delSysUser',
            '/adminrole/create':'/admin/sysrole/create',
            '/adminrole/list' :'/admin/sysrole/list',
            '/adminrole/get' : '/admin/sysrole/get',
            '/adminrole/update' : '/admin/sysrole/update',
            '/adminrole/delete' : '/admin/sysrole/',
            '/user/list':'/admin/user/list',
            '/user/lock':'/admin/user/lock',
            '/user/edit':'/admin/user/edit',
            '/user/del':'/admin/user/del',
            '/module/list':'/admin/module/list',
            '/module/lock':'/admin/module/lock',
            '/module/edit':'/admin/module/edit',
            '/module/del':'/admin/module/del',
            '/module/get':'/admin/module/get'
        }
    };
    
})();
    