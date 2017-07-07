<template>
	<footer class="footer am-ft-12 company-info " auto-adj="company-info" :class="{'company-info-abs':!static}">
		<slot></slot>
		<p class="cust-company-info company-copyright-text"></p>
		<section v-if="!removeVerisign" class="safety-msg cust-certification"><span class="i-login-safety">
            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAiCAYAAACnSgJKAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkQ0OUEzNjg3NkZERDExRTU4MEY1ODM2MTJCRTg2NERBIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkQ0OUEzNjg4NkZERDExRTU4MEY1ODM2MTJCRTg2NERBIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RDQ5QTM2ODU2RkREMTFFNTgwRjU4MzYxMkJFODY0REEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RDQ5QTM2ODY2RkREMTFFNTgwRjU4MzYxMkJFODY0REEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7SIuATAAADGklEQVR42ryYa0sUURjHZ9312tK6VitJRRFF28VuZlL2QqkgikCiBD9A9KJ30TfpIxSkUaAVRS+8QPf7HZQupGurdpHWytXd3P4P/A8chpnZ2VlnD/xQZ86c/3PO8zznPMeA0fPT8NACoAlsAGPgXu5kdKHQQco8CMs3x8l2cBh0BK5MB/0WrwZdYA8IgYdARHfKcxhQ7Zd4AzgNNoMcuAoegR4gSx4HZ2DAKve+y+/zCnAAtIIa8A1cBzNanzA4BurBLHgMBhEHf72Ki2gzaAFR/v1Mggv8s1nFvfxG3v8BT8Q1MCLlVrxKE41QNAGGwJSL1awF+8FGkAFp8FZcBCPG7cQjtHw3l1FER8F9kPSQFVGmY5wxkmFaPhdjYMicEt8i6UKfloNPjORJo/gmY25jWtbQiF+gW4mfBzEwTFG3O88prtAlzi5fZq0Du5g5X8ro4zBf3ipAWNpqRniFi76Sjh/BDe4RMRHMah2Chv9tQV+KrLZkoRKIq40tp36ZpQFLSiBeTq05JT7DB0tLIB6mVlqJT9MXkRKIRyieUj7+TvE6i86dIN9hcdbm+aRFGtZS64ea+Tj345U2hYPXZvVtjFoTauZjfCAvKiUYtM6XHQY/x58XTN84GdNArYQecEkuz1of/V3PgkROvKReTAwz5+M+im/i3j4iNZ8u/hLMc/8N+yAcYhUkE3xlLqMk3T7QH80+iDfyDJCa4LNVDTfI2TcyJRarVXJCEpRDWPKclbhULO84+4NFppne2rWK6I1T9SrHaoobS1OeQRPcSOYd+uxgoP0GvWrWduIi3Mvaq5UBaNe6wUWHQkJE23hw3YTwhJu6/T0LxjRL4jUellrKpiMUHoDwi0IuDf3gKVOjg9Womxakj9s14QG73LNrspR9jNB94ChYAR7o1YipLeds61gk9kH4tVPiG3kMuM04OMQAFBfckVPJdMFo4Z0ty7PiGoS/FntdUk1ET3B2YrTMSPy4nvV+kDEi1W8/hDOLcVczbxZtFKtiSZRlqo3IikB0yu1gAY//HFjGy+NWXjDuQnS00EH+CzAA6vrjpfY84QYAAAAASUVORK5CYII="></span>通过VeriSign国际安全认证
		</section>
	</footer>
</template>

<script>
    export default {
        props : {
            static : {
                type : Boolean,
                default : false
            },
            removeVerisign : Boolean,
            adjustTo : String
        },
        data () {
            return {
                wh:0,
                limit : 10
            }
        },
        methods : {
            show : function() {
                $(this.$el).show();
            },
            hide : function() {
                $(this.$el).hide();
            },
            _hasScrollBar : function() {
                return document.body.scrollHeight > document.documentElement.clientHeight;
            },
            adjust : function() {
                if(this.static) {
                    $(this.$el).removeClass('company-info-abs');
                    return;
                }
                var me = this;

                try {
                    var el = $(this.$el);
                    var adjustToEl = $(this.adjustTo);
                    if (adjustToEl && adjustToEl.length > 0){

						var clientHeight =  document.documentElement.clientHeight,
							adjustHeight = adjustToEl.height(),
							elHeight = el.height();
						//alert(clientHeight+','+adjustHeight+','+elHeight)
						if (document.documentElement.clientHeight - adjustToEl.height() >= el.height()) {
							//no abs
							el.addClass(el.attr('auto-adj') + '-abs');
							me.limit = 1;
						} else {
							el.removeClass(el.attr('auto-adj') + '-abs');
						}


                        el.removeClass('hidden');
                        return;
                    }
                } catch(e) {}

                var el = $(this);
                el[this._hasScrollBar() ? 'removeClass' : 'addClass'](el.attr('auto-adj') + '-abs');
                el.removeClass('hidden');

            }
        },
        components : {},
        mounted : function() {
            var me = this;
            var el = $(this.$el);
            this.wh = window.innerHeight;
            // 假如是flex布局 不需要计算
            if(this.static) {
                $(this.$el).removeClass('company-info-abs');
                return;
            }
            var me = this;
            //this.adjust();
            var tick = function () {
                me.adjust();
                me.limit--;
                if(me.limit > 0) {
                    setTimeout(tick,1000);
                }

            };
            tick();

            $(window).on('resize',function () {
//                /*if(window.innerHeight<me.wh){
//                    el.hide()
//                }else {
//                    el.show()
//                }*/
//
                me.adjust();
            });
            $(document.body).on('touchmove', function() {
                setTimeout(function() {
                    me.adjust();
				}, 500);
			});
        }
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
	.footer {
		padding: .15rem 0;
		color: #999;
		text-align: center;
		width: 100%;
	}

	footer {
		&.company-info-abs {
			position: absolute;
			bottom: 0rem;
		}
	}

	.company-info {
		color: #9B9B9B !important;
		font-size: .12rem !important;
	}
	.company-info-abs {
		position : absolute;
		bottom : 0rem;
	}

	.safety-msg img {
		height: .18rem;
		vertical-align: middle;
		margin: 0 .02rem .01rem 0;
	}

	.company-copyright-text:before {
		content : '本服务由杭州数立信息技术有限公司提供';
	}
</style>
