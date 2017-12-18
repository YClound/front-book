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
    // 后台根目录
    var root2 = getRootPath_web();
    var index = location.href.indexOf('/zzz');
    if(location.href.indexOf('localhost')>-1){
        //root2 = location.href.substring(0,index);
    }
    //console.log(root2,index)
    window.API = {
        host:'http://' + location.host,
        root:root2,
        demo:'/api',
        mode:'server',
        // 上传路径请修改为真实绝对路径
        upload:'/fileyun/api/editorUpload',
        map:{
            
        }
    };
    
})();
    