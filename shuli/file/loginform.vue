<template>
	<natural-person-prechecker @completed="completed" @formInited="formInited">
		<!-- 需要短信验证 -->
		<template v-if="needsVerify">
			<div class="wrapper second-auth-wrapper">
				<page-title title="短信验证" />
				<common-verifycode-form
						send-sms-code-url="/email/sendSms.json"
						poll-login-state-url="/email/queryTaskStatus.json"
						submit-login-url="/email/submitLogin.json">
					<template scope="props" slot="header">
						<div class="am-whitespace rem5"></div>
						
						<div class="second-auth-header bg-white" align="center">
							<img width="150" src="../../../images/second_auth.png">
							<p class="second-auth-info">
								<b>已向您的手机发送短信验证码</b>
							</p>
							<p class="tip">{{props.me.tip}}</p>
						</div>
						
						<div class="am-whitespace rem5"></div>
					</template>
				</common-verifycode-form>
			</div>
		</template>
		<!-- 不需要短信验证 -->
		<template v-else>
			<page-title title="获取邮箱账单" />
			<common-login-form
					ref="loginform"
					:auto-jump-to-verify="false"
					@verifycodeRequired="verifycodeRequired"
					@loaded="loaded"
					channel-label="邮箱"
					list-url="/"
					get-login-forms-url="/email/getLoginForms.json"
					get-login-fields-url="/email/queryLoginFormFields.json"
					send-sms-code-url="/email/sendSms.json"
					change-verify-code-url="/email/acquireLoginVerifyCode.json"
					submit-login-url="/email/submitLogin.json"
					poll-login-state-url="/email/queryTaskStatus.json">
				<!-- 服务器不可用 -->
				<template slot="service-unavailable-zone" scope="props">
					<div class="service-unavaible">
						{{props.me.errorCode=='AE0501561993'?'网络超时，请重试':'当前官网不能访问，请稍候重试'}}
					</div>
				</template>
				<!-- 公司信息 -->
				<template slot="company-info" scope="props">
					<!-- 引导页面 -->
					<guide-page ref="guidePage" v-if="!props.me.hasError" :auto="true" text="" :item-code="props.me.itemCode" :guide-url="props.me.loadPageUrl" />
					<div class="feedback-wrapper">
						<div class="pop3-help" v-show="props.me.loginType.indexOf('POP3')>-1">
							<template v-if="props.me.itemCode == 'EMAIL_QQ'">
								<guide-page :auto="false" v-if="!props.me.hasError" text="如何获取授权码" :item-code="props.me.itemCode" :guide-url="props.me.loadPageUrl" :enable="props.me.loadPage"/>
							</template>
							<template v-else>
								<guide-page :auto="false" v-if="!props.me.hasError" text="如何开启POP3协议" :item-code="props.me.itemCode" :guide-url="props.me.loadPageUrl" :enable="props.me.loadPage" />
							</template>
			            </div>
						<span class="feedback" v-if="props.me.orgCode=='ZHIMA'">
			                <router-link :to="'/feedback/' + $route.params.itemCode">意见反馈</router-link>
			            </span>
					</div>
					
				</template>



			</common-login-form>
		</template>
	</natural-person-prechecker>
</template>

<script>
    import naturalPersonPrechecker from '../../../components/h5/natural-person-prechecker.vue';
	import commonLoginForm from '../../../components/h5/common-login-form.vue';
	import commonVerifycodeForm from '../../../components/h5/common-verifycode-form.vue';
    import pageTitle from '../../../components/page-title.vue';
    import guidePage from '../../../components/h5/guide-page.vue';
	
	export default {
		props : {
		},
		data () {
			return {
                guideUrls : {
                    EMAIL_126 : '/html/pop3help/EMAIL_126.html',
                    EMAIL_139 : '/html/pop3help/EMAIL_139.html',
                    EMAIL_163 : '/html/pop3help/EMAIL_163.html',
                    EMAIL_ALIYUN : '/html/pop3help/EMAIL_ALIYUN.html',
                    EMAIL_HOTMAIL : '/html/pop3help/EMAIL_HOTMAIL.html',
                    EMAIL_LIVE : '/html/pop3help/EMAIL_LIVE.html',
                    EMAIL_OUTLOOK : '/html/pop3help/EMAIL_OUTLOOK.html',
                    EMAIL_QQ : '/html/pop3help/EMAIL_QQ.html',
                    EMAIL_SINA : '/html/pop3help/EMAIL_SINA.html'
                },
			    needsVerify : false
			}
		},
		components : {commonLoginForm, commonVerifycodeForm, pageTitle, naturalPersonPrechecker, guidePage},
		watch: {
		    '$route': function (to,from) {
		        var path = from.path;
		        var match = path.indexOf('loginform')>-1 || path=='/';

                if(match){
					this.showGuidePage();
                }
            }
		},
		methods : {
			// 展示引导页
		    showGuidePage: function () {
                if( this.$refs.loginform && this.$refs.loginform.loadPage == 'true') {
                    this.$nextTick(function () {
                        this.$refs.guidePage && this.$refs.guidePage.open();
                    });
                }
            },
            verifycodeRequired : function() {
                this.needsVerify = true;
            },
            //session中含有自然人信息展示dom
		    formInited : function() {
                $(document.body).show();
		    },
		    loaded : function() {
                $(document.body).show();
                this.$refs.loginform.adjustFooter();
                this.showGuidePage();
		    },
		    //自然人信息采集成功显示邮箱列表
            completed : function() {
                $(document.body).hide();
                var me = this;
                var _pid = setInterval(function() {
                    if (me.$refs.loginform) {
                    	//加载登陆表单
                        me.$refs.loginform.startToLoad();
                        clearInterval(_pid);
                    }
        
                }, 10);
            }
		},
        mounted : function() {
            $(document.body).hide();
        }
	};
</script>

<style lang="less" rel="stylesheet/less" scoped>
	.service-unavaible {
		color: #f76a24;
		font-size: .15rem;
		line-height: .3rem;
		text-align: center;
		padding : .1rem;
		padding-top: .4rem;
	}
	
	.feedback-wrapper {
		font-size : .14rem;
		text-align : center;
		display : block;
		width : 100%;
		.feedback-text {
			border-width : 0;
			height : 1.5rem;
			width : 100%;
		}
	}
	
	.tip {
		padding-left: .1rem;
		padding-right: .1rem;
	}
</style>
