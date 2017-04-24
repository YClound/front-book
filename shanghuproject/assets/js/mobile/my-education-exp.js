/*
    添加、编辑教育经历使用模板加载template
*/ 
$(function(){
    var dataId;
    var $input = $("form").find('input');
    // 加载教育经历编辑
    dm.router(['/education'],function(path){
        dm.view.show(path.replace(/^\//, ''));
    });

    var currentItem;
    //重置表单内容
    var reset = function(){
        $("form")[0].reset();
        $("form").find('input').val("");
    }

    //点击添加教育经历按钮
    $(".add-information").on("click",function(){
        reset();
        //清除input错误信息提示
        $input.removeClass('error');
        $input.removeBubble();
        $("[type=date]").addClass("placeholder");
        $(".delete-infomation").find("i").hide();
        dm.router.go("/education");
        dm.view.show("education");
    });
    //点击教育经历列表,表单获取列表数据
    $(".my-content").on("click",".content-item",function(){
        dataId = $(this).data("id");
        $(".delete-infomation").find("i").show();
        //清除input错误信息提示
        $input.removeClass('error');
        $input.removeBubble();
        $("[type=date]").removeClass("placeholder");
        var varField = $(this).find(".var-field");
        varField.each(function(){
            var name = $(this).data("name");
            $("[name=" + name +"]").val($.trim($(this).text()));
        })

        // 传参
        $("[name=educationId]").val(dataId);

    });

    $(".delete-infomation i").on("click",function(){
        var item = $(".my-content").find("[data-id="+dataId+"]");
        dm.confirm("是否删除该教育经历?",function(){
            dm.get("../api/mobile/delete.json",{"id" : dataId}).done(function(){
                item.remove();
                var $item = $(".my-content").find(".content-item").length;
                console.log($item);
                if($item == 0){
                    $(".my-content").attr("data-content","0");;
                } 
                history.back(); 
            });  
        });
        
    })

    if(!dataId){
        dm.router.go("");
    }
    //日期输入提醒
    $("[type=date]").on("click",function(){
        if($(this).hasClass("placeholder") && $(this).val() == ''){
            console.log("1111");
            $(this).removeClass("placeholder");
        }
    })

    // 添加或修改成功后
    $("form").on("success",function(i,e){
        var data = {
            id : e.data.educationId,
            startDate : e.data.startDate,
            endDate : e.data.endDate,
            school : e.data.educationSchool,
            profession : e.data.educationMajor
        }
        console.log(data);
        // 判断是修改教育信息还是添加教育信息
        var $item = $(".my-content .panel").find('[data-id=' + (e.data.educationId || 0) + ']');
        console.log($item.length);
        $(".my-content").attr("data-content","1");
        //编辑或添加教育信息
        if($item.length){
            $item.replaceWith(template('edu-item', data));
        }else{
            $(".my-content .panel").append(template('edu-item', data));
        }

        history.back();
    });
})