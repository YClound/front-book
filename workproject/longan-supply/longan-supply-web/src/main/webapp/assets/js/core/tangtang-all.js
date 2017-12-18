$(document).ready(function() {
	// $('ul.main-menu li:not(.nav-header)').hover(function() {
	// 	$(this).animate({
	// 		'margin-left' : '+=5'
	// 	}, 300);
	// }, function() {
	// 	$(this).animate({
	// 		'margin-left' : '-=5'
	// 	}, 300);
	// });
});

$(function() {
	$('.doubledate').kuiDate({
		className : 'doubledate',
		isDisabled : "1"
	});
});