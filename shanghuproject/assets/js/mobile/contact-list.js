var filterHelper = function () {
    var $wrap, $header, $content, $form;
    var bindEvent = function () {
        var nameId;
        // 收起列表
        $wrap.on('fold', function () {
            $wrap.removeClass('unfold');
            $header.children().removeClass('unfold');
            $content.children().hide();
        });

        //点击列表重置样式
        var resetMenuStyle =function(){
            var $ul = $content.children("ul");
            // 筛选条件不组合
            $content.find("li").removeClass("active");
            $content.find("a").removeClass("active");
            $(".home-name").removeClass('active');
            $(".home-name").parent(".filter-title").removeClass("show");
            $ul.each(function(){
                $(this).find("li").eq(0).addClass("active");
            });
            $content.find("a").eq(0).addClass("active");
            $(".home-county").hide();
            $(".home-district").hide();
        }
        //点击头部标题
        $header.on('click', '.filter-title', function (e) {
            e.stopPropagation();
            var $title = $(this);
            $('.filter-title').removeClass('active');
            $content.find('.active').each(function () {
                var $li = $(this);
                console.log($li.index())
                if ($li.index()) {
                    $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').find('i').addClass('show');
                }else{
                    $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').find('i').removeClass('show'); 
                }
            });
            if($(".home-city").find(".active").index() == 0){
                $header.find("[data-name=home]").find('.filter-name').closest('.filter-title').find('i').removeClass('show'); 
            }
            if ($title.hasClass('unfold')) {
                $wrap.trigger('fold');
            } else {
                $(this).addClass('active');
                $wrap.addClass('unfold');
                $title.addClass('unfold').siblings().removeClass('unfold');
                $content.find('[data-name=' + $title.data('name') + ']').show().siblings().hide();
                $(this).find(".if").addClass("show");
            }
        });

        // 点击列表
        $content.on('click', '.liClick', function (e) {
            e.stopPropagation();
            var $li = $(this), 
                key = $li.parent().data('name');
            resetMenuStyle();
            $wrap.trigger('fold');

            $(this).siblings().removeClass("active");
            $(this).addClass('active');

            highlight();
            // 表单隐藏域赋值
            $form.find(".filter-type").val("");
            $form.find('[name=' + key + ']').val($li.data("value"));
            $('.filter-title').removeClass('active');
            // 点击之后重新加载通讯录列表
            $(".contact-list").html("");
            $(".infinite-loading").find("[data-name=offset]").val(1);
            $(".infinite-loading").infiniteLoading("reset");
        });
    };

    var highlight = function () {
        $.each(["职务","老家","行业","分组"],function(i,e){
                $(".filter-name").eq(i).text(e);
            })
        $content.find('.active').each(function () {
            var $li = $(this);
            if ($li.index()) {
                $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').text($li.text());
                $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').addClass('show');
                $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').find('i').addClass('show');
            }else{
                $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').removeClass('show');
                $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').find('i').removeClass('show'); 
            }
        })
    };

    return {
        init: function () {
            $wrap = $('.s-filter');
            $header = $wrap.find('.header');
            $content = $wrap.find('.content-wrap');
            $form = $('.ajax-form');
            if (!$form.length) return;
            $form[0].reset();
            bindEvent();
        }
    };
}();

