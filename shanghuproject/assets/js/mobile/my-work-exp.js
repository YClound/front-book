/*
    添加、编辑工作经历
*/
$(function(){
    var dataId;
    var $input = $("form").find('input');
    //打开修改页面
    dm.router(['/work'],function(path){
        dm.view.show(path.replace(/^\//, ''));
    });
    // 获取当前工作经历的data-id
    var dataID;

    // 添加工作经历时重置form
    var reset = function(){
        $("form")[0].reset();
        $("form").find('input').val("");
    }

    // 点击添加按钮
    $(".add-information").on("click",function(e){
        reset();
        //清除input错误信息提示
        $input.removeClass('error');
        $input.removeBubble();
        $("[type=date]").addClass("placeholder");
        $(".delete-infomation").find("i").hide();
        dm.router.go("/work");
        dm.view.show("work");
    })
    //点击工作经历列表,表单获取列表数据
    var index;
    $(".my-content").on("click",".content-item",function(){
        currentItem = $(this);
        index = $(this).index();
        dataId = $(this).data("id");
        if(!dataId){
            dm.notice("获取工作经历失败");
            return;
        }
        $("[type=date]").removeClass("placeholder");
        //清除input错误信息提示
        $input.removeClass('error');
        $input.removeBubble();

        $(".delete-infomation").find("i").show();
        var varField = $(this).find(".var-field");
        varField.each(function(){
            var name = $(this).data("name");
            $("[name=" + name +"]").val($.trim($(this).text()));
        })
        //传参
        $("[name=workId]").val(dataId);
    });
    // 删除
    $(".delete-infomation i").on("click",function(){
        console.log(index);
        dm.confirm("是否删除该工作经历？",function(){
            dm.get("../api/mobile/delete.json",{"id" : dataId}).done(function(){
                $(".my-content").find(".content-item").eq(index).remove();
                var $item = $(".my-content").find(".content-item").length;
                console.log($item);
                if($item == 0){
                    $(".my-content").attr("data-content","0");
                } 
                history.back();  
            }) 
        });
    })

    dm.router.go("");
    
    //日期输入提醒
    $("[type=date]").on("click",function(){
        if($(this).hasClass("placeholder") && $(this).val() == ''){
            console.log("1111");
            $(this).removeClass("placeholder");
        }
    })

    // 表单提交成功
    $("form").on("success",function(i,json){
        var data = {
            workId : json.data.workId,
            startDate : json.data.startDate,
            endDate : json.data.endDate,
            workCompany : json.data.workCompany,
            workPosition : json.data.workPosition
        };
        // 判断是修改教育信息还是添加教育信息
        var $item = $(".my-content .panel").find('[data-id=' + (data.workId || 0) + ']');
        //编辑或添加教育信息
        $(".my-content").attr("data-content","1");
        if($item.length){
            $item.replaceWith(template("work-item",json.data));
        }else{
            $(".my-content .panel").append(template("work-item",json.data));
        }
        history.back();   
    })
});

    

