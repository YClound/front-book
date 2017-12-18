$(function(){
    // 加载服务组和商会职务
    var sociatyRender = function(id){
        var groupHtml = '<option value="">--服务组申请（可选）--</option>';
        var positionHtml = '<option value="">--商会职务申请（可选）--</option>';
        if(id){
            $.get("../api/sociatyGroup/listAll.json",{'sociatyId':id}).then(function(group){
                var groupData = group.data.list;
                $.each(groupData,function(index,elem){
                    groupHtml += '<option value="'+elem.groupId+'">'+elem.groupName+'</option>'
                })
                $(".groupName").html(groupHtml);
            });
            $.get("../api/sociatyPosition/listAll.json",{'sociatyId':id}).then(function(position){
                var positionData = position.data.list;
                $.each(positionData,function(index,elem){
                    positionHtml += '<option value="'+elem.positionId+'">'+elem.positionName+'</option>'
                })  
                $(".positionName").html(positionHtml);
            })
        }else{
            $(".groupName").html(groupHtml);
            $(".positionName").html(positionHtml);
        } 
    }
    // 加载行业
    var tradeRender = function(){
        var tradeHtml = '';
        $.each(trade,function(i,e){
            tradeHtml += '<option value="'+e.value+'">'+e.registerTrade+'</option>';
        });
        $(".registerTrade").append(tradeHtml);
    }
    // 公司列表获取
    var comapnySelect = function(){
        // 获取搜索列表
        dm.get("../api/mobile/companyName.json").done(function(json){
            var list = json.data.list || [];
            var html = "";
            for(var i=0;i<list.length;i++){
                html += template("companyNameList",list[i]);
            }
            $(".company-list ul").append(html); 
        });
        // 高亮关键字 
        var companyVal;
        $(".companyVal").on("input",function(){
            companyVal = $(this).val();
            var companyItem = $(".company-list ul").find(".company-item");
            var reg = new RegExp(companyVal,'g');
            companyItem.each(function(i,e){
                var companyName = $(this).text();
                var hightLight = '<b class="colorYellow">'+companyVal+'</b>';
                $(this).html(companyName);
                if(companyVal){
                    if(reg.test(companyName)){
                        var result = companyName.replace(reg,hightLight)
                        $(this).html(result);
                    }
                }else{
                    $(this).html(companyName);
                }
            });   
        });
        //点击搜索结果的列表
        $(".company-list").on("click","li a",function(){
            var $this = $(this);
            var liVal = $this.find('.company-item').text();
            var liIndustry = $this.find('.industry').text()
            $("[name=registerCompany]").val(liVal);
            $("[name=registerTrade]").val(liIndustry);
            dm.router.go("");   
        })
        $(".company-serch").on("submit",function(e){
            e.preventDefault();
            $("[name=registerCompany]").val(companyVal);
            $("[name=registerTrade]").val("");
            dm.router.go("");
        })
    }
    // 介绍人
    var inviterSelected = function(sociatyId){
        $(".inviterVal").val("");
        $(".inviter-search").off("submit").on("submit",function(event){
            event.preventDefault();
            var inviterName = $(this).find(".inviterVal").val();
            dm.get("../api/mobile/inviterName.json",{"inviterName" : inviterName,"sociatyId":sociatyId}).done(function(res){
                var data = res.data;
                var html = "";
                if(data.length){
                    for(var i=0;i<data.length;i++){
                        html += template("inviterNameList",data[i]);
                    }
                }else{
                    html = '<span class="db pt-6 tac c-9">未查询到该介绍人信息，请重新输入</span>'
                } 
                $(".inviter-list ul").html(html);
            });
        });
        $('.inviter-list').on('click','.inviter-item',function(){
            var inviterId = $(this).data('id');
            var inviterName = $(this).find(".inviter-name").text();
            $('.identity-form').find('.inviterId').val(inviterId);
            $('.identity-form').find('.inviterName').val(inviterName);
            $('html,body').animate({scrollTop: $('[name="companyAddress"]').offset().top},'fast');
            dm.router.go("");
        })
    }
    var init = function(){
        $('.s-header').headroom();
        var sociatyId = serverData.sociatyId;
        // 公司名称
        dm.router(['/company','/inviter'],function(path){
            dm.view.show(path.replace(/^\//,""));
            $(".companyVal").val("");

        })
         // 推荐的默认商会
        $(".sociatyName").val(sociatyId);
        sociatyRender(sociatyId);
        inviterSelected(sociatyId);
        
        $('.sociatyName').on("change",function(){
            sociatyId = $(this).val() || '';
            sociatyRender(sociatyId);
            inviterSelected(sociatyId);
        });
        tradeRender();
        comapnySelect();

        // 表单验证成功
        $(".identity-form").on("success",function(e){
            location.href = "commerce-success.html";
        });
    }

    init();
})