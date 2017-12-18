$(function(){
    dm.router(['/motto'],function(path){
        dm.view.show(path.replace(/^\//,""));
    });
    $("form").on("success",function(){
        history.back();
    })
})