//获取老家列表
var homeSelect = function(){
    // 重置筛选头部
    var formValue;
    var proValue = "浙江省,";
    var homeHighlight = function(){
        $.each(["职务","老家","行业","分组"],function(i,e){
            $(".filter-name").eq(i).text(e).parent(".filter-title").removeClass("show");
            $(".filter-name").eq(i).text(e).parent(".filter-title").find("i").removeClass("show");
        })
        var index = $(".home-city").find(".active").index();
        var text;
        if(index){
            text = $(".home-district").find(".active").text() || $(".home-county").find(".active").text() || $(".home-city").find(".active").text();
            $(".home-name").parent(".filter-title").addClass("show");
        }else{
            text = "老家";
            $(".home-name").parent(".filter-title").removeClass("show");
        }
        $(".home-name").text(text);
    }
    // 重置筛选条件的样式
    var resetStyle = function(){
        $('.content-wrap').find("li").removeClass("active");
        $('.content-wrap ul').each(function(){
            $(this).find("li").eq(0).addClass("active");
        });
        $('.s-filter').trigger('fold');
        $(".home-name").closest('.filter-title').siblings('.filter-title').removeClass('active');
        homeHighlight();
    }
    //重置数据滚动加载
    var resetLoading = function(){
        $(".ajax-form").find(".filter-type").val("");
        $("[name=home]").val(formValue);
        $(".contact-list").html("");
        $(".infinite-loading").find("[data-name=offset]").val(1);
        $(".infinite-loading").infiniteLoading("reset");
    }
    //表单隐藏域传参
    return{
        init: function(){
            var coutyValue;
            var cityValue;
            // 获取市级列表
            var cityTempl = "<a class='db active pt-3 pb-3 city' href='javascript:;' data-value='' data-id='0'>不限</a>";
            $.each(ChineseDistricts['330000'],function(name,value){
                cityTempl += "<a class='db pt-3 pb-3 city' href='javascript:;' data-value='"+value+"' data-id='"+name+"'>"+value+"</a>" 
            })
            $(".home-city").append(cityTempl);

            // 获取县级列表
            $(".home-city").on("click",".city",function(){
                var value = $(this).data("id");
                var countyTempl = "";
                var index = $(this).index();
                $(this).addClass("active");
                $(this).siblings(".city").removeClass("active");
                if(value){
                    $.each(ChineseDistricts[value],function(name,value){
                       countyTempl += "<a class='db pt-3 pb-3 county' href='javascript:;' data-value='"+value+"' data-id='"+name+"'>"+value+"</a>" 
                    });
                }else{
                    $(".home-county").hide();
                }
                
                //判断是否是第一行
                if(index){
                    $("[data-name=home]").find(".filter-name").text($(this).text()).addClass('active');
                    $(".home-county").show();
                    cityValue = $(this).data("value");
                    formValue = proValue + cityValue;
                }else{
                    $('.content-wrap').find("a").removeClass("active");
                    $('.content-wrap').find("a").eq(0).addClass("active");
                    $(".home-name").addClass('active').text("老家");
                    $(".home-county").html("");
                    $(".home-name").parent(".filter-title").removeClass("active");
                    formValue="";
                    resetStyle();
                    resetLoading();
                }
                $(".home-district").html("");
                $(".home-district").hide("");
                $(".home-county").html(countyTempl); 
            })

            //获取区级列表
            $(".home-county").on("click",".county",function(){
                $(this).addClass("active");
                $(this).siblings(".county").removeClass("active");
                var value = $(this).data("id");
                var districtTempl = "";
                coutyValue = $(this).data("value");
                formValue = proValue + cityValue +','+ coutyValue;
                $("[data-name=home]").find(".filter-name").text($(this).text());
                if(value){
                    $.each(ChineseDistricts[value],function(i,json){
                        districtTempl += "<a class='db pt-3 pb-3 district' data-value='"+json.text+"' href='javascript:;' data-id='"+json.id+"'>"+json.text+"</a>";
                    });
                    $(".home-district").html(districtTempl);
                    $(".home-district").show();
                }else{
                    $(".home-district").hide();
                    $(".home-district").html("")
                } 
            })

            //点击区级列表
            $(".home-district").on("click",".district",function(){
                var districtValue = $(this).data("value");
                formValue = proValue + cityValue +','+ coutyValue +','+districtValue;
                $(this).addClass("active");
                $(this).siblings(".district").removeClass("active");
                $("[data-name=home]").find(".filter-name").text($(this).text());
                resetStyle();
                resetLoading();
            })

            // 点击确定按钮
            $(".confirm-choose").on("click",function(){
                resetStyle();
                resetLoading();
            })

            //点击取消按钮
            $(".cancle-choose").on("click",function(){
                $('.s-filter').trigger('fold');
                $('.content-wrap').find("a").removeClass("active");
                $('.content-wrap').find("a").eq(0).addClass("active");
                $(".home-name").parent(".filter-title").removeClass('active').removeClass("show");
                $(".home-name").text("老家")
                $(".home-county").hide();
                $(".home-district").hide();
                $(".ajax-form").find("[name=home]").val("");
            })

        }
    }
}();

