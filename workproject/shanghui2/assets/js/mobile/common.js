/*
    全局 js 
    serverData 后台服务器赋值对象
 */
 $(function(){

    var serverData = window.serverData || {};
    var user = serverData.user || {};
    var memberType  = user.memberType;

    // 需要同乡或者企业家认证才可以查看一些功能
    $(document).on('click','.auth-link',function(e){
        var link = $(e.currentTarget),href = link.attr('href');
        e.preventDefault();
        e.stopPropagation();
        //console.log(1111111,memberType)
        if('2' == memberType || '3' == memberType) {
            location.href = href;
        }else {
            dm.confirm('需要同乡认证才能查看',{yes:'立刻认证',no:'暂不认证'},function(){
                location.href = 'village-register.html'
            })
        }

    });

    
 });
    