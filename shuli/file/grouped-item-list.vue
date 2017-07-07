<template>
	<div>
		<div @touchmove.prevent.stop class="am-search am-input-autoclear">
			<!-- 搜索框 -->
			<div class="am-search-input">
				<div class="am-search-icon"><i class="am-icon search-inpage"></i></div>
				<input @focus="focusSearch" @blur="blurSearch" class="am-search-value" type="search" :placeholder="searchTip" v-model.trim="searchStr">
				<!-- 清空搜索 -->
				<div class="am-search-clear" @click="clearSearch(true)">
					<i class="am-icon-clear am-icon" style="visibility:visible;" v-show="searchStr"></i>
				</div>
			</div>
			<!-- 取消搜索 -->
			<div class="am-search-button" style="display:block;" v-show="searchStr">
				<button @click="clearSearch" type="button">取消</button>
			</div>
		</div>
		<div class="scroll-panel">
			<!-- 列表 -->
			<div class="am-group-select-container" :type="type" v-show="!searchStr">
				<div v-if="hotItems.length" class="am-list" style="padding:0">
					<div class="am-list-header" role="hot-title">{{hotTitle}}</div>
					<!--hot-->
					<div class="am-list-body" category="hot">
						<div v-for="item in hotItems" hot="" class="am-list-item cust-itemlist-item-hot" :keywords="item.itemName + ',' + item.keywords">
							<a role="item" :disabled="item.itemStatus=='DISABLE' || item.itemStatus=='CLOSE'" :code="item.itemCode" href="javascript:void(0)">
								<img v-if="iconRenderer" :src="iconRenderer(item)" alt="">
								{{item.itemName + (item.itemStatus == 'DISABLE' ? '(维护升级中)' : (item.itemStatus == 'CLOSE' ? '(渠道关闭)' : ''))}}
							</a>
						</div>
					</div>
				</div>
				<div class="am-list" style="padding:0rem;">
					<div class="am-list-header list-common-title">{{commonTitle}}</div>
					<!--groups-->
					<div class="am-list-body" category="groups">
						<template v-for="group in groupArr">
							<template v-if="groupedItemsMap[group.name].length > 0">
								<div class="am-list-group" :name="group.name">
									<!--group head-->
									<div class="am-list-item group-header cust-itemlist-group" @click="toggleGroup(group)">
										<div class="am-list-content">{{group.name}}</div>
										<div class="am-list-arrow">
											<span class="am-icon arrow vertical"></span>
										</div>
									</div>
									<!--sub items-->
									<div class="am-list-sub" :name="group.name">
										<div v-for="item in groupedItemsMap[group.name]" class="am-list-item group-item cust-itemlist-item" :keywords="item.itemName + ',' + item.keywords">
											<a role="item" :disabled="item.itemStatus=='DISABLE' || item.itemStatus=='CLOSE'" :code="item.itemCode" href="javascript:void(0)">
												<img v-if="iconRenderer" :src="iconRenderer(item)" alt="">
												{{item.itemName + (item.itemStatus == 'DISABLE' ? '(维护升级中)' : (item.itemStatus == 'CLOSE' ? '(渠道关闭)' : ''))}}
											</a>
										</div>
									</div>
								</div>
							</template>
						</template>
					</div>
				</div>
			</div>
			<!-- 搜索结果 -->
			<div class="am-list" role="search" v-show="searchStr">
				<div class="am-list-header">{{searchResultTitle}}</div>
				<div class="am-list-body" role="search-result">
					
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		props : {
		    expireSeconds : {
		        type : Number,
		        default : 300 //default 5 mins
            },
			hotTitle : String,
			commonTitle : String,
			searchTip : String,
			type : {
			    type : String,
				default : 'dock' //dock-横向堆叠，每行3个/flat-垂直平铺
            },
			iconRenderer : {
			    type : Function,
				default : null
			},
			searchResultTitle : {
				type : String,
				default : '搜索结果'
			},
			loadUrl : String
		},
		data() {
			return {
				searchStr : '',
				items : [],
				hotItems : [],
				groupArr : [],
				groups : [],
				orgCode : '',
				groupedItemsMap : []
			};
		},
		components : {},
		watch : {
			// 过滤搜索条件
			searchStr : function(searchStr) {
				if (!searchStr) {
					$('.am-list-body[role=search-result]').empty();
					return;
				}
				var checker = {};
				$('.am-list-body[role=search-result]').empty().append($('.am-list-item[keywords*="' + searchStr.toUpperCase() + '"]').filter(function() {
					var kw = $(this).attr('keywords');
					if (checker[kw]) return false;
					checker[kw] = '~';
					return true;
				}).clone());
				
			}
		},
		methods : {
			// 输入框获取焦点触发focusSearch
		    focusSearch : function() {
		        this.$emit('focusSearch');
		    },
            blurSearch : function() {
                this.$emit('blurSearch');
            },
            //开始加载邮箱列表
		    startToLoad : function() {
                this._registerScrollFn();
                var me = this;
            	// 绑定点击列表项事件
                $(document.body).on('click', 'a[role=item]:not([disabled])', function() {
                    var el = $(this);
                    el.addClass('active');
                    setTimeout(function() {
                        el.removeClass('active');
                    }, 100);
                    me.$emit('itemclicked', $(this).attr('code'));
                });
            
                //-----start 2 get meta datas..
			    this.__initDataFromServer();
		    },
		    // 获取邮箱列表的数据
			__initDataFromServer : function() {
                this.__renderTime = new Date().getTime();
		        var me = this;
		        
                $.get(this.loadUrl, function(data) {
                    var items = [];
                
                    me.orgCode = data.msg.orgCode;
                    $.each(data.msg.list, function() {
                        items.push({
                            itemOrder : this.itemFirstOrder,
                            itemCode : this.itemCode,
                            itemType : this.itemType,
                            itemName : this.itemName,
                            itemStatus : this.itemStatus,
                            isHot : this.itemSecondOrder == '*',
                            groupName : this.groupName,
                            keywords : this.keywords,
                            groupOrder : this.itemThirdOrder,
                            status : this.status
                        });
                    });
                
                    me._loadItems(items);
                
                    me.$emit('loaded');
                });
			},
			clearSearch : function(focus) {
				this.searchStr = '';
				if (focus == true) {
					$(this.$el).find('.am-search-value').focus();
				}
			},
			toggleGroup : function(group) {
				var el = $('.am-list-group[name="' + group.name + '"]>.cust-itemlist-group');
                
				var groupEl = $('.am-list-group[name="' + group.name + '"]>.am-list-item');
                groupEl.addClass('active');
                setTimeout(function() {
                    groupEl.removeClass('active');
                }, 100);
				
				var sub = el.next();
				if (el.is('[expanded=true]')) {
					el.removeAttr('expanded');
					el.find('span.am-icon').removeClass('up');
					//collapse it!
					sub.height(0);
				} else {
					el.attr('expanded', true);
					el.find('span.am-icon').addClass('up');
					//expand it!
					sub.height(sub[0].scrollHeight).show();
					//auto scroll the group header to top.
					$('.scroll-panel').scrollTo({
						toT : el.position().top + $('.scroll-panel')[0].scrollTop
					});
				}
				
			},
			_loadItems : function(items) {
		        var me = this;
				this.items = items;
				//group items...
				var groups = {};
				var groupedItemsMap = {};
				
				$.each(items, function() {
					function addIntoGroup(item) {
						if (item.isHot) {
							var arr = groupedItemsMap['*'];
							if (!arr) {
								arr = [];
								groupedItemsMap['*'] = arr;
							}
							arr.push(item);
						}
						
						var arr = groupedItemsMap[item.groupName];
						if (!arr) {
							arr = [];
							groupedItemsMap[item.groupName] = arr;
						}
						arr.push(item);
						
					}
                    
					switch (this.status) {
						case 0:
						    if (this.itemStatus == 'PREVIEW' && me.orgCode != 'DATAHUB') return;
						    break;
						case 1:
						    return;
						case 2:
						    if (this.itemStatus == 'PREVIEW') return;
	                        this.itemStatus = 'CLOSE';
						    break;
					}
					
					addIntoGroup(this);
					groups[this.groupName] = {
						order : this.groupOrder,
						name : this.groupName
					};
				});
				
				//start 2 sort
				for (var key in groupedItemsMap) {
					if (key == null || key == '') continue;
					var arr = groupedItemsMap[key];
					if (arr && arr.length == 0) continue;
					
					arr.sort(function(o1, o2) {
						return o1.itemOrder < o2.itemOrder ? -1 : 1;
					});
				}
				
				//insert hot items
				var hotItems = groupedItemsMap['*'] || [];
				hotItems.sort(function(o1, o2) {
					return o1.itemOrder < o2.itemOrder ? -1 : 1;
				});
				
				this.hotItems = hotItems;
				
				//handle groups
				var groupArr = [];
				for (var key in groups) {
					if (key == null || key == '') continue;
					groupArr.push(groups[key]);
				}
				
				groupArr.sort(function(o1, o2) {
					return o1.order < o2.order ? -1 : 1;
				});
				
				this.groupArr = groupArr;
				this.groups = groups;
				this.groupedItemsMap = groupedItemsMap;
				
			},
			_registerScrollFn : function() {
		        if ($.fn.scrollTo) return;
                $.fn.scrollTo = function(options) {
                    var defaults = {
                        toT : 0,    //滚动目标位置
                        durTime : 200,  //过渡动画时间
                        delay : 30,     //定时器时间
                        callback : null   //回调函数
                    };
                    var opts = $.extend(defaults, options),
                        timer = null,
                        _this = this,
                        curTop = _this.scrollTop(),//滚动条当前的位置
                        subTop = opts.toT - curTop,    //滚动条目标位置和当前位置的差值
                        index = 0,
                        dur = Math.round(opts.durTime / opts.delay),
                        smoothScroll = function(t) {
                            index++;
                            var per = Math.round(subTop / dur);
                            if (index >= dur) {
                                _this.scrollTop(t);
                                window.clearInterval(timer);
                                if (opts.callback && typeof opts.callback == 'function') {
                                    opts.callback();
                                }
                                return;
                            } else {
                                _this.scrollTop(curTop + index * per);
                            }
                        };
                    timer = window.setInterval(function() {
                        smoothScroll(opts.toT);
                    }, opts.delay);
                    
                    return _this;
                };
			}
		},
        activated : function() {
            //check if the render time is too long ago?
            if (new Date().getTime() - this.__renderTime > 1000 * this.expireSeconds) {//15mins will force 2 reload!
                this.__initDataFromServer();
                this.searchStr = '';
            }
        }
	};
