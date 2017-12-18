define(function(require, exports, module){
    
    /* 项目核心数据审核以及修改 */
    $(function(){

        $('.applyLimitForm').on('success',function(){
            dm.notice('修改成功');
        });
        // 权限修改的时候提交
        $('.applyLimitForm input').on('change',function(){
            $(this).closest('form').submit();
        });

        // 同意申请
        $(document).on('click','.btn-agree',function(){
            var data = $(this).data() || {};
            var $this = $(this);
            var url = $(this).data('url') || api.agreeProjectApply;

            dm.confirm('确认同意吗？',function(){
                dm.getJSON(url, data).done(function(){
                    dm.notice('已同意');
                    $this.prop('disabled', true).text('已同意');

                });
            })
                
        });
        

    });


});