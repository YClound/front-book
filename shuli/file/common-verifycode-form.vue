<template>
	<div class="wrapper second-auth-wrapper">
		<slot name="header" :me="this"></slot>
		
		<div class="second-auth-content">
			<div class="second-auth-login-form">
				<form action="" id="verifyForm">
					<div class="am-list form ">
						<div class="am-list-body">
							<template  v-for="(field, idx) in formFields">
								<smscode-field v-if="field.loginFieldCode=='LOGIN_SMS_CODE'"
											   :rule="field.rule"
											   :rule-msg="field.ruleMsg"
											   @userchange="_checkValid"
											   mandatory
											   :label-width=".8"
											   :max-length="field.maxLength"
											   ref="formField"
											   :label="field.loginFieldName"
											   :type="field.loginFieldType"
											   :placeholder="field.loginFieldDesc"
											   :handle-send-sms="sendSms"
											   :name="field.loginFieldCode"></smscode-field>
								<text-field v-else
											:rule="field.rule"
											:rule-msg="field.ruleMsg"
											ref="formField"
											:label-width=".8"
											mandatory
											@userchange="_checkValid"
											:max-length="field.maxLength"
											@changeverifycode="changeverifycode(field)"
											:type="field.loginFieldType"
											:label="field.loginFieldName"
											:name="field.loginFieldCode"
											:placeholder="field.loginFieldDesc"
											:has-verify-code="field.hasVerifyCode"
											:verify-code="field.verifyCode"
											:verify-format="field.verifyFormat"
											:ext="field.loginFieldExt">
								</text-field>
							</template>

						</div>
					</div>
				</form>
			</div>
		</div>
		
		<div v-show="errorMsg" class="am-wingblank am-whitespace login-error am-ft-12 hide rem5">
			{{errorMsg}}
		</div>
		
		<div class="am-whitespace rem5"></div>
		<div class="am-wingblank rem5">
            <button @click="submitSmsCode" type="button" class="am-button submit-button" :disabled="!valid">提交
            </button>
            
			<!--<div class="am-whitespace rem5"></div>-->
            <!---->
            <!--<button @click="goBack" type="button" class="am-button white">返回-->
            <!--</button>-->
		</div>
		
		<company-info v-if="showCompanyInfo" adjust-to=".second-auth-wrapper"></company-info>
		<login-state-poller ref="poller"></login-state-poller>
	</div>
</template>

