<template>
	<task-poller ref="taskPoller" :method="method" toast-msg="认证中"></task-poller>
</template>

<script>
	import taskPoller from './task-poller.vue';
	
	export default {
		props : {
			autoJumpToVerify : {
			    type : Boolean,
				default : true
			},
            method : {
                type : String,
                default : 'get'
            }
		},
		data () {
			return {}
		},
		methods : {
			start : function(url, failHandler) {
				if (!url) {
					alert('no url specified');
					return;
				}
				if (!failHandler) {
					alert('no failHandler specified');
					return;
				}
				
				var me = this;
				var poller = this.$refs.taskPoller;
				poller.start(url, null, function(result) {
					var content = result.msg,
						status = content && content.status;
					
					if (!result.success) {
						failHandler(content);
						return true;
					}
					if (['success', 'redirect'].indexOf(status) != -1) poller.stop();
					else return;
					
					Utils.toast('success', '登录成功', 500);
					//write login success flag into session storage
					Utils.setLogined();
					var url = content.redirectPath;
					//登陆成功之后根据返回的跳转地址判断是否需要验证码验证码验证
					if (url) {
						if (url && url.startsWith('verify/') && url.endsWith('.htm')) {
						    if (me.autoJumpToVerify) {
								Utils.jumpTo('/verifycode');
						    } else {
						        me.$emit('verifycodeRequired');
						    }
						} else {
							Utils.redirectTo(url);
						}
					} else {//没有返回地址跳转到loadingdata页面
						var extraData = content.extraData;
                        Utils.jumpTo(`/loadingdata/${extraData.salt}/${extraData.sign}/${extraData.waitSecond}`);
                    }
					
				});
			}
		},
		components : {taskPoller}
	};
</script>

<style lang="less" rel="stylesheet/less" scoped>
</style>
