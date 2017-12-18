define(function(require, exports, module) {
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');
    require('header');
    require('create.common');
    console.log("测试");
    
    $(function(){
    	//出让股权
    	var $sellShare=$('#sell-share');
    	var $projectValue=$('#project-value');
    	var $financeTotal=$('#finance-total');
    	$projectValue.add($financeTotal).on('input propertychange',function(){
    		var projectValue=parseInt($projectValue.val(),10) || 0,
    		    financeTotal=parseInt($financeTotal.val(),10) || 0;
    		if(projectValue && financeTotal){
    			$sellShare.val(Math.round(financeTotal / projectValue * 10000)/100);
    		}else{
    			$sellShare.val('');
    		}
    	});

    	//是否允许超募
    	var $inputOverFund=$('#raise-scal');
    	$('[name="super-raise"]').change(function(){
    		if(this.value==0){
    			$inputOverFund.val(0).prop('disabled',true);
    		}else{
    			$inputOverFund.prop('disabled',false);
    		}
    	});
    });
});