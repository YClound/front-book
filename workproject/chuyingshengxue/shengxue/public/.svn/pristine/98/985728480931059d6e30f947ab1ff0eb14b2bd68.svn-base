$(function() {
    var TPL_BANNER = '<div class="swiper-slide" style="background-image: url({contentUrl});"> <a push-href="{linkUrl}" class=""></a> </div>';
    var TPL_ALLTAGS = '<li><a href="javascript:;" data-id="{id}" data-text="{text}" data-source="{text}" class="item handle">{text}</a></li>';
    var TPL_MYTAGS = '<li><a href="javascript:;" data-id="{id}" data-text="{text}" data-source="{source}" class="item handle">{text}</a><a href="javascript:;" class="btn-del"></a>';
    var module = {
        init:function(){
            this.render();
            this.bind();
            this.sync();
        },
        request:function(){},
        render:function(){
            this.renderBanner();
            this.renderMsg();
            this.renderJianRen();
            this.renderTags();
        },
        bind:function(){
            this.bindMsg();
            this.bindCare();
            this.bindTagView();
            //this.bindWaterFull();

        },
        sync:function(){
            this.showEveryDay();
        },
        bindWaterFull:function(config){
            this.waterfull && this.waterfull.stop();
            this.waterfull = new Tiles(document.getElementById('waterfull'));
            this.waterfull.init(config);
            //console.log(11,this.waterfull)
        },
        renderTags:function(){
            var self = this;
            var first = $('#mytags .first').closest('li')[0].outerHTML;
            var ID = 0;
            // mytags
            APP.get('/openapi/IndexPageComponent/getUserTags',{}).done(function(res){
                var tags = res.msg || '';
                var tagsArr = tags.split(',');
                var list = [];
                $.each(tagsArr,function(i,item){
                    list.push({
                        id:'tags_'+ (ID++),
                        text:dm.mytag(item)
                    })
                });

                var html = APP.iTemplate.makeList(TPL_MYTAGS,list);
                $('#mytags').html(first+html);
                self.updateMyTags(false);

                self.bindWaterFull({ajaxParams:{start:0,limit:10,tags:tags}});
            });
            // alltags
            APP.get('/openapi/IndexPageComponent/getAllTags',{}).done(function(res){
                var list = [];
                var result = res.msg || {};
                var buffer = [];
                $.each(result,function(key,item){
                    var json = {
                        from:key,
                        tags:item
                    };
                    var html = template('TPL_TAG_PANEL',json);
                    buffer.push(html);
                });
                $('#alltags').html(buffer.join(''));
            });
        },
        // return [] 
        getMyTags: function(){
            var tags = [];
            $('#mytags .handle').each(function(i,item){
                var data = $(item).data() || {};
                var text = data.text;
                tags.push('#'+text+'#');
            });
            return tags;
        },
        updateMyTags:function(force){
            var ret = [],buffer = [];
            var tags = [];
            var tpl = '<a href="javascript:;" data-id="{id}" data-text="{text}" class="item handle">{text}</a>';
            $('#mytags .handle').each(function(i,item){
                var data = $(item).data() || {};
                var text = data.text;
                var html = APP.iTemplate.sub(tpl,data);
                ret.push(data.id);
                buffer.push(html);
                tags.push('#'+text+'#');
            });
            $('#mytags2').html(buffer.join(''));
            // 提交更新
            force && APP.post('/openapi/IndexPageComponent/operateUserTagse',{tags:tags.join(',')}).done(function(){
                //dm.notice('保存成功');
            });
        },
        filterMyTags:function(){

        },
        //
        bindTagView:function(){
            var self = this;
            // 显示tag
            $(document).on('click','.m-youlike-addbtn',function(e){
                $('.m-main').hide();
                $('.m-tags-container').show();
            });
            // 关闭tag
            $(document).on('click','.m-tags-container .btn-close',function(e){
                $('.m-main').show();
                $('.m-tags-container').hide();
                var tags = self.getMyTags().join(',');
                console.log(1111,tags)
                self.bindWaterFull({ajaxParams:{start:0,limit:10,tags:tags}});
            });
            // 删除我的tag
            $(document).on('click','#mytags .btn-del',function(e){
                var parent = $(e.currentTarget).closest('li');
                var target = parent.find('.handle');
                var data = target.data();
                var source = data.source || '';
                var tpl = TPL_ALLTAGS;
                var html = APP.iTemplate.sub(tpl,data);

                parent.remove();
                self.updateMyTags(true);

                // 在相应类别里插入
                if(source) {
                    $('.tags-panel2-ul').each(function(i,item){
                        var data = $(item).data();
                        if(source == data.source){
                            $(item).prepend(html)
                        }
                    });
                }
                
            });
            // 添加tag
            $(document).on('click','#alltags .handle',function(e){
                // 判断是否满足个数
                var total = $('#mytags .handle').size();
                if(total>=9){
                    dm.notice('标签不能超过9个呢');
                    return;
                }
                var target = $(e.currentTarget);
                var parent = target.closest('li');
                var data = target.data();
                var tpl = TPL_MYTAGS;
                var html = APP.iTemplate.sub(tpl,data);

                $('#mytags').append(html);
                parent.remove();
                // 
                self.updateMyTags(true);
            });

            // 筛选tag
            $(document).on('click','#mytags2 .handle',function(e){
                return;
                var top = $(window).scrollTop();
                var target = $(e.currentTarget);
                var data = target.data() || {};
                target.addClass('active').siblings().removeClass('active');
                self.waterfull.init({ajaxParams:data,callback:function(){
                    //$(window).scrollTop(top);
                }})
            });

            var el = document.getElementById('mytags');
            var sortable = Sortable.create(el,{
                handle: ".handle",
                chosenClass: "sortable-chosen",  // Class name for the chosen item
                dragClass: "sortable-drag",  // Class name for the dragging item
                onUpdate: function (evt){
                    var itemEl = evt.item; // 当前拖拽的html元素

                },
                onEnd:function(){
                    self.updateMyTags(true);
                }
 
            });
        },
        // 点击完消息应该消失
        bindMsg:function(){
            $(document).on('click','#notice a',function(e){
                $('#notice').hide();
                // 修改为已阅读
                APP.get('../api/message/done.json');
            });
        },
        // 点击关注
        bindCare:function(){
            $(document).on('click','.btn-care',function(e){
                var $target = $(this);
                var id = $target.data('id');
                if(!$target.hasClass('done')){
                    APP.post('/openapi/IndexPageComponent/attentionUser',{focusUserId:id}).done(function(){
                        $target.html('已关注').addClass('done');
                        
                    });
                }
            });
        },
        // 获取荐人
        renderJianRen:function(){
            APP.get('/openapi/IndexPageComponent/getUserListByHeat',{start:0,limit:3}).done(function(res){
                var list = res.msg || [];
                var html = APP.makeList('TPL_JIANREN',list) || '';
                
                html && $('#jianren').html(html);
            });
        },
        // 获取通知
        renderMsg:function(){
            APP.get('/openapi/IndexPageComponent/getNewMessageCount').done(function(res){
              
                var total = res.msg || 0;
                var html = total>9?total+''+'+':total+'';
                $('#notice .count').html(html);
                total && $('#notice').css('opacity',1)
                 
            });
        },
        // 渲染轮播图
        renderBanner:function(){
            
            APP.get('/openapi/IndexPageComponent/getContentManageList?start=0&type=advertisement&limit=2').done(function(res){
             
                var list = res.msg || [];
                var html = APP.iTemplate.makeList(TPL_BANNER,list);

                $('#swiperBanner').append(html);

                var mySwiper = new Swiper ('.swiper-container', {
                    loop: true,
                    autoplay: 4000,
                    pagination: '.swiper-pagination'
                });   
            });
        },
        // 每天第一次进入弹框
        showEveryDay:function() {
            var d = new Date();
            var year = d.getFullYear(),month = d.getMonth(),date = d.getDate();
            var _d = year+'/'+month+'/'+date;
            var local = localStorage.getItem('isFirst');

            if(!local || local != _d){
                localStorage.setItem('isFirst',_d);
                // todo
                // alert('第一次')
            }
            
        },
        destroy:function(){}
    };

    module.init();
    
});

$(document).on('jsfrbridgeready', function() {
	
	$(document.body).on('click', 'a[push-href]', function(e) {
		JSFRBridge.call('pushWindow', {
		    url : $(this).attr('push-href')
        })
	});
    
    //TODO
    /*JSFRBridge.call('toast', {
        type : 'none',
        text : '你好',
        duration : 100
    }, function(json) {
        alert(json.msg)
    });*/
});