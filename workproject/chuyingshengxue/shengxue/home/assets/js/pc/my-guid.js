$(function(){
  $(".my-guid-form").attr("action",API.root + $(".my-guid-form").attr("action"))
  // 获取学科
  dm.get(API.root+'/api/subject/list').done(function(res){
    var list = res.data.list;
    var html = "";
    for(var i=0; i<list.length; i++){
      html += template("subjectList",list[i]);
    };
    $('.subject-list').html(html);
  });
  // 引导成功
  $('.my-guid-form').on('success',function(e,res){
    if(res.code == 0){
      document.location.href= API.root + "/my-paper.html";
    }
  })

  //所在城市
  var getCity = function(){
    var provinceList = [],provinceHtml = "",location;
    $.each(ChineseDistricts[86],function(name,item){
      provinceList = provinceList.concat(item);
    })
    console.log(provinceList);
    $.each(provinceList,function(i,item){
      provinceHtml += '<li class="province-item" data-code="'+item.code+'">'+item.address+'</li>';
    })
    $('.location-list').find(".province-list").html(provinceHtml);

    //点击省份
    $(".location-city").on("click",".province-item",function(){
      var cityHtml = "";
      var code = $(this).data("code");
      location = ""
      $('[name="province"]').val(code);
      location += $(this).text();
      $(this).parent().siblings(".city-list").show();
      $(this).addClass("active");
      $(this).siblings().removeClass("active");
      $.each(ChineseDistricts[code],function(code,address){
        cityHtml += '<li class="city-item" data-code="'+code+'">'+address+'</li>';
      });
      $('.location-list').find(".city-list").html(cityHtml);
      $(".city-name").text(location);
    });

    // 点击城市
    $(".location-city").on("click",".city-item",function(){
      var city = $(this).text()
      location = location +'/'+ city;
      var code = $(this).data("code");
      $(this).addClass("active");
      $(this).siblings().removeClass("active");
      $('[name="city"]').val(code);
      $(".location-list").hide();
      $(".location-city").removeClass("expanded");
      $(".city-name").text(location);
      $('[name="location"]').val(location.split("/").join(","));
    })
  }

  // 展开/关闭城市列表
  $(".location-city").on('click',".city-name",function(event){
    if( $(".location-city").hasClass("expanded")){
      $(".location-list").hide();
      $(".location-city").removeClass("expanded");
    }else{
      $(".location-list").show();
       $(".location-city").addClass("expanded");
    }
  })

  getCity();
})