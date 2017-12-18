function load(isSetPayPwd){
	var setDiv = document.getElementById('setDiv');
	var editDiv = document.getElementById('editDiv');
	if(isSetPayPwd=="true"){
		setDiv.style.display="none";
		editDiv.style.display="block";
	}else{
		setDiv.style.display="block";
		editDiv.style.display="none";
	}
}

$().ready(function() {
	$("#editForm").validate({
		focusInvalid:false, 
	    submitHandler: function(form){
	    	submitEditPre();
	        form.submit();  
	    },
		rules : {
			oldPayPwd1 : {
				required: true,
				pwdVerify: true
			},
			newPayPwd1 : {
					required: true,
					pwdVerify: true
				},
			newPayPwd2 : {
					required: true,
					pwdVerify: true,
					equalTo: "#newPayPwd1"
				}
		},
		messages: {
			oldPayPwd1 : {
				required : "请输入旧支付密码",
				pwdVerify : "6~20个字符，可使用字母、数字，区分大小写，不可纯数字。"
				},
			newPayPwd1 : {
				required : "请输入新支付密码",
				pwdVerify : "6~20个字符，可使用字母、数字，区分大小写，不可纯数字。"
				},
			newPayPwd2 : {
				required : "请输入确认新密码",
				pwdVerify : "6~20个字符，可使用字母、数字，区分大小写，不可纯数字。",
				equalTo: "两次输入密码不一致"
				}
		}
	});
});
$().ready(function() {
	$("#setForm").validate({
		focusInvalid:false, 
	    submitHandler: function(form){  
	    	submitSetPre();
	        form.submit();  
	    },
		rules : {
			payPwd1 : {
					required: true,
					pwdVerify: true
				},
			payPwd2 : {
					required: true,
					pwdVerify: true,
					equalTo: "#payPwd1"
				}
		},
		messages: {
			payPwd1 : {
				required : "请输入支付密码",
				pwdVerify : "6~20个字符，可使用字母、数字，区分大小写，不可纯数字。"
				},
				payPwd2 : {
				required : "请确认支付密码",
				pwdVerify : "6~20个字符，可使用字母、数字，区分大小写，不可纯数字。",
				equalTo: "两次输入密码不一致"
				}
		}
	});
});
$.validator.addMethod("pwdVerify", function(value, element) {
    var payPwdVerify1 = /^\w{6,20}$/;
    var payPwdVerify2 = /^\d{6,20}$/;
    var flag = false;
    if(payPwdVerify1.test(value)){
    	flag = true;
    }
    if(payPwdVerify2.test(value)){
    	flag = false;
    }
    return flag;
}
);
function submitEditPre(){
		var oldPayPwd1 = jQuery('#oldPayPwd1').val();
	var newPayPwd1 = jQuery('#newPayPwd1').val();
	
	oldPayPwd1 =  CryptoJS.MD5(oldPayPwd1+"longan_login");
	newPayPwd1 =  CryptoJS.MD5(newPayPwd1+"longan_login");
	
	jQuery('#oldPayPwd').val(oldPayPwd1);
	jQuery('#newPayPwd').val(newPayPwd1);
	}
function submitSetPre(){
		var payPwd1 = jQuery('#payPwd1').val();
		payPwd1 =  CryptoJS.MD5(payPwd1+"longan_login");
		jQuery('#payPwd').val(payPwd1);
	}