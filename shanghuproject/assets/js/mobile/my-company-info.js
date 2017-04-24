$(function(){ 
    var dataId;
    var $input = $("form").find('input');
    // 显示编辑公司信息表单
    dm.router(['/company'],function(path){
        dm.view.show(path.replace(/^\//,""));
    });
    // select加载
    var tradeHtml='';
    $.each(trade,function(i,e){
        tradeHtml += '<option data-value="'+e.value+'">'+e.registerTrade+'</option>';
    });
    $("[name=companyIndustry]").append(tradeHtml);

    // 企业规模加载
    var scaleHtml = '';
    $.each(companyScale,function(i,e){
        scaleHtml += '<option data-value="'+e.id+'">'+e.name+'</option>';
    });
    $("[name=companyScale]").append(scaleHtml);
    //重置表单内容
    var reset = function(){
        $("form")[0].reset();
        $("form").find("input,select").val("");
    }
    //点击添加按钮
    $(".add-information").on("click",function(){
        reset();
        //清除文本框的限制
        $(".view-company .no-edit").find("input").attr('readonly',false);
        $(".view-company .no-edit").find("input").css('color', '#222');
        $(".footer-tip").hide();
        //清除input错误信息提示
        $input.removeClass('error');
        $input.removeBubble();
        $(".delete-infomation").find("i").hide();
        dm.router.go("/company");
        dm.view.show("company");
    })
    //修改公司信息
    $(".my-content").on("click",".content-item",function(){
        var $this =$(this);
        var index = $this.index();
        reset();
        dataId = $this.data("id");
        if(!dataId){
            dm.notice("获取公司信息失败");
            dm.router.go("");
            return;
        }
        //清除input错误信息提示
        $input.removeClass('error');
        $input.removeBubble();
        $(".delete-infomation").find("i").show();
        if(!index){
            $(".delete-infomation").find("i").hide();
            $(".view-company .no-edit").find("input").attr('readonly',true);
            $(".view-company .no-edit").find("input").css('color', '#bbb');
        }else{
            $(".delete-infomation").find("i").show();
            $(".view-company .no-edit").find("input").attr('readonly',false);
            $(".view-company .no-edit").find("input").css('color', '#222');
        }
        

        var varField = $this.find(".var-field");
        varField.each(function(){
            var name = $(this).data("name");
            $("[name=" + name +"]").val($.trim($(this).text()));
        });

        // 获取当前公司信息数据
        dm.get("../api/mobile/company.json",{"id" : dataId}).done(function(json) {
            $("[name=companyIndustry").val(json.data.companyIndustry);
            $("[name=companyAddress").val(json.data.companyAddress);
            $("[name=companyScale").val(json.data.companyScale);
            $("[name=companyWebsite").val(json.data.companyWebsite);
            $("[name=companyWx").val(json.data.companyWx);
            
            //待审核的公司信息的状态
            if(json.data.status == 0){
                $(".footer-tip").show();
                $("form").find("button").attr('disabled', 'true');
            }else{
                $(".footer-tip").hide();
                $("form").find("button").removeAttr('disabled');
            }

            // 传参
            $(".ajax-form").find('[name=companyId]').val(json.data.companyId);
            $(".ajax-form").find('[name=status]').val(json.data.status);
        }).fail(function(){
            dm.toast('获取公司信息失败')
        });
        
    });

    $(".delete-infomation i").on("click",function(){
        var item = $(".my-content").find("[data-id=" + dataId +"]");
        dm.confirm("是否要删除该公司信息?",function(){
            dm.get("../api/mobile/delete.json",{"id" : dataId}).done(function(){
                item.remove();
                history.back();
            }) 
        })
        
    });

    dm.router.go("");


    $("form").on("before",function(){
        $(this).validate('init');
        $("[name=status]").val(0);
    })
    //添加、修改公司信息成功
    $("form").on("success",function(i,json){
        // var $item = $('.my-content .panel').find("[data-id=" + (json.data.modifyId || 0) + "]");
        // if($item.length){
        //     // $item.replaceWith(template("company-info",json.data));
        // }else{            
        //     // $('.my-content .panel').append(template("company-info",json.data));
        // }
        // history.back();
        
        
        dm.alert("信息修改后，需要秘书审核通过，才能生效",function(){
            $(".footer-tip").show();
            $("form").find("button").attr('disabled', 'true');
            $("[name=status]").val(0);
        });
        
    })
})