<script>
	import companyInfo from './company-info.vue';
	import smscodeField from './form/smscode-field.vue';
	import loginStatePoller from './login-state-poller.vue';
    import textField from './form/text-field.vue';
	
	export default {
		props : {
		    // 额外的参数对象
            extra: {
                type: Object,
				default: function () {
					return {};
                }
			},
			// 图片验证码路径
            verifyImageUrl : {
                type : String,
                default : ''
            },
			// 是否有图片验证码
		    hasVerifyImage: {
                type : Boolean,
                default : false
			},
		    autoSend : {
		        type : Boolean,
			    default : false
		    },
			sendSmsCodeUrl : {
			    type : String,
				default : '/metadata/sendSms.json'
            },
            pollLoginStateUrl : {
                type : String,
                default : '/metadata/queryTaskStatus.json'
            },
			submitLoginUrl : {
			    type : String,
				default : '/metadata/submitLogin.json'
			},
            showCompanyInfo : {
                type : Boolean,
                default : true
            }
		},
		data () {
			return {
                ensureVerifyImage:false,
                formFields: [
                    {
                        rule:/\S{6}/,
                        ruleMsg:'请输入6位正确的短信验证码',
                        maxLength:6,
                        loginFieldName: '动态码',
                        loginFieldType: 'text',
                        loginFieldDesc: '请输入短信验证码',
                        loginFieldExt: '',
                        loginFieldCode: 'LOGIN_SMS_CODE'
                    }
                ],
				imageField: {
                    rule:/\S{4,}/,
                    ruleMsg:'图片验证码格式有误',
                    loginFieldName: '验证码',
                    loginFieldType: 'text',
                    loginFieldDesc: '点击图片可刷新',
                    loginFieldExt: '',
                    loginFieldCode: 'LOGIN_VERIFY_CODE',
                    hasVerifyCode: true,
                    verifyCode:'',
                    verifyFormat: 'png'
                },
				valid : false,
				tip : '',//'点击获取手机动态验证码,完成动态密码验证',
				errorMsg : '',
				sendSms : function() {
					var me = this.$parent;
					me.errorMsg = '';
					
					$.ajax({
						url : me.sendSmsCodeUrl,
						type : 'post',
						data : {
							smsJson : JSON.stringify({
								step : '2',
								params : {
									LOGIN_STEP : '2',
									LOGIN_SMS_CODE : ''
								}
							})
						},
						success : function(data) {
							if (data.success) {
                                me.errorMsg = '';
							    if(data.msg && data.msg.indexOf('*')>-1) {
                                    me.tip = data.msg;
								} else {
                                    me.tip = `${data.msg ? data.msg.substr(0, 3) + '*****' + data.msg.substr(8, 3) : ''}`;
								}

							} else {
								me.errorMsg = data.msg;
							}
						}
					});
					
					return true;
				}
			}
		},
		methods : {

            $checkAll: function () {
                var me = this;
                var flag = true;
                var errorMsg = '';
                $.each(me.$refs.formField || [], function () {
                    var value = this.value, name = this.name, msg = this.errorMsg || this.ruleMsg;
                    //console.log(this.name, this.ruleMsg,this.errorMsg,this.dirty);

                    if (!this.valid) {
                        flag = false;
                        errorMsg = value ? msg:'';
                        return false;
                    }
                });
                // 校验不通过
                if (!flag) {
                    me.valid = false;
                    me.errorMsg = me.errorMsg ? me.errorMsg : errorMsg;

                } else {
                    me.valid = true;
                    me.errorMsg = '';
                }
            },
		    _checkValid: function(fieldValue, field) {
                if (!field.valid) {
                    this.errorMsg = field.errorMsg || field.ruleMsg;
                } else {
                    this.errorMsg = '';
                }
                this.$checkAll();
			},
            changeverifycode : function(field, callback) {
                if(!field) {
                    field = this.imageField;
				}
                field.verifyCode = '';
                var me = this;
				var extra = {LOGIN_STEP : '2'};
                me.errorCode = '';
                $.ajax({
                    url : this.verifyImageUrl,
                    noMask : true,
                    ignoreError : true,
                    type : 'get',
                    data : {
                        formJson:JSON.stringify(extra),
                        itemCode : this.$route.params.itemCode,
                        loginTypeCode : this.extra.loginType
                    },
                    success : function(data) {

                        var msg = data.msg || {};
                        if (!data.success) {
                            //me.errorCode = msg.code || '';
                            me.hasError = true;
                            return;
                        }
                        var verifyInfo = data.msg;
                        verifyInfo ? (field.verifyFormat = verifyInfo.verifyFormat) && (field.verifyCode = verifyInfo.verifyCode) : null;
                        verifyInfo && callback && callback(verifyInfo);
                    }
                });
            },
			goBack : function() {//TODO REMOVE IT
				history.go(-1);
			},
			submitSmsCode : function() {
				var me = this;
				var poller = this.$refs.poller;
                var formData = Utils.serialize($('#verifyForm'));

                formData.LOGIN_STEP = '2';
                $.ajax({
                    url : '/authform/submitLoginInfo.json',
                    type : 'post',
                    data : {
                        loginFormJson : JSON.stringify(formData)
                    },
                    success : function(data) {
                        var ajaxRes = data.ajaxResult;
                        if (!ajaxRes.hasError) {
                            poller.start(me.pollLoginStateUrl, function(msg) {
                                me.errorMsg = msg.errorMsg || msg.fixTips;
                            });
                        } else {
                            me.ensureVerifyImage && me.changeverifycode();
                            me.errorMsg = ajaxRes.errorMsg;
                        }
                    }
                });
				
			}
		},
		mounted : function() {
		    var me = this;
		    var imageField = this.imageField;
		    var formFields = this.formFields;
		    var _formField = this.$refs.formField;
		    var smsCode = _formField.length?_formField[0]:_formField;

            // 假如有图片验证码
		    if(this.hasVerifyImage) {
				this.changeverifycode(imageField,function (verifyInfo) {
					if(verifyInfo.verifyCode) {
					    me.ensureVerifyImage = true;
                        formFields.push(imageField);
					}
                });
			}
			// 自动发送短信
            smsCode.sendSms();
		},
		components : {loginStatePoller, companyInfo, smscodeField, textField}
	};
</script>

<style lang="less" rel="stylesheet/less" scoped>
	.second-auth-wrapper {
		padding-bottom: .2rem;
	}
	
	.errorMsg {
		color : red;
	}
</style>
