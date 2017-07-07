<template>
	<div class="am-list-item">
		<div :style="'width:' + labelWidth + 'rem;'" class="am-list-label nowrap cust-field-label">{{label}}</div>
		<div class="am-list-control">
			<select ref="select" :id="guid" :name="name" @change="change" v-model="value">
				<option v-for="opt in opts" :value="opt.value">{{opt.label}}</option>
			</select>
		</div>
		<label :for="guid">
		<div class="am-list-arrow"><span class="am-icon arrow vertical"></span></div>
		</label>
	</div>
</template>

<script>
	export default {
		props : {
            labelWidth : {
                type : Number,
                default : 1
            },
			label : String,
			name : String,
			itemCode : String,
			loginType : String,
			option : {
				type : String,
				value : '[]'
			}
		},
		data () {
			return {
				errorMsg : '',
				focused : false,
				valid : true,
				value : '',
				guid : Math.random() + '_sel'
			}
		},
		computed : {
			//根据option(字符串拼接的json数据)计算opts
			opts : function() {
				try {
					return JSON.parse(this.option);
				} catch(e) {
					return [];
				}
			}
		},
		methods : {
			resetValue : function() {},
			focus : function() {
				$(this.$refs.select).focus();
				return this;
			},
			blur : function() {
				$(this.$refs.select).blur();
				return this;
			},
			trigger : function(eName) {
				$(this.$refs.select).trigger(eName);
				return this;
			},
			change : function() {
				this.$emit('userchange');
			}
		},
		components : {},
		mounted : function() {
			//set default value
			this.value = this.opts.length ? this.opts[0].value : '';
		}
	};
</script>

<style lang="less" rel="stylesheet/less" scoped>
</style>
