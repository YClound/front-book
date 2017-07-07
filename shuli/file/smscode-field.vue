<template>
	<div class="am-list-item am-input-autoclear">
		<div :style="'width:' + labelWidth + 'rem;'" class="am-list-label nowrap cust-field-label">{{label}}</div>
		<div class="am-list-control">
			<input @blur="onblur" @focus="onfocus" ref="input" type="text" class="am-flexbox-item"
			       :name="name" v-model.trim="value"
			       :placeholder="placeholder" :disabled="disabled">
		</div>
		
		<div class="am-list-clear" v-show="value && focused" @click="resetValue">
			<i class="am-icon-clear am-icon"></i>
		</div>
		<div class="am-list-button">
			<button @click="sendSms" type="button" :disabled="countDownValue > 0 || forbidden">{{countDownValue > 0 ? countDownValue + '秒后重发' : '点击获取'}}</button>
		</div>
	</div>
</template>

<script>
	export default {
		props : {
            rule : {
                type : RegExp,
                default : null
            },
            ruleMsg : {
                type : String,
                default : ''
            },
		    name : String,
            labelWidth : {
                type : Number,
                default : 1
            },
			label : {
				type : String,
				default : '验证码'
			},
			disabled : Boolean,
			cooldownSecs : {
				type : Number,
				default : 120
			},
			mandatory : {
				type : Boolean,
				default : false
			},
			type : {
				type : String,
				default : 'text'
			},
			placeholder : String,
			handleSendSms : {
				type : Function,
				//return true if success
				default : function() {return true;}
			},
			ext : String,
            maxLength : {
                type : Number,
                default : 64
			}
		},
		data () {
			return {
                forbidden : false,
				value : '',
				focused : false,
				countDownValue : 0,
				errorMsg : ''
			}
		},
		watch : {
			value : function(n, o) {
				// 触发change事件和input事件
				this.$emit('change');
				this.value = this.value.substr(0, Math.max(1, this.maxLength));
				this.$emit('input', this.value,this);
			}
		},
		computed : {
			regex : function() {
                if(this.rule) {
                    return this.rule;
                }
				var validatorsArr = [];
				try {
					var ext = this.ext.replaceAll("'", '"');
					validatorsArr = JSON.parse(ext);
				} catch(e) {}
				
				return validatorsArr.length ? validatorsArr[0].regex.replace('\\\\', '\\') : null;
			},
			regexErrorTip : function() {
                if(this.ruleMsg) {
                    return this.ruleMsg;
                }
				var validatorsArr = [];
				try {
					var ext = this.ext.replaceAll("'", '"');
					validatorsArr = JSON.parse(ext);
				} catch(e) {}
				
				return validatorsArr.length ? validatorsArr[0].msg : null;
			},
			valid : function() {
				if (this.mandatory && !this.value) {
					this.errorMsg = '';//this.label + '必须填写';
					return false;
				}
				
				if (this.regex && !new RegExp(this.regex).test(this.value)) {
					this.errorMsg = this.regexErrorTip;
					return false;
				}
                
                //match exactly
                if (this.regex) {
                    var match = this.value.match(this.regex);
                    if (match == null || this.value != match[0]) {
                        this.errorMsg = this.regexErrorTip;
                        return false;
                    }
                }
				
				this.errorMsg = '';
				return true;
			}
		},
		methods : {
		    disableBtn : function (flag) {
				this.forbidden = flag;
            },
			setValue : function(v) {
				this.value = v;
			},
			getValue : function() {
				return this.value;
			},
			focus : function() {
				$(this.$refs.input).focus();
				return this;
			},
			blur : function() {
				$(this.$refs.input).blur();
				return this;
			},
			trigger : function(eName) {
				$(this.$refs.input).trigger(eName);
				return this;
			},
			resetValue : function(e) {
				this.value = '';
				$(this.$refs.input).focus();
				if (e) this.$emit('userchange', this.value, this);
				return this;
			},
			startCooldown : function() {
				this.countDownValue = this.cooldownSecs;
				var me = this;
				var pid = setInterval(function() {
					me.countDownValue--;
					if (me.countDownValue <= 0) clearInterval(pid);
				}, 1000);
			},
			cooldownImmediately : function() {
				this.countDownValue = 0;
			},
			sendSms : function() {
				// 发送验证码倒计时开始
				if (this.handleSendSms()) this.startCooldown();
			},
			onfocus : function() {
				clearTimeout(this._pid);
				this.focused = true;
                this.$emit('focus',this.value,this);
			},
			onblur : function() {
				var me = this;
				clearTimeout(this._pid);
				this._pid = setTimeout(function() {
					me.focused = false;
				}, 100);
			}
		},
		components : {},
		mounted : function() {
			$(this.$refs.input).attr('type', this.type);
			var me = this;
			$(this.$refs.input).keyup(function() {
				me.$emit('userchange', me.value, me);
			});
			$(this.$refs.input).on('input propertychange', function() {
				me.$emit('userchange', me.value, me);
			});
		}
	};
</script>

<style lang="less" rel="stylesheet/less" scoped>
	.am-icon-clear.am-icon {
		visibility: visible!important;
	}
	.am-list-label.nowrap {
		white-space: nowrap;
	}
	.am-list-item, .am-list-item button {
		height:.25rem;
	}
</style>