</script>

<style lang="less" rel="stylesheet/less" scoped>
	*[type=dock] {
		div[category=hot] {
			padding: 0 .15rem;
		}
		
		div[category=hot] .am-list-item {
			padding-right: 0;
			-webkit-border-radius: .03rem;
			-moz-border-radius: .03rem;
			border-radius: .03rem;
			display: inline-block !important;
			background: #FFF !important;
			padding: 0 !important;
			width: 29% !important;
			margin: 1% 3% 1% 0 !important;
			text-align: center !important;
		}
		
		div[category=hot] a {
			line-height: .33rem;
			text-align: center;
			background-color: white;
			padding: 0;
			color: #333;
			font-size : .15rem;
			white-space: nowrap;
			text-overflow: ellipsis;
			overflow: hidden;
		}
		
		div[category=hot].am-list-body {
			> .am-list-item {
				padding: 0rem !important;
				overflow: hidden;
			}
		}
	}
	
	span.am-icon:before {
		-webkit-transition: all .3s;
		-moz-transition: all .3s;
		-ms-transition: all .3s;
		-o-transition: all .3s;
		transition: all .3s;
	}
	
	//searchbar styles
	.am-search {
		.am-search.am-input-autoclear.search-input {
			-webkit-appearance: textfield;
			-webkit-box-sizing: content-box;
		}
		input::-webkit-search-decoration,
		input::-webkit-search-cancel-button {
			display: none;
		}
		.am-search-button {
			font-size : .14rem!important;
			white-space:nowrap;
			>button {
				color : #3ba2ed;
				border-width : 0rem;
				padding-left : 0rem;
				padding-right : 0rem;
				background-color: transparent;
			}
		}
	}
	
	.scroll-panel {
		position: absolute;
		top: .43rem;
		left: 0rem;
		right: 0rem;
		bottom: 0rem;
		z-index: 2;
		padding-bottom: .5rem;
		overflow-y: scroll;
		-webkit-overflow-scrolling: touch;
	}
	
	.common-link() {
		color: #333;
		width: 100%;
	}
	
	.active-link() {
		background-color: #E8E8E8 !important;
		color: #555;
	}
	
	body.uc .scroll-panel {
		.am-list-item {
			a > .am-icon {
				margin-right: .28rem;
			}
		}
		.am-list-sub {
			> .am-list-item {
				a .am-icon {
					margin-right: .38rem !important;
				}
			}
		}
	}
	body.uc {
		.am-search.am-search-input {
			width : 100%!important;
		}
	}
	
	.am-list-item {
		padding: 0rem !important;
		overflow-x: hidden !important;
		> a {
			line-height: .43rem;
			padding-left: .15rem;
			padding-right: .15rem;
			display: block;
			width: 100%;
			.common-link();
			&.active {
				.active-link();
			}
			> .am-icon {
				float: right;
				display: inline-block;
				margin-top: .15rem;
			}
			img {
				width: .29rem;
				height: .29rem;
				margin-top: -.02rem;
				margin-right: .05rem;
			}
		}
		> a[disabled] {
			color: #B0B0B0;
			&:active {
				color: #B0B0B0;
				background-color: transparent !important;
			}
		}
	}
	
	.am-list-group {
		> .am-list-item {
			padding-left: .15rem !important;
			padding-right: .15rem !important;
			line-height: .43rem;
			&.active {
				background-color: #E9E9E9 !important;
			}
			.am-list-content {
				font-size: .16rem;
			}
		}
	}
	
	.am-list-sub {
		overflow: hidden;
		//-webkit-transform: translateZ(0);
		-webkit-transition: height 0.3s;
		-moz-transition: height 0.3s;
		-ms-transition: height 0.3s;
		-o-transition: height 0.3s;
		transition: height 0.3s;
		height: 0rem;
		> .am-list-item {
			padding: 0rem !important;
			margin: 0rem !important;
			margin-top: -.01rem !important;
			background-color: transparent !important;
			a {
				line-height: .42rem;
				display: block;
				padding-left: .25rem;
				padding-right: .25rem;
				img {
					margin-top: -.04rem;
				}
				> .am-icon {
					margin-right: -.12rem;
				}
			}
		}
	}
	
	div[category=groups].am-list-body {
		a {
			background-color: #F5F5F5;
			border-bottom: .01rem solid #E5E5E5 !important;
			margin-top: .01rem !important;
		}
		.am-list-item.group-header {
			margin-top : -1px;
			border-bottom : 1px #eee solid;
			border-top : 1px #eee solid;
			background : white;
		}
		.am-list-item.group-item {
			background-color: green;
		}
	}
	
	.am-list-body[role=search-result]:empty:before {
		display: block;
		font-size: .13rem;
		text-align: center;
		color: #666;
		content: '没有找到结果';
		margin-top: .15rem;
	}
</style>
