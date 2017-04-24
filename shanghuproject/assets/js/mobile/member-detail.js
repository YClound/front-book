$(function(){
    $(".tab-title").tab2();

    $("body").find('form').on('success',function(e,res){
        $('#myModal').modal('hide');
        dm.alert('信息发送成功!');
    });

    // 判断是否含有历史路径
    if(document.referrer == ""){
        $(".s-header").find("a").removeAttr('onclick').attr("href","index.html");
    }else{
        $(".s-header").find("a").attr('onclick',"javascript:history.back()").attr("href","javascript:;");
    }
})