$(function () {
    // 加载职位、分组、行业列表
    var name = ['job','group'];
    // console.log(name);
    $.each(name,function(e,name){
        dm.get("../api/mobile/"+name+".json",{"type" : name}).done(function(json) {
            // console.log(json);
            var html = "";
            $.each(json.data,function(i,e){
                if(!html){
                    html += "<li class='active pt-3 pb-3 liClick' data-value=''>"+e.info+"</li>"
                }else{
                   html += "<li class='pt-3 pb-3 liClick' data-value='"+e.info+"'>"+e.info+"</li>" 
                }
            })
            var wrap = $(".content-wrap").find("[data-name=" + name +"]");
            var $li = wrap.find('li');
            if($li.length == 0){
                wrap.append(html);
            }
        }); 
    })

    // 获取行业列表
    var tradeHtml = "<li class='active pt-3 pb-3 liClick' data-value=''>不限</li>";
    $.each(trade,function(i,e){
        tradeHtml += '<li class="pt-3 pb-3 liClick" data-value="'+e.value+'">'+e.registerTrade+'</li>';
    });
    $(".content-wrap ").find("[data-name=industry]").append(tradeHtml);


    filterHelper.init();
    $('.s-filter').headroom();

    //加载老家列表
    homeSelect.init();

    // 滚动加载
    var infiniteLoading = window.infiniteLoading = $(".infinite-loading").infiniteLoading({parseData:function(list){
        for(var i=0;i<list.length;i++){
            var groupName = list[i].groupStr || "";
            list[i]._groupName = groupName.split(",");
            // console.log(groupName)
        }
    }});
    // 搜索关键字
    var formPage = infiniteLoading.find('form');
    var formSearch = $(".form-search");
    var headerResult = $(".s-header-result");
    // 搜索展示结果
    var showResult = window.showResult = function(){
        dm.router.go("");
        $(".contact-list").html("");
        formPage.find("[data-name=offset]").val(1);
        infiniteLoading.infiniteLoading("reset");
        // $("html,body").animate({scrollTop:0},"fast",function(){
        //     dm.router.go("");
        // });
        // formPage.submit();      
    };
    // 搜索关键字
    var searchKeyword = window.searchKeyword = function(keyword){
        formPage.find('[name=keyword]').val(keyword);
        headerResult.removeClass('dn').find('.keyword').html(keyword);
        showResult();
    };
    var resetFilter = function(){
        // // 清空筛选条件
        // $.each(["职务","老家","行业","分组"],function(i,e){
        //     $(".filter-name").eq(i).text(e).parent(".filter-title").removeClass("show").removeClass('active');
        // })
        $(".filter-type").val("");
        // $('.content-wrap').find("a").removeClass("active");
        // $('.content-wrap').find("a").eq(0).addClass("active");
        // $(".home-county").hide();
        // $(".home-district").hide();
        // $('.content-wrap').find("li").removeClass('active');
        // $(".content-wrap ul").each(function(){
        //     $(this).find("li").eq(0).addClass("active")
        // })
    }
    dm.router(['/search'],function(path){
        dm.view.show(path.replace(/^\//, ''));
    });

    //搜索表单提交
    formSearch.on("submit", function(event){
        event.preventDefault();
        var keyword = formSearch.find('[name=keyword]').blur().val();
        formSearch[0].reset();
        resetFilter();
        searchKeyword(keyword);
        pushSearchHistory(keyword);
        $(".s-filter").hide();
        $(".contact-content").css("paddingTop","10px");
    });

    // 取消搜索
    $(".btn-cancel-search").on("click",function(){
        location.reload();
    });
    //存储数据
    var pushSearchHistory = function (val) {
        if (val == null || val == '') return;
        var searchHistory = store('searchHistory') || []
        var index = searchHistory.indexOf(val)
        if (index > -1) {
            searchHistory.splice(index, 1)
        }
        searchHistory.push(val)
        store('searchHistory', searchHistory)
        showSearchHistory()
    };
    // 显示搜索历史列表
    var showSearchHistory = function () {
        var html = '',
        history = store('searchHistory') || []
        for (var i = history.length; i > 0; i--) {
            html += '<li class="pt-6 pb-6 bb c-b">' + history[i - 1] + '</li>'
        }
        $('.search-history-list').html(html)
    };

    showSearchHistory();
    //清除历史列表
    $('.clear-search-history').click(function () {
        store('searchHistory', null)
        showSearchHistory()
    });
    // 点击历史列表
    $('.search-history-list').on('click', 'li', function () {
        var keyword = $(this).text()
        formSearch.find('[type="search"]').val(keyword);
        searchKeyword(keyword);
        resetFilter();
        $(".s-filter").hide();
        $(".contact-content").css("paddingTop","10px");
    });

})
