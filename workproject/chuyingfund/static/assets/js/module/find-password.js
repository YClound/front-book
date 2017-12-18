define(function(require, exports, module) {
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');
    // require('../base2.js')
    $(function(){
      window.smsHelper.init();
      //手机号码验证
      $('#password1').on('success',function(){
        console.log(1);
        location.href = 'password-set.html';
      });
      //密码验证
      $('#password2').on('beforeSend',function(){
        $(this).data('error','');
        var $newPW = $('#new-pw').val();
        var $verifyPW = $('#verify-pw').val();
        if($newPW != $verifyPW){
          $(this).data('error',true);
          /*$('#show-tip').append("<label>您两次密码输入不一样</label>").css('color','red');
          $('#verify-pw').css('color','#000');
          $('#verify-pw').css('margin-bottom','10px');*/
          $('.password-tip').css('display','block');
        }else{
          $('.password-tip').css('display','none');
        }
        
      });
      $('#password2').on('success',function(){
        location.href = 'login-register.html';
        
      });


      
    });
    
});