$(function(){
    // 搜索页面
    dm.router(["/search"],function(path){
        dm.view.show(path.replace(/^\//, ''))
    });
    // 搜索框获取焦点
    $(".view-search").on("shown",function(){
        $(".view-search").find("input[type=search]").focus();
    });

    // 会员列表自动加载 传入的参数处理
    // $(".infinite-loading").infiniteLoading({parseData:function(list){
    //     for(var i=0;i<list.length;i++){
    //         console.log(list[i]);
    //     }
    // }});
    var infiniteLoading = window.infiniteLoading = $(".infinite-loading");
    $(".infinite-loading").infiniteLoading({parseData:function(list){
        for(var i=0;i<list.length;i++){
            var groupName = list[i].groupName || "";
            list[i]._groupName = groupName.split(",");
            console.log(groupName)
        }
    }});

    // 搜索关键字
    var formPage = infiniteLoading.find('form');
    var formSearch = $(".form-search");
    var headerResult = $(".s-header-result");
    // 搜索展示结果
    var showResult = window.showResult = function(){
        dm.router.go("");
        $(".member-list").html("");
        formPage.find("[data-name=offset]").val(1);
        infiniteLoading.infiniteLoading("reset");
        
        /*$("html,body").animate({scrollTop:0},"fast",function(){
            
               
        });*/
              
    };
    // 搜索关键字
    var searchKeyword = window.searchKeyword = function(keyword){
        formPage.find('[name=keyword]').val(keyword);
        headerResult.removeClass('dn').find('.keyword').html(keyword);
        showResult();
    };

    formSearch.on("submit", function(event){
        event.preventDefault();
        var keyword = formSearch.find('[name=keyword]').blur().val();
        formSearch[0].reset();
        searchKeyword(keyword);
        publishSearchHistory(keyword);
    });

    // 取消搜索
    $(".btn-cancel-search").on("click",function(){
        var inputSearch = formSearch.find('[name=keyword]'),
            inputKeyword = formPage.find('[name=keyword]');
        inputSearch.val("");
        inputKeyword.val("");
        $(".member-list").html("");
        formPage.find('[data-name=offset]').val(1);
        infiniteLoading.infiniteLoading('reset');
        headerResult.addClass('dn');
    });

    // 保存数据
    var  publishSearchHistory = function(val){
        if(val == null || val == "") return;
        var searchHistoryMember = store("searchHistoryMember") || [];
        var index = searchHistoryMember.indexOf(val);
        console.log(index)
        if(index > -1){
            searchHistoryMember.splice(index,1);
        }
        searchHistoryMember.push(val);
        store("searchHistoryMember",searchHistoryMember);
        showSearchHistory();
    }

    // 展示保存的数据
    var showSearchHistory = function(){
        var html = "",
            history = store("searchHistoryMember") || [];
        for (var i = history.length - 1; i >= 0; i--) {
            html += '<li class="pt-6 pb-6 bb c-b">' + history[i] + '</li>';
        }
        $(".search-history-list").html(html);
    }

    showSearchHistory();

     //清除列表
    $(".clear-search-history").on("click",function(){
        store("searchHistoryMember",null);
        showSearchHistory();
    })

    // 点击搜索历史列表
    $(".search-history-list").on("click","li",function(){
        var val = $(this).text();
        formSearch.find("[type=search]").val(val);
        searchKeyword(val);
        console.log("11111111");
    })
   

})