$(function(){
    $("form.ajax-form").on("success",function(e){
        e.preventDefault;
        location.href = "company-success.html";
    })

    // select加载
    var tradeHtml = '';
    $.each(trade,function(i,e){
        tradeHtml += '<option value="'+e.value+'">'+e.registerTrade+'</option>';
    });
    $("[name=registerTrade]").append(tradeHtml);


    // 公司名称
    dm.router(['/company'],function(path){
        dm.view.show(path.replace(/^\//,""));
        $(".companyVal").val("");
    })
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

    $(".form-search").on("submit",function(e){
        e.preventDefault();
        $("[name=registerCompany]").val(companyVal);
        $("[name=registerTrade]").val("");
        dm.router.backTo();
    })
})