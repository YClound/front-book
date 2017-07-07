<template>
	<div v-if="polling && !noMask" class="loading-mask">
		<div class="am-toast">
			<div class="am-toast-text"><span :class="'am-icon toast ' + toastType"></span>{{toastMsg}}</div>
		</div>
	</div>
</template>
<script>
	export default {
		props : {
			maxPollTimes : {
				type : Number,
				default : 100
			},
			method : {
				type : String,
				default : 'get'
			},
			noMask : {
				type : Boolean,
				default : false
			},
			toastType : {
				type : String,
				default : 'loading'
			},
			toastMsg : {
				type : String,
				default : '载入中'
			}
		},
		data () {
			return {
				polltimes : 1,
				pid : null,
				polling : false
			}
		},
		methods : {
			start : function(url, params, cb) {
				var me = this;
				
				this.polltimes = this.maxPollTimes;
				
				this.polling = true;
				this.pid = setInterval(function() {
					me.polling && me._startSub(url, params, cb);
				}, 1000);
			},
			stop : function() {
				clearInterval(this.pid);
				this.pid = null;
				this.polling = false;
			},
			_startSub : function(url, params, cb) {
				if (!url) {
					alert('no url specified!');
					this.stop();
					return;
				}
				if (!cb) {
					alert('no polling handler specified!');
					this.stop();
					return;
				}
				var me = this;
				
				this.polltimes--;
				if (this.polltimes <= 0) {
					this.stop();
					return;
				}
				
				$.ajax({
					url : url,
					type : this.method,
					data : params,
					noMask : true,
					ignoreError : true,
					success : function(data) {
						if (cb(data)) {
							me.stop();
						}
					},
					error : function () {
						me.stop();
					}
				});
			}
		}
	};
</script>