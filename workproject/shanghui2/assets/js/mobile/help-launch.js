$(function(){
    $('.launch-form').on('success',function(e){
        dm.alert("求助正在审核中",function(){
            location.href ='help-looking.html'
        })  
    })
})