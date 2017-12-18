define(function(require, exports, module){
    // 假如返回跳转地址则跳转
    $('form').on('success',function(e,res){
        if(res&&res.redirect){
            location.href = res.redirect;
        }
    });
    // 判断密码
    $('form').on('beforeSend',function(){
        $(this).data('error','');
        var $newPW = $('input[name=new-password]').val();
        var $confirmPW = $('input[name=confirm-password]').val();
        if($newPW != $confirmPW){
          $(this).data('error',true);
          $('.password-tip').css('display','block');
        }else{
          $('.password-tip').css('display','none');
        }
        
      });
});