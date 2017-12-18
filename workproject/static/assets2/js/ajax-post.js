var dm = {
  doAjax : function(method,url,data,callback,dataType){
  if(method === "get"){
    method = "get" ;
  }
  if(method === "post"){
    method = "get" ;
  }
  if(dataType == "html"){dataType = "html";}else{dataType = 'json'}
  $.ajax({
    url : url ,
    dataType : dataType ,
    type : method ,
    data : data,
    success : callback
  })
  }
} 