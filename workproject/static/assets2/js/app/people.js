$(document).ready(function () {	
	// 加载更多
	AWS.load_list_view(API.userActionsAnswer + '?uid=' + PEOPLE_USER_ID + '-actions-201', $('#bp_user_actions_answers_more'), $('#contents_user_actions_answers'));	// 参与的问题回复
			  
	AWS.load_list_view(API.userActionsQuestion + '?uid=' + PEOPLE_USER_ID + '-actions-101', $('#bp_user_actions_questions_more'), $('#contents_user_actions_questions'));	// 发起的问题
	
	AWS.load_list_view(API.userActionsArticle + '?uid=' + PEOPLE_USER_ID + '-actions-501', $('#bp_user_actions_articles_more'), $('#contents_user_actions_articles'));	// 发起的文章
		
	AWS.load_list_view(API.userActions + '?uid=' + PEOPLE_USER_ID + '-actions-' + ACTIVITY_ACTIONS, $('#bp_user_actions_more'), $('#contents_user_actions'));	// 个人动态
		
	AWS.load_list_view(API.peopleTypeFollow + '?uid=' + PEOPLE_USER_ID, $('#bp_user_follows_more'), $('#contents_user_follows'));	// 关注
	
	AWS.load_list_view(API.peopleTypeFans + '?uid=' + PEOPLE_USER_ID, $('#bp_user_fans_more'), $('#contents_user_fans'));	// 粉丝
		
	AWS.load_list_view(API.peopleTypeTopic + '?uid=' + PEOPLE_USER_ID, $('#bp_user_topics_more'), $('#contents_user_topics'));	// 话题

	AWS.load_list_view(API.integralLog + '?', $('#bp_user_integral'), $('#contents_user_integral'));	// 积分
	



	if (window.location.hash)
	{
		if (document.getElementById(window.location.hash.replace('#', '')))
		{
			document.getElementById(window.location.hash.replace('#', '')).click();
		}
	}
	// 关注tab设置 点击更多与tab li 显示相同的效果
	$('.aw-tabs li').click(function() {
		$(this).addClass('active').siblings().removeClass('active');
		// 关注tab内容转换
		$('#focus .aw-user-center-follow-mod').eq($(this).index()).show().siblings().hide();
	});
});