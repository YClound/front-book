$(function(){
    // 下拉菜单的实现
    var $wrap = $(".header-nav-profile"),
        $list = $wrap.find('.zu-nav-dropdown');
    $wrap.mouseover(function(event) {
        /* Act on the event */
        $list.show();
    });
    $wrap.mouseout(function(event) {
        /* Act on the event */
        $list.hide();
    });

    $(".feed-item").each(function(){
        var $this = $(this);
        var floowTopic = $this.find('.floow-topic'),
            floowProblem = $this.find('.floow-problem'),
            thank = $this.find('.thank'),
            feedMeta = $this.find('.feed-meta');


        // 关注话题
        floowTopic.on('click', function(event) {
            event.preventDefault();
            var topicText = $(".floow-topic:contains('关注话题')");
            console.log(topicText);
            if(topicText.length == 1){
                $(".floow-topic").text("取消关注");
            }else{
                $(".floow-topic").text("关注话题");
            }
        });

        
        //关注问题
        floowProblem.on("click",function(event){
            event.preventDefault();
            var problemText = $(".floow-problem:contains('关注问题')");
            console.log(problemText);
            if(problemText.length == 1){
                $(".floow-problem span").text("取消关注");
                $(".floow-problem i").hide();
            }else{
                $(".floow-problem span").text("关注问题");
                $(".floow-problem i").show();
            }
        })

        // 感谢
        thank.on("click",function(event){
            event.preventDefault();
            var thankText = $(".thank span").text();
            console.log(thankText);
            if(thankText == "感谢"){
                $(".thank span").text("取消感谢");
            }else{
                $(".thank span").text("感谢");
            }
        });


        // 不感兴趣页面显示和隐藏
        feedMeta.find('a.ignore').on("click",function(){
            $this.find('.dislike-option').show();
            $this.find('.feed-item-inner').hide();
        });
        $this.find('.dislike-option .revert').on("click",function(){
            $this.find('.dislike-option').hide();
            $this.find('.feed-item-inner').show();
        })
    });

});