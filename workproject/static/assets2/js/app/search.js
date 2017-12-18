var search_query = '';
var split_query = '';
var ajax_template = '';
$(function()
{
	$('#list_nav a').click(function ()
	{	
		// 获取a标签的href值
		window.location.hash = $(this).attr('href').replace(/#/g, '');
		console.log(window.location.hash);
		
		$('#aw-search-type').html($(this).text()); //设置搜索类型是获取点击的a链接的内容
		// 搜索结果先显示一张加载图片
		$('#search_result').html('<p style="padding: 15px 0" align="center"><img src="' + G_STATIC_URL + '/common/loading_b.gif" alt="" /></p>');
		
		$('#search_result_more').attr('data-page', 1).click();   //data-page=1页面刷新先加载一个页面
	});
	// 点击加载更多
	$('#search_result_more').click(function()
	{
		var _this = this,
			page = parseInt($(this).attr('data-page')) || 1
		// 请求地址	
		var request_url = '../assets2/ajax/search/search-type-' +  window.location.hash.replace(/#/g, '') + ".json";
		
		$(this).addClass('loading');
		dm.doAjax("get",request_url, {"q":search_query,"page" : page} ,function (response)
		{
			console.log(response);
			// 当请求页面不为空时
			if (response)
			{
				// 动态加载模板
				var template ="", template1;
				for (var  i = 0; i < response.rsm.length; i++) {
					if(response.rsm[i].type == "user"){
					 	template1 = Hogan.compile(AW_TEMPLATE.searchUser).render(
	          {
	          	'uhref' : response.rsm[i].uhref,
	          	'type' : response.rsm[i].type,
	            'uuid': response.rsm[i].uuid,
	            'user_pic' :response.rsm[i].user_pic,
	            'search_query' : search_query,
	            'discuss_count' : response.rsm[i].discuss_count,
	            'thank_count' : response.rsm[i].thank_count,
	            'prestige_count' : response.rsm[i].prestige_count
	          })				
					}


					if(response.rsm[i].type == "topic"){
						template1 = Hogan.compile(AW_TEMPLATE.searchUser).render(
	          {
	          	'uhref' : response.rsm[i].uhref,
	          	'type' : response.rsm[i].type,
	            'uuid': response.rsm[i].uuid,
	            'user_pic' :response.rsm[i].user_pic,
	            'search_query' : search_query,
	            'discuss_count' : response.rsm[i].discuss_count,
	            'thank_count' : response.rsm[i].thank_count,
	            'prestige_count' : response.rsm[i].prestige_count
	          })
					}

					template = template + template1;
					console.log(template);
				}


				if ($(_this).attr('data-page') == 1)
				{  					
					$('#search_result').html(template);				
				}
				else
				{
					$('#search_result').append(template);
				}
				
				$('#search_result .aw-title a').highText(split_query, 'span', 'aw-text-color-red');
				// 点击加载更多页数增加一页
				$(_this).attr('data-page', parseInt($(_this).attr('data-page')) + 1);
				
			}
			else
			{
				// 当请求页面为空，并且是第一页时 搜索结果页面显示没有内容
				if ($(_this).attr('data-page') == 1)
				{
					$('#search_result').html('<p style="padding: 15px 0" align="center">' + _t('没有内容') + '</p>');
				}
				//请求页面为空时，更多按钮设置为不可用
				$(_this).addClass('disabled');
				
			}
			
			$(_this).removeClass('loading');
			
		});
		
		return false; 
	});
	// 判断hash
	switch (window.location.hash)
	{	
		case '#questions':
		case '#topics':
		case '#users':
		case '#articles':
			$("#list_nav a[href='" + window.location.hash + "']").click();
		break;
		//默认情况下 显示全部搜索到信息
		default:
			$("#list_nav a[href='#all']").click();
		break;
	}
});