$(function(){
    // 自动加载
    $(".infinite-loading").infiniteLoading();
    // 删除信息
    $(document).on("click",".delete-info",function(){
        var item = $(this).closest(".message-item");
        var dataId = item.data("id");
        console.log(dataId);
        dm.confirm("是否删除该信息",function(){
           dm.get("../api/mobile/delete.json",{"id" : dataId}).done(function(){
                item.remove();
                var $item = $(".my-message").find(".message-item").length;
                if(!$item){
                    $(".my-message").attr("data-content","0");
                }
            });
        }); 
    });
})