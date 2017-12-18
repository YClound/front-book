define(function(require, exports, module) {
    $(function(){
      // 找回密码表单提交成功
      $('#password-find').on('success',function(){
        console.log(1);
        location.href = 'password-alter.html';
      });
      //新密码验证
      $('#password-alter').on('beforeSend',function(){
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
      //确认新密码验证成功
      $('#password-alter').on('success',function(){
        location.href = 'user-login.html';        
      });


      
    });
    
});