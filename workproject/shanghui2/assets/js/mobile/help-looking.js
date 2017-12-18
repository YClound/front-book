$(function(){
    $(".infinite-loading").infiniteLoading({parseData:function(list){
        for(var i=0; i<list.length; i++){
            list[i]._helpPhoto = list[i].helpPhoto.split(",");
        }
    }});

    $(".ajax-form").on("success.infiniteLoading",function(){
        var imagesList = $(".help-list").find(".help-item").find(".images-list");
        $.each(imagesList,function(index,list){
            if(!$(this).data("setting")){
                console.log("111")
                var $this = $(this);
                var imgArr = $this.children();
                var number = $this.children().length;
                if(number > 4 || number == 3){
                    imgArr.css('width', '33%');
                }else{
                    if(number%2 == 0){
                        imgArr.css({'width': '50%','height' : '110px'});
                    }else{
                        imgArr.css({'width': '60%','height' : '100%'});
                    }
                }
                $(this).data("setting","true");
            }
            
        })
    })

    $(document).on('click',".images-list .image-item",function(event){
        try{
            var curImageSrc = $(this).find("img").attr('src');
            var images = $(this).closest('.images-list').find('img');
            var imageArr = [];
            $.each(images,function(index,ele){
                var imgSrc= $.trim($(this).attr("src"))
                imageArr.push(imgSrc);
            })
            wx.previewImage({
                current: curImageSrc,
                urls: imageArr
            });
        }catch(e){
            alert(e)
        }
    })
    
})