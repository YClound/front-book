$(function(){
    var abc = new Array();//学科关联题型 - 全局
    var _date = "20161124";//20120507 - 格式

    // 学科选择
    var selectorHover = function(){
        var timer = null;
        $("h4[class$=btn]").live("click",function(){
            var  _this = $(this)
            var txt =_this.next();
            txt.toggle();
            $("h4[class$=btn]").parent().css("z-index","1")
            $(this).parent().css("z-index","10")
        }); 
        $("h4[class$=btn]").live("mouseout",function(){
            var contentId = $(this).attr("class").replace("btn","Content");
            timer = setTimeout(function(){
                $("."+contentId).hide();
            },200);
        });
        $("ul[class$=Content]").live("mouseout",function(){
            var id = $(this).attr("class");
            timer = setTimeout(function(){
                $("."+id).hide();
            },200);
        });
        $("ul[class$=Content]").live("mouseover",function(){
            clearTimeout(timer);
        });
        $("ul[class$=Content] li").live("click",function(){
            var _this = $(this);
            var ul = _this.parent();
            var h4 = ul.prev();
            h4.html(_this.html());
            // 在写后端代码的时候,需要在这里设置一下隐藏域的值.
            ul.hide();
        });
    }

    //自动生成试卷名称
    var auto_paper_name = function(){
        /*获取科目名称*/
        var type_g = $("input[name=type_g]").val();
        var type_g_name = "";
        $(".selectorContent li").each(function(i){
            if($(this).attr("id")==type_g){
                type_g_name = $(this).html();
                return;
            }
        });
        /*获取试卷类型名称*/
        var _radio = "";
        $("input[name=model_name]").each(function(i){
            if($(this).is(":checked")){
                  _radio =  $(this).attr("id");
                  return;
            }
        })
        /* 获取难易度名称 */
        var type_nanyi = $("input[name=type_nanyi]").val();
        var type_nanyi_name = "";
        $("a.nany").each(function(i){
            if($(this).attr("id")==type_nanyi){
                type_nanyi_name = $(this).html();
                return;
            }
        });
        //高中数学期末考试卷(难度系数：中)-20120628
        $("input[name=paper_title]").val(type_g_name+_radio+"(难度系数："+type_nanyi_name+")-"+_date);
    }

    /*科目关联题型*/
    var ajax_kemu = function(sub_id,shijuan_type){
        $.ajax({
            type: "get",
            url: "../api/tixing.json",
            data: {sub_id:sub_id,shijuan_type:shijuan_type},
            dataType: "json",
            success: function (data) {
                var tixing_html="";
                //如果是高考模拟卷，则会有默认数值
                if(data['shijuan_type']==5){
                    for(var i =0;i<data['tixing'].length;i++){
                        tixing_html += "<li>";
                        tixing_html += "<label>"+data['tixing'][i].name+"</label>";
                        tixing_html += "<font>";
                        tixing_html += "<button type='button' value='-' class='minus1' onclick=\"changeCount(countInput,this, '-')\"></button>";
                        tixing_html += "<input type='text' value='"+data['tixing_num'][data['tixing'][i].id]+"' id=\"countInput\" maxlength='3' onblur='jiance($(this))' onkeyup=\"this.value=this.value.replace(/\\D/g,'')\" onafterpaste=\"this.value=this.value.replace(/\\D/g,'')\" class='countInput' name='tx["+data['tixing'][i].id+"]' />";
                        tixing_html += "<button type='button' value='+' class='add' onclick=\"changeCount(countInput,this, '+')\"></button>";
                        tixing_html += "</font><u>道题</u>";
                        tixing_html += "</li>";
                    };
                }else{
                    for(var i =0;i<data['tixing'].length;i++){
                        tixing_html += "<li>";
                        tixing_html += "<label>"+data['tixing'][i].name+"</label>";
                        tixing_html += "<font>";
                        tixing_html += "<button type='button' value='-' class='minus1' onclick=\"changeCount(countInput,this, '-')\"></button>";
                        tixing_html += "<input type='text' value='0' id=\"countInput\" maxlength='3' onblur='jiance($(this))' onkeyup=\"this.value=this.value.replace(/\\D/g,'')\" onafterpaste=\"this.value=this.value.replace(/\\D/g,'')\" class='countInput' name='tx["+data['tixing'][i].id+"]' />";
                        tixing_html += "<button type='button' value='+' class='add' onclick=\"changeCount(countInput,this, '+')\"></button>";
                        tixing_html += "</font><u>道题</u>";
                        tixing_html += "</li>";
                    };
                }
                $(".quesTypes ul").empty().append(tixing_html);//清空父节点下的子节点，并且插入新的节点
                abc[sub_id] = tixing_html;
            }
        });
    }

    //获取试卷类型
    var paperType = function(){
        var shijuan_type = 1;
        $("input[name='model_name']").click(function(){
            var shijuan_type = $(this).val();
            var sub_id = $("input[name='type_g']").val();
            ajax_kemu(sub_id,shijuan_type);
        })
    }

    var jiance = window.jiance = function (_this){
        var n = _this.val();
        if(!n){
            _this.val(0);
            return true;
        }
        n = parseInt(n,10);
        if(n>80){
            n=80;
        }
        _this.val(n);
    }

    // 题数增减
    var changeCount = window.changeCount = function (Id, obj, act){
        var num = 0;
        if (act == 0){
            num = 0;
            $(obj).removeClass("minus1").addClass("minus2");
        }else if (act == '+'){
            num = parseInt($(obj).parent().find('input').attr('value')) + 1;
            $(obj).parent().find('input').attr('value', num);
            $(obj).parent().find('.minus1').removeClass("minus1").addClass("minus2");
        }else if (act == '-'){
            num = $(obj).parent().find('input').attr('value');
            if (num == 0){
                num = 0
                $(obj).removeClass("minus2").addClass("minus1"); 
            }else{
                num = parseInt($(obj).parent().find('input').attr('value')) - 1;
                $(obj).removeClass("minus1").addClass("minus2");
            }
            $(obj).parent().find('input').attr('value', num);
        }else{
            num = obj.value;
            if (num == 0){
                num = 1;
                $(obj).parent().find('input').attr('value', num);
            }
        }
    }

    var init = function(){
        //默认加载题型
        ajax_kemu(5,1);
        //自动生成试卷
        auto_paper_name();
        selectorHover();
        paperType();
        //选择难易度
        $("a.nany").click(function(){
            var _this = $(this);
            _this.addClass("this").parent().siblings("li").children("a").removeClass("this"),
            $("input[name=type_nanyi]").val(_this.attr('id'));
            auto_paper_name();
        })
        /**
         * 选择“试卷模版”
         */
        $(".js_aaa").click(function(){
            var _this = $(this);
            $(".js_aaa").removeClass('this');
            _this.addClass('this');
            auto_paper_name();
        });
        /**
         * 提交表单
         */
        $("#submit_1").click(function(){
            var n;
            var m = 0;/*总数*/
            $("input.countInput").each(function(i){
                n = $(this).val();
                n = parseInt(n,10);
                m += n;
            });
            if(m>0 && m<=80){
                $("#znExamIndexForm").submit();
            }else if(m<=0){
                $("#tipContent1 div h4").html('您还没有选择试题数量！');
                $("#tipContent1 div a").remove();
                showTipsWindown("提示", 'tipContent1', 390, 60);
            }else if(m>80){
                $("#tipContent1 div h4").html('您选择的试题数量不能超过 80 个！');
                $("#tipContent1 div a").remove();
                showTipsWindown("提示", 'tipContent1', 390, 60);
            }
        });
    }
    init()
})