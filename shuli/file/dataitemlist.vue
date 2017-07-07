<template>
	<natural-person-prechecker @completed="completed">
		<page-title title="选择登录邮箱" />
		<grouped-item-list
				ref="list"
				type="flat"
				hot-title="请选择您的邮箱"
				:icon-renderer="iconRenderer"
				common-title="常用邮箱"
				search-tip="可输入中文、拼音进行查找"
				load-url="/email/getDataItemList.json"
				@focusSearch="focusSearch"
				@blurSearch="blurSearch"
				@loaded="loaded"
				@itemclicked="itemClicked"></grouped-item-list>
		
		<company-info ref="companyInfo" remove-verisign></company-info>
	</natural-person-prechecker>
</template>

<script>
	import naturalPersonPrechecker from '../../../components/h5/natural-person-prechecker.vue';
	import companyInfo from '../../../components/h5/company-info.vue';
	import groupedItemList from '../../../components/h5/grouped-item-list.vue';
	import pageTitle from '../../../components/page-title.vue';
	
	export default {
		data () {
			return {
                iconRenderer : function(item) {
                    return `${CDN_URL}/images/${item.itemCode}.png`;
                }
			}
		},
		methods : {
		    //搜索框获取焦点隐藏底部公司信息
            focusSearch : function() {
                this.$refs.companyInfo.hide();
            },
            //搜索框失去焦点显示底部公司信息
            blurSearch : function() {
                this.$refs.companyInfo.show();
            },
            //显示dom
			loaded : function() {
				$(document.body).show();
			},
			//选择邮箱跳转到loginform页面传递参数code
			itemClicked : function(code) {
				Utils.jumpTo('/loginform/' + code);
			},
			//自然人信息采集成功，点击下一步，隐藏表单显示搜索框、加载常用邮箱列表
            completed : function() {
                var me = this;
                setTimeout(function() {
                	//加载邮箱列表
		            me.$refs.list.startToLoad();
                }, 10);
            }
		},
		components : {
			groupedItemList, companyInfo, naturalPersonPrechecker, pageTitle
		}
	};
</script>

<style lang="less" rel="stylesheet/less">
	.list-common-title {
		display : none!important;
	}
</style>
