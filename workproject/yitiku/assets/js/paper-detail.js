$(function(){
    // 标题hover效果处理
    var titleHover = function(){
        $("#pui_title").live("mouseover",function(){
            $(this).children(".pui_titlemenu").show();
            $(this).find(".amendquestype").show();
            $(this).css({ border: "1px solid #f43c5e", background: "#feeff2" });
        });
        $("#pui_title").live("mouseout",function(){
            $(this).children(".pui_titlemenu").hide();
            $(this).find(".amendquestype").hide();
            $(this).css({ border: "1px solid #fff", background: "#fff" });
        });
    }
    // 提醒修改
    var noticemenuHover = function(){
        $(".pui_noticeBox").live("mouseover",function(){
            $(this).children(".pui_noticemenu").show();
            $(this).find(".amendquestype").show();
            $(this).css({ border: "1px solid #f43c5e", background: "#feeff2" });
        });
        $(".pui_noticeBox").live("mouseout",function(){
            $(this).children(".pui_noticemenu").hide();
            $(this).find(".amendquestype").hide();
            $(this).css({ border: "1px solid #fff", background: "#fff" });
        });
    }
    var partHeaderHover = function(){
        $(".parthead").live("mouseover",function(){
            $(this).children(".partmenu").show();
            $(this).find(".amendquestype").show();
            $(this).css({ border: "1px solid #f43c5e", background: "#feeff2" });
        });
        $(".parthead").live("mouseout",function(){
            $(this).children(".partmenu").hide();
            $(this).find(".amendquestype").hide();
            $(this).css({ border: "1px solid #fff", background: "#fff" });
        });
    }
    var quesTypeHeadHover = function(){
        $(".questypehead").live("mouseover",function(){
            $(this).children(".questypemenu").show();
            $(this).find(".amendquestype").show();
            $("#paperpart1 .questype:first .questypemenu .typemoveup").hide();
            $("#paperpart1 .questype:last .questypemenu .typemovedn").hide();   
            $("#paperpart2 .questype:first .questypemenu .typemoveup").hide();
            $("#paperpart2 .questype:last .questypemenu .typemovedn").hide();
            /*end*/
            $(this).css({ border: "1px solid #f43c5e", background: "#feeff2" });
            
        });
        $(".questypehead").live("mouseout",function(){
            $(this).children(".questypemenu").hide();
            $(this).find(".amendquestype").hide();
            $(this).css({ border: "1px solid #fff", background: "#fff" });
        });

    }

    /**
     * 修改大题弹出层
     */
    var popTips5 = function(message, t_id,fenshu){
        console.log(fenshu)
        showTipsWindown("试卷设置", 'mbquesContent4',390, 240);
        $(".mbquesBtn4").attr("checked", false);
        
        var _find = $("#windown-content").find("textarea[name=t_title]");
        //将题型的默认分数值放入框中
        $("#windown-content").find("input[name=fenzhi_xuanze]").val(fenshu);
        _find.html(message);
        _find.attr('abc', t_id);/*增加题型选项*/
    }

    // 修改大题标签
    var editQuesHeader = function(){
        $(".mbquesBtn4").live("click", function(){
            var _find = $(this).parent().next().find("span[class=questypename]");
            var message = _find.html();

            var reg = /每小题(\d)/;
            var r = message.match(reg);
            if(r!=null){
                var fenshu = r[1];//获得题型统一设置的每小题的分数
            }
            var t_id = _find.attr('name');/*题型id*/
            popTips5(message,t_id,fenshu);
        });

        /**
         * 修改大题信息，统一修改下面小题的分值，确定按钮
         **/
        $(".edit_t_title").live("click", function(){
            var _find = $("#windown-content").find("textarea[name=t_title]");
            var fenzhi_value = $("#windown-content").find("input[name=fenzhi_xuanze]").val();
            var t_title = _find.val();//修改后的标题

            if(fenzhi_value==''){
                alert(" 还没有设定分值");
                return false;         
            }
            if(!/^[0-9]*$/.test(fenzhi_value)){
                alert("请输入数字!");
                return false;
            }
            var t_id = _find.attr('abc');
            $.ajax({
                type: "get",
                url: "../api/edit_tixing_exam.json",
                data: "fenzhi_value="+fenzhi_value+"&t_id="+t_id,
                success: function(data){
                    console.log(data);
                    title_all = data.title;//题型名称
                    edit_t_title(title_all, t_id, fenzhi_value); 
                    $("span[abc="+t_id+"]").html(title_all);
                    closeWindown();
                }
            });          
        });
    }
    //修改大题信息
    var edit_t_title = function (t_title, t_id, fenzhi_value,whether_per){
        //是否单独设置每小题的分数
        if(whether_per==null){
            whether_per = 'false';
        }
        var examPaper_2 = $("#pui_noticetip").html();//判断是否是用的高考模拟卷的模板
        var AjaxUrl = '../api/edit_t_title.json';
        $.ajax({
            type: "get",
            data: {t_title:t_title, t_id:t_id, fenzhi_value:fenzhi_value, whether_per:whether_per},
            url: AjaxUrl,
            dataType: "json",
            success: function(data){
                if(examPaper_2==null){
                    //examPaperIndex1应用到满分的部分
                    $("#zongfen_1").html("");
                    $("#zongfen_1").css({"color":"#c00","opacity":"0","filter":"alpha(opacity=0),"}).fadeIn(500).html(data).animate({"opacity":"1","filter":"alpha(opacity=100),"},1000,function(){$('.totalScore span').css("color","#333");});
                }else{
                    $("#zongfen_1").html("，满分："+data.score+"分。");
                }
            }
        });
    }
    //保存试卷标题和副标题的“公共方法”
    var save_title_public =function(){
        /*搜索弹出层*/
        var title    = $("#windown-content").find("textarea[name=paper_title]").val();
        var title2   = $("#windown-content").find("textarea[name=paper_title2]").val();
        edit_paper_title(title, title2);
        /*更新html*/
        $("div#pui_maintitle").html(title);
        $("div#pui_subtitle").html(title2);
        closeWindown();
        $("div.mbquesTxt").find("textarea[name=paper_title]").html(title);
        $("div.mbquesTxt").find("textarea[name=paper_title2]").html(title2);
        /*提交保存试卷的隐藏框子*/
        $("div.mbquesTxt").find("textarea[name=save_title]").html(title);
        $("div.mbquesTxt").find("textarea[name=save_title1]").html(title2);
        /*修改下载试卷的title*/
        title = $(".save_title_docx").text().replace(/\(/g,"<br />(");
        $(".save_title_doc").html(title+'_yitiku.doc');
        $(".save_title_docx").html(title+'_yitiku.docx');
        $(".save_title_pdf").html(title+'_yitiku.pdf');
        
        /*修改下载答题卡的title*/
        $(".fl .datika_doc").html(title+"答题卡.doc");
        $(".fl .datika_docx").html(title+"答题卡.docx");
    }
    /**
    * 修改试卷标题
    * title 主标题
    * title2 副标题
    */
    var edit_paper_title = function(title, title2){
        var AjaxUrl = '../api/edit_paper_title.json';
        $.ajax({
            type: "get",
            data: {title:title,title2:title2},
            url: AjaxUrl,
            dataType: "json",
            success: function(data){
            }
        });
    }
    /**
    * 修改试卷注意事项
    */
    var edit_paper_attention = function (attention){
        if(attention){
            var AjaxUrl = '../api/edit_paper_attention.json';
            $.ajax({
                type: "get",
                data: {attention:attention},
                url: AjaxUrl,
                dataType: "json",
                success: function(data){
                }
            });
        }
    }
    // 修改第一卷
    var edit_first_paper = function(first_paper_title, first_paper_attention){
        if(first_paper_title && first_paper_attention){
            var AjaxUrl = '../api/edit_paper_attention.json';
            $.ajax({
                type: "get",
                data: {first_paper_title:first_paper_title, first_paper_attention:first_paper_attention},
                url: AjaxUrl,
                dataType: "json",
                success: function(data){
                }
            });
        }
    }

    // 绑定修改事件
    var bindEditPaper = function(){
        //修改试卷标题
        $(".edit_paper_title").live("click", function(){
            save_title_public();
        });
        /**
        * 修改试卷“注意事项”
        */
        $(".edit_paper_attention").live("click", function(){
            var attention    = $("#windown-content").find("textarea[name=attention]").val();
            edit_paper_attention(attention);
            $("div#pui_noticetext").html(attention);
            closeWindown();
            $("div.mbquesTxt").find("textarea[name=attention]").html(attention);
        });

        //修改第一卷
        $(".edit_first_paper").live("click", function(){
            var first_paper_title = $("#windown-content").find("textarea[name=first_paper_title]").val();
            var first_paper_attention = $("#windown-content").find("textarea[name=first_paper_attention]").val();
            edit_first_paper(first_paper_title, first_paper_attention);
            $("div#partname1").html(first_paper_title);
            $("div#partnote1").html(first_paper_attention);
            closeWindown();
            $("div.mbquesTxt").find("textarea[name=first_paper_title]").html(first_paper_title);
            $("div.mbquesTxt").find("textarea[name=first_paper_attention]").html(first_paper_attention);
        });
    }

    // 题目Hover效果
    var quesboxHover = function(){
        $(".fck007").live("mouseover",function(){
            var _this = $(this).parent().parent();
            _this.children(".quesopmenu").show();
            _this.css("border", "1px solid #1887e3");
        });
        $(".quesTxt2").live("mouseover",function(){
            var _this = $(this).parent().parent();
            _this.children(".quesopmenu").show();
            _this.css("border", "1px solid #1887e3");
        });
        // $(".quesbox").live('mouseleave',function(){
        //     $(this).children(".quesopmenu").hide();
        //     $(this).css("border", "1px solid #fff");
        //     $(this).children().children(".quesTxt2").hide();
        //     $(this).children().children(".showAnswer").hide();
        // })

        $(".quesbox").hover(function(){
            $(this).closest('.questypebody').find('li:first').find('.quesopmenu .moveup').hide();
            $(this).closest('.questypebody').find('li:last-child').find('.quesopmenu .movedn').hide();
        },function(){
            $(this).children(".quesopmenu").hide();
            $(this).css("border", "1px solid #fff");
            $(this).children().children(".quesTxt2").hide();
            $(this).children().children(".showAnswer").hide();
        });
    }
    // 查看答案
    var openAnswer = function(){
        $('.quesbox').on('click','.quesopmenu .answer',function(){
            var answerTxt = $(this).parent().parent().children().children(".quesTxt2");
            answerTxt.slideToggle("slow");
        })
    }

    // 初始化
    var init = function(){
        titleHover();
        partHeaderHover();
        noticemenuHover();
        quesTypeHeadHover();
        bindEditPaper();
        editQuesHeader();
        quesboxHover();
        openAnswer();
    }
    // 初始化
    init();
})