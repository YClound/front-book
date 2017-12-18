$(function(){
    // 单选多选切换
    var bindChooseEvent = function(){
        // 切换成多选
        $('#input1').on('click',function(){
            $("#input1").css("display","none");
            $("#input2").css("display","block");
            $("#all_zsd").css("display","none");
            $(".button").css("display","block");
            $(".leftbottom2").css("display","block");
            $("#root li input").css("display","inline-block");
        });
        // 切换成单选
        $('#input2').on('click',function(){
            $("#all_zsd").css("display","block");
            $("#input2").css("display","none");
            $("#input1").css("display","block");
            $(".button").css("display","none");
            $("#root li input").css("display","none");
        });
        //全选
        $("#input3").click( function() {
             $('#root').find('input[type=checkbox]').each(function () 
             { $(this).attr('checked', 'checked'); });
        })
        //清空
        $("#input4").click(function() {
             $('#root').find('input[type=checkbox]').each(function () 
             { $(this).removeAttr('checked', 'checked'); });
        });
        // 多选确定传递参数
        $("#input5").click(function(){
            var point = $("input:checked").map(function(){
            return $(this).val();
        }).get().join("-");
            window.location.href="/tiku/shuxue/point" + point ; 
        });
    }

    // 报错事件
    var bindErrorEvent = function(){
        $('.txt').live("mouseover",function(){
            $(this).find('.quesdiv font').css("display","block");
            $(this).addClass('detailsLine');
        });
        $('.txt').live("mouseleave",function(){
            $(this).find('.quesdiv font').css("display","none"); 
            $(this).removeClass('detailsLine')   
        });
        $('.reportError').live("mouseover",function(){
            $(this).css("color","#f43c5e");
        }); 
        $('.reportError').live("mouseout",function(){
            $(this).css("color","#1887e3");
        });
    }

    //当页面滚动时固定左侧筛选条件
    var leftPosition = function(){
        var ht = $(document).height();
        var isScrollLocked = false;
        $(window).scroll(function(){
            if (!isScrollLocked) {
                isScrollLocked = true;
                var top = $(window).scrollTop();
                var Hei=135;
                if (top <= Hei) {
                    $(".tongbu").removeClass("tongbuposition");   
                }
                if (top > Hei) {  
                    $(".tongbu").addClass("tongbuposition");
                    $(".leftbottom2").css("max-height","500px");
                    $(".leftbottom2").css("height","auto");
                }
                isScrollLocked = false;
            }
        });
    }

    //右侧试卷固定
    var bindRigthNav = function(){
        var browserWidth = $(window).width();
        if(browserWidth<800){
            $(".rightNav").css("bottom","150px");
        }
        $("#lock").live("click",function(){
            $("#lock").toggleClass("lock");
            if($("#lock").hasClass('lock')){
                $(this).text("点击展开");
            }else{
                $(this).text("点击收起");
            }
            $(".rightNav").toggleClass("rightNav1");
        });
    }
    //获取我的试卷中的内容
    var ajax_right = function(){
        $.ajax({
            type: "get",
            url: "../api/sg_right_ajax.html",
            dataType: "html",
            success: function(html){
                $("#ajax_r").empty().append(html + '<div class="suggestion"><a href="/feedback/" target="_blank">问题反馈</a></div>');
            }
        });
    }
    //移除试卷
    var js_del_sg = function (_this){
        var exam_id = _this.attr('id');//试题id
        var t_id = _this.attr('t_id');//题型id
        $.ajax({
            type: "get",
            url: "../api/sg_del_exam.html",
            data: {exam_id:exam_id,t_id:t_id},
            dataType: "html",
            success: function(html){
                $(".paper_"+exam_id).html('加入试卷').removeClass('js_del_sg')
                .parent().removeClass('overicon4').addClass('icon4').addClass("abc");
                $("#ajax_r").empty().append(html);
            }
        });

    }
    // 判断能否添加试卷 ajax
    var bindJoinPaper = function(){
        $(".handle #js_qs li.abc").live('click',function(){
            var _this = $(this);
            var _this_c = _this.children();
            if(_this.hasClass('icon4')){//添加试题情况
                var sub_id = _this_c.attr('sub_id');
                var exam_id = _this_c.attr('id');
                var t_id = _this_c.attr('t_id');
                $.ajax({
                    type: "get",
                    url: '../api/check_add_paper.html',
                    dataType: "html",
                    data: {sub_id:sub_id,exam_id:exam_id,t_id:t_id},
                    success: function(data){

                        if (data.indexOf('<ul class="ajax_r">')<0){
                            $(".errorPop #save_til").html(data);
                            //不可以加入
                            showTipsWindown("提示", 'no_allow_joinContent', 700, 100);
                        }else{
                           _this.remove();
                           var str = "<li class='overicon4 abc'><a href='javascript:;' id='"+exam_id+"' sub_id='"+sub_id+"' t_id='"+t_id+"' class='js_del_sg paper_"+exam_id+"'>移出试卷</a></li>";
                            $("ul.jsjs"+exam_id).prepend(str);
                            $("#ajax_r").empty().append(data);
                        }
                    }
                });
            }
            else{//移除试题
                js_del_sg(_this_c);
            }
        });
    }
    // 取消收藏
    var cancel_collect = function(exam_id,sub_id,sub_name,tixing_name){
        var is_shijuan;
        $.ajax({
            type: "get",
            url: "../api/cancel_collect.json",
            data: "exam_id="+exam_id+"&sub_id="+sub_id,
            success: function(msg){
                console.log(msg)
                if(msg.status==1){
                    //如果是试卷下载页面则不加A标签，否则加A标签
                    if(is_shijuan!=null){
                        $(".exam_"+exam_id).html("收藏试题");
                    }else{
                        $("#exam_"+exam_id).html("<a href=\"javascript:;\">收藏试题</a>");
                    }
                    $("#exam_"+exam_id).addClass("icon3");
                    $("#exam_"+exam_id).removeClass("overicon3");
                    $("input[name='tagName']").val(sub_name+tixing_name);
                }else{
                    alert("对不起，取消收藏失败，请重试！");
                }
            }
        });
    }
    // 收藏
    var collect_exam = function(exam_id,sub_id){
        var is_shijuan;
        $.ajax({
            type: "get",
            url: "../api/collect_exam.json",
            data: "exam_id="+exam_id+"&sub_id="+sub_id,
            success: function(msg){
              if(msg.status==1){
                //提示框
                showTipsWindown("提示", 'mbstowContent',500, 275);
                $(".stowTag").attr("checked", false);
                //如果是试卷下载页面则不加A标签，否则加A标签
                if(is_shijuan!=null){
                   $(".exam_"+exam_id).html("取消收藏");
                }else{
                   $(".exam_"+exam_id).html("<a href=\"javascript:;\">取消收藏</a>");
                }
                $(".exam_"+exam_id).toggleClass("overicon3");
                $(".exam_"+exam_id).removeClass("icon3");
              }else{
                alert("对不起，收藏失败！");
              }
            }
        });
    }

    //收藏事件
    var bindCollect = function(){
        //点击收藏试题
        $(".icon3").live("click",function(){
            var exam_id = $(this).attr('exam_id');
            var sub_id = $(this).attr('sub_id');
            var sub_name = $(this).parent().find("input[name='sub_name']").val();
            var tixing_name = $(this).parent().find("input[name='tixing_name']").val();
            $("#mbstowContent").find("input[name=tagName]").attr('value',sub_name+tixing_name);
            collect_exam(exam_id,sub_id);
        });

        //点击取消收藏触发的事件
        $(".overicon3").live("click",function(){
            var sub_id = $(this).attr('sub_id');
            var exam_id = $(this).attr('exam_id');
            var sub_name = $(this).parent().find("input[name='sub_name']").val();
            var tixing_name = $(this).parent().find("input[name='tixing_name']").val();
            cancel_collect(exam_id,sub_id,sub_name,tixing_name);
        });
    }

    //过滤条件
    var bindFilter = function(){
        $(".js_filter").click(function(){
            var id = $(this).attr('id');
            if(id){
                if(id=='des'){id='ny_d';}
                if(id=='asc'){id='ny_a';}
                $("input[name=form_filter]").val(id);
                $("form#form_filter").submit();
            }
        });
    }

    //将试题库列表里面灰色按钮都变成蓝色按钮。
    var blue_turns_to_grey = function(){
        $(".js_del_sg").each(function(){
            $(this).removeClass("js_del_sg").attr("rel", "nofollow").html('加入试卷').parent().attr("class", "abc icon4");
        });
    }

    // 清空我的试卷
    var resetPaper = function(){
        $(".sdfr2").click(function(){
            $.ajax({
                type: "get",
                url: "../api/clear_qk.html",
                dataType: "html",
                success: function(html){
                    $("#ajax_r").empty().append(html+'<div class="suggestion"><a href="/feedback/" target="_blank">问题反馈</a></div>');
                    blue_turns_to_grey();
                }
            });
        });
    }
    //当开启整个试题库页面缓存时候，执行下面JS，替换掉灰色按钮
    var initCollectAndPaper = function(){
        $.ajax({
            type: "get",
            url: "../api/tiku_auto_ajax.json",
            dataType: "json",
            success: function(json){
                if(json){
                    var arr = eval(json);
                    //加入试卷判断
                    $(".handle #js_qs li.abc a").each(function(){
                        var _this = $(this);
                        var id = _this.attr('id');
                        if($.inArray(id, arr[0])!=-1){//证明存在数组中
                            _this.parent().removeClass("icon4").addClass("overicon4").children().html('移出试卷');
                        }
                    });
                    //收藏试题判断
                    $(".handle #js_qs li.icon3").each(function(){
                        var _this = $(this);
                        var id = _this.attr('exam_id');
                        if($.inArray(id, arr[1])!=-1){//证明存在数组中
                            _this.removeClass("icon3").addClass("overicon3").children().html('取消收藏');
                        }
                    });

                }
            }
        });
    }

    // 下载试题
    var bindDown = function(){
        $("#downitem").live("click",function(){
            var sub_id=$(this).parent('.icon1').next('li').attr('sub_id');
            var exam_down_id=$(this).attr('exam_id');
            $.ajax({
                type : 'get',
                data : {'exam_down_id' : exam_down_id,'sub_id':sub_id},
                url : '../api/window_ajax.json',
                dataType : 'jaon',
                success : function(){
                    
                }
            })
        });
    }

    var init = function(){
        initCollectAndPaper();
        ajax_right();
        leftPosition();
        bindChooseEvent();
        bindErrorEvent();
        bindRigthNav();
        bindJoinPaper();
        bindCollect();
        bindFilter();
        resetPaper();
        bindDown();
        $("#mbsaveQues2").live("click",function(){
            window.location = ''
        });
    }

    //初始化
    init();
})
