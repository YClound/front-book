define(function(require, exports, module) {
    require('m_common');
    require('dm.tab');
    var Util = require('dm.util');
    
    
    $(function () {
        $('.tab-title').ajaxTab();        
    });

    var recommendProjectTPL = '<div class="modal modal-recommend fade" tabindex="-1">'+
                                '<div class="modal-dialog">'+
                                    '<div class="modal-content">'+
                                        '<form action="{action}" method="get" class="ajax-form " >'+
                                            '<div class="modal-header">'+
                                                '<button type="button" class="close" data-dismiss="modal">&times;</button>'+
                                                '<h4 class="modal-title">向{TA}推荐项目</h4>'+
                                            '</div>'+
                                            '<div class="modal-body" >'+
                                                '<input type="hidden" name="uid" value="{uid}">'+
                                                '<input type="hidden" name="investorID" value="{investorID}">'+
                                                '<div class="check-project">'+
                                                    '<input type="radio" id="r1" name="proid" value="{proid}">'+
                                                    '<label for="r1">{title}</label>'+
                                                '</div>'+
                                                '<div class="ctrl-wrap" >'+
                                                    '<textarea name="msg" class="w-100 p-5" rows="3" placeholder="推荐给TA的原因"></textarea>'+
                
                                                '</div>'+
                                            '</div>'+
                                            '<div class="modal-footer">'+
                                                '<button type="submit" class="btn-primary pl-10 pr-10 mr-7">提交</button>'+
                                                '<button type="button" class="btn btn-o pl-10 pr-10" data-dismiss="modal">取消</button>'+
                                            '</div>'+
                                        '</form>'+
                                    '</div>'+
                                '</div>'+
                            '</div>';

    $.fn.recommendProject = function(){
        return this.each(function () {
            var $this = $(this);
           
            var url = api.userProjectList;
            var showModal = function(json){
                var pro = json.list[0];
                var data = {
                    action:api.userProjectRecommend,
                    TA:json.investorName,
                    investorID:json.investorId,
                    uid:json.uid,
                    proid:pro.id,
                    title:pro.title
                }
                //console.log(data);

                $('.modal-recommend').remove();
                var html = $(Util.substitute(recommendProjectTPL, data)).appendTo($('body'));
                
                html.find('form').on('success',function(e,res){
                    $('.modal-recommend').modal('hide');
                    dm.alert('推荐成功');
                });

                if(json.list.length>1){
                    var $first = html.find('.check-project').eq(0);
                    var buffer = ['<div>']
                    for(var i=0;i<json.list.length;i++){
                        var item = json.list[i];
                        var id = 'r' + i;
                        var html = '<div class="check-project">'+ '<input type="radio" id="'+id+'" name="proid" value="'+item.id+'" >'+ '<label for="'+id+'">'+item.title+ '</div>'
                        buffer.push(html);
                    }
                    buffer.push('</div>');

                    $first.replaceWith(buffer.join(''));
                    console.log(html)
                    $('.modal-recommend').find('input[type=radio]')[0].checked = true;

                }
                $('.modal-recommend').modal('show');




            };

            
            $this.on('click',function(){
                var _data = $this.data();
                if(!_data.investorId){
                    dm.alert('缺少参数投资人ID');
                    return;
                }
                dm.getJSON(url,{}).done(function(res){
                    
                    if(!res.data.uid){
                        dm.alert('请先登录再推荐');
                    }else if(!res.data.list.length){
                        dm.alert('您还没有审核通过的项目');
                    }else {
                        showModal($.extend({},res.data,_data));
                    }
                });
            });


        })
    }

    $(function(){
        $('[data-role="recommend"]').recommendProject();

    });



    
      
});