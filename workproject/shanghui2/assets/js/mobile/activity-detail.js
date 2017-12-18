$(function(){
    $('#join-form').on("submit",function(){
        $('.join-activity').text("审核中");
        $(".join-activity").attr("disabled","disabled");
        $(".join-activity").css("backgroundColor","gray");
    });
    // 加载评论
    var infiniteLoading = $(".infinite-loading").infiniteLoading({parseData:function(list){
        for(var i=0; i<list.length; i++){
            list[i].userId = serverData.userId;
        }
    }});
    infiniteLoading.find(".end-text").text("没有更多评论了！")
    // 提交评论
    $(".comment-form").on("success",function(event,res){
        $(this).find('.comment').val("");
        console.log(res,event);
        $("html,body").animate({scrollTop:$(".comment").offset().top},'fast')
        $.get("../api/mobile/activity/getComment.json",{"commentId":res.info}).done(function(res){
            var data = res.data;
            data.userId = serverData.userId;
            console.log(data.userId)
            var html = template("comment-list",data);
            $(".comment-list").prepend(html);
            $(".comment-count").text(parseInt($('.comment-count').text()) + 1);
        })
    })

    // 如果是登录者自己的发的评论可以删除
    $('.comment').on('click','.delete-Comment',function(){
        var commentId = $(this).closest('.comment-item').data("id");
        dm.confirm("确定删除该条评论?",function(){
            dm.get('../api/mobile/activity/delete.json',{commentId:commentId,commentUserId:serverData.userId}).done(function(){
                $(".comment-list").html("");
                infiniteLoading.find('form').find("[data-name=offset]").val(1);
                infiniteLoading.infiniteLoading("reset"); 
            }) 
        })    
    })

    // 展开全部
    $(".expand-list").on('click', function(event) {
        event.preventDefault();
        var height = $(".member-list").height() + 25;
        if($(this).hasClass("expanded")){
            $(this).removeClass("expanded");
            $(this).find('span').text("展开全部");
            $(".member-content").stop(true, false).animate({"maxHeight": "230px","height":"auto"});
        }else{
            $(".member-content").stop(true, false).animate({"maxHeight": height,"height": height});
            $(this).find('span').text("收起列表");
            $(this).addClass("expanded");
        }
    });

    // 求助详情图片点击
    $(".m-help-detail").on("click",".images-list .image-item",function(){
        var currentImg = $(this).find("img").attr("src");
        var images = $(this).closest(".images-list").find("img");
        var imgArr = [];
        $.each(images,function(){
            var image = $(this).attr("src");
            imgArr.push(image);
        })
        wx.previewImage({
            current : currentImg,
            urls : imgArr
        })
    })
})