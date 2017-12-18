
$(function () {
    var filterHelper = function () {
        var $wrap, $header, $content, $form,title = [];
        var sociatyId;
        // 获取筛选条件的名称
        var filterTitle = $(".s-filter").find(".filter-title");
        $.each(filterTitle,function(index,elem){
            title.push($(elem).find('.filter-name').text());
        });
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
                $(".city-name").closest('.filter-title').removeClass('show');
                $header.find('[data-name="group"]').removeClass('show');
                $ul.each(function(){
                    $(this).find("li").eq(0).addClass("active");
                });
                $('.city-item').hide();
                $content.find("a").eq(0).addClass("active");
                $form.find(".filter-type").val("");
            }
            //点击头部标题
            $header.on('click', '.filter-title', function (e) {
                e.stopPropagation();
                var $title = $(this);
                $('.filter-title').removeClass('active');
                if ($title.hasClass('unfold')) {
                    $wrap.trigger('fold');
                } else {
                    if($(this).data('name') == "group" && sociatyId ==""){
                        dm.notice("请先选择商会")
                    }
                    $(this).addClass('active');
                    $wrap.addClass('unfold');
                    $title.addClass('unfold').siblings().removeClass('unfold');
                    $content.find('[data-name=' + $title.data('name') + ']').show().siblings().hide();
                }
            });

            // 点击列表
            $content.on('click', '.liClick', function (e) {
                e.stopPropagation();
                var $li = $(this), 
                    key = $li.parent().data('name');
                var type = $li.data('type');
                $wrap.trigger('fold');
                if(type == "sociaty"){
                    resetMenuStyle();
                    $(this).addClass('active');
                    $(this).siblings('li').removeClass('active');
                    sociatyId = $li.data("value");
                    loadingGroup(sociatyId);
                    highlight();
                }else if(type == 'group'){
                    $(this).addClass('active');
                    $(this).siblings('li').removeClass('active');
                    $header.find('[data-name="group"]').addClass("show").find('.filter-name').text($(this).text());
                    $form.find('[name="commerce"]').val(sociatyId);
                }else{
                    resetMenuStyle();
                    $(this).addClass('active');
                    $(this).siblings('li').removeClass('active');
                    sociatyId = '';
                    $(".content-wrap").find('[data-name="group"]').html("");
                    highlight();
                }
                // 表单隐藏域赋值
                $form.find('[name=' + key + ']').val($li.data("value"));
                $('.filter-title').removeClass('active');
                // 点击之后重新加载通讯录列表
                $(".contact-list").html("");
                $(".infinite-loading").find("[data-name=offset]").val(1);
                $(".infinite-loading").infiniteLoading("reset");
                $(window).scrollTop(100)
            });
        };
        var highlight = function () {
            $.each(title,function(i,e){
                    $(".filter-name").eq(i).text(e);
                })
            $content.find('.active').each(function () {
                var $li = $(this);
                if ($li.index()) {
                    $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').text($li.text());
                    $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').addClass('show');
                }else{
                    $header.find('[data-name=' + $li.parent().data('name') + ']').find('.filter-name').closest('.filter-title').removeClass('show');
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

    //获取城市
    var citySelect = function(){
        var title = [],index,text,formValue,proValue;
            var filterTitle = $(".s-filter").find(".filter-title");
            $.each(filterTitle,function(index,elem){
                title.push($(elem).find('.filter-name').text());
        });
        var highlight = function () {
            $.each(title,function(i,e){
                console.log(e)
                $(".filter-name").eq(i).text(e).parent(".filter-title").removeClass("show");
            });
            index = $(".province-item").find(".active").index();
            if(index){
                text = $(".city-item").find(".active").text() || $(".province-item").find(".active").text();
                $(".city-name").parent(".filter-title").addClass("show");
            }else{
                text = "城市";
                $(".city-name").parent(".filter-title").removeClass("show");
            }
            $(".city-name").text(text);
        };
        var resetStyle = function(){
            $('.content-wrap').find("li").removeClass("active");
            $('.content-wrap ul').each(function(){
                $(this).find("li").eq(0).addClass("active");
            });
            $('.s-filter').trigger('fold');
            $(".home-name").closest('.filter-title').siblings('.filter-title').removeClass('active');
            highlight();
            $(".content-wrap").find('[data-name="group"]').html("");
        }
        //重置数据滚动加载
        var resetLoading = function(){
            $(".ajax-form").find(".filter-type").val("");
            $("[name=city]").val(formValue);
            $(".contact-list").html("");
            $(".infinite-loading").find("[data-name=offset]").val(1);
            $(".infinite-loading").infiniteLoading("reset");
            $(window).scrollTop(100);
        }
        var provinceRender = function(){
            var provinceHtml = '<a class="db active pt-3 pb-3 province" href="javascript:;" data-value="" data-id="0">不限</a>'
            var ChineseProvice = window.ChineseDistricts[86];
            var provinceData = [];
            // 获取省份
            $.each(ChineseProvice,function(key,value){
                $.each(value,function(index,elem){
                    provinceData.push(elem);
                })
            });
            $.each(provinceData,function(index,elem){
                provinceHtml += '<a class="db pt-3 pb-3 province" href="javascript:;" data-value="'+elem.address+'" data-id="'+elem.code+'">'+elem.address+'</a>' 
            })
            $(".province-item").append(provinceHtml);
        }
        var cityRender = function(){
            $(".province-item").on("click",".province",function(){
                var provinceId = $(this).data("id");
                proValue = $(this).data("value");
                console.log(provinceId,proValue)
                formValue = proValue;
                $(this).addClass("active").siblings('a').removeClass("active");
                if(provinceId == 0){
                    $(".city-name").text("城市");
                    $(".city-name").closest(".filter-title").removeClass('show').removeClass('active');
                    $('.s-filter').trigger('fold');
                    $('.city-item').hide();
                    resetLoading();
                }else{
                    var cityHtml = "";
                    $(".city-name").text($(this).text());
                    $.each(ChineseDistricts[provinceId],function(key,value){
                        cityHtml += '<a class="db pt-3 pb-3 city" href="javascript:;" data-value="'+value+'" data-id="'+key+'">'+value+'</a>'
                    })
                    $(".city-item").show().html(cityHtml);
                }
            })
        }
        var render = function(){
            $(".city-item").on("click",".city",function(){
                var cityVal = $(this).data("value");
                formValue = proValue + "," + cityVal;
                $(".city-name").text($(this).text());
                $(this).addClass("active").siblings('a').removeClass("active");
                $(".city-name").closest(".filter-title").removeClass('unfold').addClass('show');
                resetStyle();
                resetLoading();
            })
        }

        var btnClick = function(){
            $(".confirm-choose").on("click",function(){
                resetStyle();
                resetLoading();
            })
            //点击取消按钮
            $(".cancle-choose").on("click",function(){
                $('.s-filter').trigger('fold');
                $('.content-wrap').find("a").removeClass("active");
                $('.content-wrap').find("a").eq(0).addClass("active");
                $(".city-name").parent(".filter-title").removeClass('active').removeClass("show");
                $(".city-name").text("城市")
                $(".city-item").hide();
                $(".ajax-form").find("[name=city]").val("");
                $(".contact-list").html("");
                $(".infinite-loading").find("[data-name=offset]").val(1);
                $(".infinite-loading").infiniteLoading("reset");
                $(window).scrollTop(100);
            })
        }
        return {
            init : function(){
                provinceRender();
                cityRender();
                render();
                btnClick();
            }
        }
    }();

    //加载职位、分组、行业列表
    var loadingSociaty =function(){
        // 加载商会列表
        dm.get("../api/mobile/commerce.json",{"type" : "commerce"}).done(function(json) {
            var html="<li class='pt-3 pb-3 liClick active' data-value='' data-type='sociaty'>不限</li>";
            $.each(json.data,function(index,elem){
                html += "<li class='pt-3 pb-3 liClick' data-value='"+elem.sociatyId+"' data-type='sociaty'>"+elem.sociatyName+"</li>" 
            })
            var wrap = $(".content-wrap").find('[data-name="commerce"]');
            var $li = wrap.find('li');
            if($li.length == 0){
                wrap.append(html);
            }
        });
        // 获取行业列表
        var tradeHtml = "<li class='active pt-3 pb-3 liClick' data-value=''>不限</li>";
        $.each(trade,function(i,e){
            tradeHtml += '<li class="pt-3 pb-3 liClick" data-value="'+e.value+'">'+e.registerTrade+'</li>';
        });
        $(".content-wrap ").find("[data-name=industry]").append(tradeHtml);
    }

    var loadingGroup = function(sociatyId){
        //获取群组列表
        dm.get("../api/mobile/group.json",{"sociatyId" : sociatyId}).done(function(json) {
            var html;
            if(sociatyId){
                html="<li class='pt-3 pb-3 liClick active' data-value='null' data-type='group'>不限</li>";
                $.each(json.data,function(index,elem){
                    html += "<li class='pt-3 pb-3 liClick' data-value='"+elem.groupId+"' data-type='group'>"+elem.groupName+"</li>" 
                })
            }else{
                html = '';
            }
            var wrap = $(".content-wrap").find('[data-name="group"]');
            wrap.html(html);
        });
    }

    filterHelper.init();
    $('.s-filter').headroom();
    citySelect.init();
    loadingSociaty();

    // 滚动加载处理数据
    $(".infinite-loading").find('form').find('.filter-type').val();
    $(".infinite-loading").find('form').find('[name="keyword"]').val();
    var infiniteLoading = window.infiniteLoading = $(".infinite-loading").infiniteLoading({parseData:function(list){
        for(var i=0;i<list.length;i++){
            var groupName = list[i].groupStr || "";
            list[i]._groupName = groupName.split(",");
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
        $(window).scrollTop(100)     
    };
    // 搜索关键字
    var searchKeyword = window.searchKeyword = function(keyword){
        formPage.find('[name=keyword]').val(keyword);
        headerResult.removeClass('dn').find('.keyword').html(keyword);
        showResult();
    };
    dm.router(['/search'],function(path){
        dm.view.show(path.replace(/^\//, ''));
    });

    //搜索表单提交
    formSearch.on("submit", function(event){
        event.preventDefault();
        var keyword = formSearch.find('[name=keyword]').blur().val();
        formSearch[0].reset();
        $(".filter-type").val("");
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
        $(".filter-type").val("");
        searchKeyword(keyword);
        $(".s-filter").hide();
        $(".contact-content").css("paddingTop","10px");
    });

})
