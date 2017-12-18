define(function(require, exports, module) {

    require('dm.common');
    require('header');
    require('dm.modal');

    require('jqPaginator');
    
    if (!investor) { var investor = {}; }
    investor.Page={
        
        openSidebar: function (button){
            var selector = $(button).attr('href');
            $(selector).show();
            $('div.sidebar').css("z-index","1000");
            $('a.btn-close').show();
            //$('div.main-content').hide();
            $(document.body).css("overflow","hidden");
            //取消事件的默认动作
            event.preventDefault();
            return false;
            },
        closeSidebar: function(button){
            $('div.sidebar').css("z-index","-1000");
            $('div.main-content').show();
            $(document.body).css("overflow","scroll");
            },
            
            
        }
    $(function(){
        var stage = $("#stage").val() || '';
        var type = $("#type").val() || '';
        var total = $("#recordCount").val() || 0;
        var pSize = $("#pageSize").val() || 10;
        var currentPage = $("#currentPage").val() || 0;
        var order=$("#order").val() || '';
        var city=$("#city").val() || '';
        if(stage!=""){
            var index =stage.lastIndexOf(",");
            var s = stage;
            if(index!=-1){
                s=stage.substring(0,index);
            }
            $("[data-id=stage-"+s+"]").attr("class","selected-item");
        }else{
            $("[data-id=stage-all]").attr("class","selected-item");
        }
        if(city!=""){
            var index =city.lastIndexOf(",");
            var s = city;
            if(index!=-1){
                s=city.substring(0,index);
            }
            $("[data-id=city-"+s+"]").attr("class","selected-item");
        }else{
            $("[data-id=city-all]").attr("class","selected-item");
        }
        
        if(type!=""){
            var index =type.lastIndexOf(",");
            var s = type;
            if(index!=-1){
                s=type.substring(0,index);
            }
            $("[data-id=type-"+s+"]").attr("class","selected-item");
        }else{
            $("[data-id=type-all]").attr("class","selected-item");
        }
        
        
        $(".stageRadioClick").click(function(){
            $("#stage").val($(this).children("input")[0].value);
            $("#currentPage").val(1);
            $("#ilForm").submit();
        });
        $(".typeRadioClick").click(function(){
            $("#type").val($(this).children("input")[0].value);
            $("#currentPage").val(1);
            $("#ilForm").submit();
        });
        $(".cityRadioClick").click(function(){
            $("#city").val($(this).children("input")[0].value);
            $("#currentPage").val(1);
            $("#ilForm").submit();
        });
        
        total = parseInt(total);
        pSize = parseInt(pSize);
        currentPage=parseInt(currentPage);
        
        /*$('#pagination1').jqPaginator({
             first: '<li class="first"><a href="javascript:void(0);">第一页</a></li>',
              prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
              next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
              last: '<li class="last"><a href="javascript:void(0);">最后一页</a></li>',
              page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
            totalCounts:total,
            pageSize:pSize,
            visiblePages: 10,
            currentPage: currentPage,
            onPageChange: function (num, type) {
                if('change'==type){
                    $("#currentPage").val(num);
                    $("#ilForm").submit();
                }
            }
        });*/
        
    });

    window.order = function (orderid) {
        $("#order").val(orderid);
        $("#currentPage").val(1);
        $("#ilForm").submit();
    }
});
