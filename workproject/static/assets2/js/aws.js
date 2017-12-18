var AWS =
{
	//全局loading
	loading: function (type)
	{
		if (!$('#aw-loading').length)
	    {
	        $('#aw-ajax-box').append(AW_TEMPLATE.loadingBox);
	    }

		if (type == 'show')
		{
			if ($('#aw-loading').css('display') == 'block')
		    {
			    return false;
		    }

			$('#aw-loading').fadeIn();

			AWS.G.loading_timer = setInterval(function ()
			{
				AWS.G.loading_bg_count -= 1;

				$('#aw-loading-box').css('background-position', '0px ' + AWS.G.loading_bg_count * 40 + 'px');

				if (AWS.G.loading_bg_count == 1)
				{
					AWS.G.loading_bg_count = 12;
				}
			}, 100);
		}
		else
		{
			$('#aw-loading').fadeOut();

			clearInterval(AWS.G.loading_timer);
		}
	},

	loading_mini: function (selector, type)
	{
		if (!selector.find('#aw-loading-mini-box').length)
		{
			selector.append(AW_TEMPLATE.loadingMiniBox);
		}

		if (type == 'show')
		{
			selector.find('#aw-loading-mini-box').fadeIn();

			AWS.G.loading_timer = setInterval(function ()
			{
				AWS.G.loading_mini_bg_count -= 1;

				$('#aw-loading-mini-box').css('background-position', '0px ' + AWS.G.loading_mini_bg_count * 16 + 'px');

				if (AWS.G.loading_mini_bg_count == 1)
				{
					AWS.G.loading_mini_bg_count = 9;
				}
			}, 100);
		}
		else
		{
			selector.find('#aw-loading-mini-box').fadeOut();

			clearInterval(AWS.G.loading_timer);
		}
	},

	ajax_request: function(url, params)
	{
		AWS.loading('show');
		console.log("params:" + params);
	    if (params)
	    {
	        $.post(url, params + '&_post_type=ajax', function (result)
	        {
	        	_callback(result);
	        }, 'json').error(function (error)
	        {
	        	_error(error);
	        });
	    }
	    else
	    {
	        $.get(url, function (result)
	        {
	        	_callback(result);
	        }, 'json').error(function (error)
	        {
	        	_error(error);
	        });
	    }

	    function _callback (result)
	    {
	    	AWS.loading('hide');

        	if (!result)
        	{
	        	return false;
        	}

            if (result.err)
            {
                AWS.alert(result.err);
            }
            else if (result.rsm && result.rsm.url)
            {
                window.location = decodeURIComponent(result.rsm.url);
            }
            else if (result.errno == 1)
            {
                window.location.reload();
            }
	    }

	    function _error (error)
	    {
	    	AWS.loading('hide');

            if ($.trim(error.responseText) != '')
            {
                alert(_t('发生错误, 返回的信息:') + ' ' + error.responseText);
            }
	    }

	    return false;
	},

	// 处理表单数据(表单，类型)

	ajax_post: function(formEl, processer, type) // 表单对象，用 jQuery 获取，回调函数名
	{
	    if (typeof (processer) != 'function')
	    {
	        var processer = AWS.ajax_processer;

	        AWS.loading('show');
	    }

	    if (!type)
	    {
	    	var type = 'default';
	    }

	    var custom_data = {
	        _post_type: 'ajax'
	    };

	    formEl.ajaxSubmit(
	    {
	        dataType: 'json',
	        data: custom_data,
	        success: function (result)
	        {
	        	console.log("result:" + result);
	        	processer(type, result);
	        },
	        // 表单提交失败时，弹出服务器返回的信息responseText字符串
	        error: function (error)
	        {		
	            if ($.trim(error.responseText) != '')
	            {
	            	AWS.loading('hide');

	                alert(_t('发生错误, 返回的信息:') + ' ' + error.responseText);
	            }
	        }
	    });
	},

	// ajax提交callback
	// 处理表单提交成功后数据
	ajax_processer: function (type, result)
	{
		// errno 是记录系统的最后一次错误代码
		console.log("result.errno:" + result.errno);
		if (type == 'default')
		{
			AWS.loading('hide');
		}
		if (typeof (result.errno) == 'undefined')
		{
			AWS.alert(result);
		}
		else if (result.errno != 1)
		{
			switch (type)
			{
				case 'default':
				case 'comments_form':
					AWS.alert(result.err);
					$('.aw-comment-box-btn .btn-success').removeClass('disabled');
				break;

				case 'ajax_post_alert':
				case 'ajax_post_modal':
				case 'error_message':
					if (!$('.error_message').length)
			    	{
				    	alert(result.err);
			    	}
			    	else if ($('.error_message em').length)
			    	{
				    	$('.error_message em').html(result.err);
			    	}
			    	else
			    	{
				    	 $('.error_message').html(result.err);
			    	}

			    	if ($('.error_message').css('display') != 'none')
			    	{
				    	AWS.shake($('.error_message'));
			    	}
			    	else
			    	{
				    	$('.error_message').fadeIn();
			    	}

			    	if ($('#captcha').length)
			    	{
			    		$('#captcha').click();
			    	}
				break;
			}
		}
		else
		{
			if (type == 'comments_form')
			{
				// 重新加载评论列表
				AWS.reload_comments_list(result.rsm.item_id, result.rsm.item_id, result.rsm.type_name);
				// 将输入框中的内容初始化为空
      	$('#aw-comment-box-' + result.rsm.type_name + '-' + result.rsm.item_id + ' form textarea').val('');
      	$('.aw-comment-box-btn .btn-success').removeClass('disabled');
			}

			if (result.rsm && result.rsm.url)
        {
        	// 转到json提供的地址
          window.location = decodeURIComponent(result.rsm.url);
        }
      else
      { //当json文件中不含有地址时重新加载本页面
      	switch (type)
      	{
      		case 'default':
					case 'ajax_post_alert':
					case 'error_message':
						window.location.reload();
					break;

					case 'ajax_post_modal':
						$('#aw-ajax-box div.modal').modal('hide');
					break;
      	}
      }
		}
	},

	// 加载更多
	load_list_view: function(url, selector, container, start_page, callback)
	{
		if (!selector.attr('id'))
	    {
	        return false;
	    }
	    //没有设置start_page将start_page设为0
	    if (!start_page)
	    {
	        start_page = 0
	    }

	    // 把页数绑定在元素上面 data-page=start_page 
	  if (selector.attr('data-page') == undefined)
		{
			selector.attr('data-page', start_page);
		}
		else
		{
			//点击更多按钮时页数的更新
			selector.attr('data-page', parseInt(selector.attr('data-page')) + 1);
		}
			// 更多按钮绑定点击事件
	    selector.bind('click', function ()
	    {
	    	var _this = this;

	    	$(this).addClass('loading');
	    	dm.doAjax("get",url, {'page' : $(_this).attr('data-page')}, function (result)
	    	{
	    		$(_this).removeClass('loading');

	    		if ($.trim(result) != '')
	    		{
	    			console.log($(_this).attr('auto-load'));
	    			// 如果自动加载为真,并且开始页面与data-page相等时 替换内容
	    			if ($(_this).attr('data-page') == start_page && $(_this).attr('auto-load') != 'false')
              {
                container.html(result);
              }
              else
              {
                container.append(result);
                // 追加内容
              }

              // 页数增加1
              $(_this).attr('data-page', parseInt($(_this).attr('data-page')) + 1);
	    			}
	    		else
	    		{
	    			//没有内容
	    			if ($(_this).attr('data-page') == start_page && $(_this).attr('auto-load') != 'false')
              {
                  container.html('<p style="padding: 15px 0" align="center">' + _t('没有内容') + '</p>');
              }

              $(_this).addClass('disabled').unbind('click').bind('click', function () { return false; });

              $(_this).find('span').html(_t('没有更多了'));
	    		}
	    		// 回调函数不等空执行回调函数
          if (callback != null)
          {
              callback();
          }
	    	},"html");

	    	return false;  //阻止默认函数
	    });

	    // 自动加载 直接执行点击事件
	    if (selector.attr('auto-load') != 'false')
	    {
	        selector.click();
	    }
	},

	// 重新加载评论列表
	reload_comments_list: function(item_id, element_id, type_name)
	{
	    $('#aw-comment-box-' + type_name + '-' + element_id + ' .aw-comment-list').html('<p align="center" class="aw-padding10"><i class="aw-loading"></i></p>');

	    switch(type_name){
	    	case "question" :
	    		dm.doAjax("get", API.reloadGetQusetion , {"question_id" : item_id }, function(data){
	    			$('#aw-comment-box-' + type_name + '-' + element_id + ' .aw-comment-list').html(data);
	    		},"html");
	    		break;
	    	case "answer" :
	    		dm.doAjax("get", API.reloadGetAnswer, {"question_id" : item_id }, function(data){
	    			$('#aw-comment-box-' + type_name + '-' + element_id + ' .aw-comment-list').html(data);
	    		},"html");
	    		break;
	    }

	    // $.get(G_BASE_URL + '/question/ajax/get_' + type_name + '_comments/' + type_name + '_id-' + item_id, function (data)
	    // {
	    //     $('#aw-comment-box-' + type_name + '-' + element_id + ' .aw-comment-list').html(data);
	    // });
	},

	// 警告弹窗
	alert: function (text)
	{
	    if ($('.alert-box').length)
	    {
	        $('.alert-box').remove();
	    }

	    $('#aw-ajax-box').append(Hogan.compile(AW_TEMPLATE.alertBox).render(
	    {
	        message: text
	    }));

	    $(".alert-box").modal('show');
	},

	/**
	 *	公共弹窗
	 *	publish     : 发起
	 *	redirect    : 问题重定向
	 *	imageBox    : 插入图片
	 *	videoBox    : 插入视频
	 *  linkbox     : 插入链接
	 *	commentEdit : 评论编辑
	 *  favorite    : 评论添加收藏
	 *	inbox       : 私信
	 *  report      : 举报问题
	 */


	dialog: function (type, data, callback)
	{
	    switch (type)
	    {
		    case 'alertImg':
		    	var template = Hogan.compile(AW_TEMPLATE.alertImg).render(
		    	{
		    		'hide': data.hide,
		    		'url': data.url,
		    		'message': data.message
		    	});
		    break;

		    case 'publish':
		        var template = Hogan.compile(AW_TEMPLATE.publishBox).render(
		        {
		            'category_id': data.category_id,
		            'ask_user_id': data.ask_user_id
		        });
		    break;

		    case 'redirect':
		        var template = Hogan.compile(AW_TEMPLATE.questionRedirect).render(
		        {
		            'data_id': data
		        });
		    break;

		    case 'imageBox':
		        var template = Hogan.compile(AW_TEMPLATE.imagevideoBox).render(
		        {
		            'title': _t('插入图片'),
		            'url': 'imgsUrl',
		            'tips': 'imgsAlt',
		            'type': "'img'",
		            'upload' : ''
		        });
		    break;

		    case 'videoBox':
		        var template = Hogan.compile(AW_TEMPLATE.imagevideoBox).render(
		        {
		            'title': _t('插入视频'),
		            'url': 'videoUrl',
		            'tips': 'videoTitle',
		            'type_tips' : _t('我们目前支持: 优酷、酷六、土豆、56、新浪播客、乐视、Youtube 与 SWF 文件'),
		            'type': "'video'",
		            'upload' : 'hide'
		        });
		    break;

		    case 'linkbox':
		    	var template = Hogan.compile(AW_TEMPLATE.linkBox).render(
		        {
		            'title': _t('插入链接'),
		            'text' : 'linkText',
		            'url'  : 'linkUrl',
		            'type' : "'link'"
		        });
		    break;

		    case 'commentEdit':
		        var template = Hogan.compile(AW_TEMPLATE.editCommentBox).render(
		        {
		            'answer_id': data.answer_id, 
		            'attach_access_key': data.attach_access_key
		        });
		    break;

		    case 'favorite':
		        var template = Hogan.compile(AW_TEMPLATE.favoriteBox).render(
		        {
			         'item_id': data.item_id, 
			         'item_type': data.item_type
		        });
		    break;

		    case 'inbox':
		        var template = Hogan.compile(AW_TEMPLATE.inbox).render(
		        {
		            'recipient': data
		        });
		    break;
        // 举报
		    case 'report':
		        var template = Hogan.compile(AW_TEMPLATE.reportBox).render(
		        {
		            'item_type': data.item_type,
		            'item_id': data.item_id
		        });
		    break;

		    case 'topicEditHistory':
		        var template = AW_TEMPLATE.ajaxData.replace('{{title}}', _t('编辑记录')).replace('{{data}}', data);
			break;

			case 'ajaxData':
				var template = AW_TEMPLATE.ajaxData.replace('{{title}}', data.title).replace('{{data}}', '<div id="aw_dialog_ajax_data"></div>');
			break;

			case 'imagePreview':
				var template = AW_TEMPLATE.ajaxData.replace('{{title}}', data.title).replace('{{data}}', '<p align="center"><img src="' + data.image + '" alt="" style="max-width:520px" /></p>');
			break;

			case 'confirm':
				var template = Hogan.compile(AW_TEMPLATE.confirmBox).render(
				{
					'message': data.message
				});
			break;

	    }

	    if (template)
	    {
	        if ($('.alert-box').length)
	        {
	            $('.alert-box').remove();
	        }

	        $('#aw-ajax-box').html(template).show();

	        switch (type)
	        {
	        	case 'redirect' :
	        		AWS.Dropdown.bind_dropdown_list($('.aw-question-redirect-box #question-input'), 'redirect');
	        	break;

	        	case 'inbox' :
	        		AWS.Dropdown.bind_dropdown_list($('.aw-inbox #invite-input'), 'inbox');
   					//私信用户下拉点击事件
	        		$(document).on('click','.aw-inbox .aw-dropdown-list li a',function() {
    					$('.alert-box #quick_publish input.form-control').val($(this).text());
    					$(this).parents('.aw-dropdown').hide();
    				});
	        	break;

		        case 'publish':
		        	AWS.Dropdown.bind_dropdown_list($('.aw-publish-box #quick_publish_question_content'), 'publish');
		        	AWS.Dropdown.bind_dropdown_list($('.aw-publish-box #aw_edit_topic_title'), 'topic');
		        	if (parseInt(data.category_enable) == 1)
		        	{
			        	$.get(G_BASE_URL + '/publish/ajax/fetch_question_category/', function (result)
			            {
		                AWS.Dropdown.set_dropdown_list('.aw-publish-box .dropdown', eval(result), data.category_id);

		                $('.aw-publish-title .dropdown li a').click(function ()
		                {
	                    $('.aw-publish-box #quick_publish_category_id').val($(this).attr('data-value'));
	                    $('.aw-publish-box #aw-topic-tags-select').html($(this).text());
		                });
			            });
		        	}
		        	else
		        	{
		        		$('.aw-publish-box .aw-publish-title').hide();
		        	}

		        	if (data.ask_user_id != '' && data.ask_user_id != undefined)
							{
								$('.aw-publish-box .modal-title').html('向 ' + data.ask_user_name + ' 提问');
							}

	            if ($('#aw-search-query').val() && $('#aw-search-query').val() != $('#aw-search-query').attr('placeholder'))
	            {
		            $('#quick_publish_question_content').val($('#aw-search-query').val());
	            }

	            AWS.Init.init_topic_edit_box('#quick_publish .aw-edit-topic');

	            $('#quick_publish .aw-edit-topic').click();

	            $('#quick_publish .close-edit').hide();

	            if (data.topic_title)
	            {
	                $('#quick_publish .aw-edit-topic').parents('.aw-topic-bar').prepend('<span class="topic-tag"><a class="text">' + data.topic_title + '</a><a class="close" onclick="$(this).parents(\'.topic-tag\').detach();"><i class="icon icon-delete"></i></a><input type="hidden" value="' + data.topic_title + '" name="topics[]" /></span>')
	            }

	            if (typeof(G_QUICK_PUBLISH_HUMAN_VALID) != 'undefined')
	            {
		            $('#quick_publish_captcha').show();
		            $('#captcha').click();
	            }
	            break;
						break;
						// 收藏回复
		        case 'favorite':
		            dm.doAjax("get", API.getFavoriteTags, function (result)
		            {
		                if (result.length > 0)
		                {
		                    $('#add_favorite_my_tags').show();
		                }
		                $.each(result, function (i, a)
		                {
		                    $('#add_favorite_my_tags').append('<a href="javascript:;" onclick="$(\'#add_favorite_tags\').val($(\'#add_favorite_tags\').val() + \'' + a['title'] + ',\');" class="topic-tag"><span>' + a['title'] + '</span></a> ');
		                });
		            });
		          break;
						break;
						// 举报
		        case 'report':
	            $('.aw-report-box select option').click(function ()
	            {
	                $('.aw-report-box textarea').text($(this).attr('value'));
	            });
	            break;
						break;

		        case 'commentEdit':
		            $.get(G_BASE_URL + '/question/ajax/fetch_answer_data/' + data.answer_id, function (result)
		            {
		                $('#editor_reply').html(result.answer_content.replace('&amp;', '&'));
		            }, 'json');

								var fileupload = new FileUpload('file', '.aw-edit-comment-box .aw-upload-box .btn', '.aw-edit-comment-box .aw-upload-box .upload-container', G_BASE_URL + '/publish/ajax/attach_upload/id-answer__attach_access_key-' + ATTACH_ACCESS_KEY, {'insertTextarea': '.aw-edit-comment-box #editor_reply'});

		            if ($(".aw-edit-comment-box .upload-list").length) {
			            $.post(G_BASE_URL + '/publish/ajax/answer_attach_edit_list/', 'answer_id=' + data.answer_id, function (data) {
		                if (data['err']) {
		                  return false;
		                } else {
	                    $.each(data['rsm']['attachs'], function (i, v) {
	                        fileupload.setFileList(v);
	                    });
		                }
			            }, 'json');
			        	}
		            break;
		        	break;

		        case 'ajaxData':
			    	$.get(data.url, function (result) {
						$('#aw_dialog_ajax_data').html(result);
					});
		    	break;

		    	case 'confirm':
		    		$('.aw-confirm-box .yes').click(function()
		    		{
		    			if (callback)
		    			{
		    				callback();
		    			}

		    			$(".alert-box").modal('hide');

		    			return false;
		    		});
		    	break;
	        }

	        $(".alert-box").modal('show');
	    }
	},

	// 兼容placeholder
	check_placeholder: function(selector)
	{
		$.each(selector, function()
		{
			if (typeof ($(this).attr("placeholder")) != "undefined")
        {
          $(this).attr('data-placeholder', 'true');

          if ($(this).val() == '')
          {
            $(this).addClass('aw-placeholder').val($(this).attr("placeholder"));
          }

          $(this).focus(function () {
              if ($(this).val() == $(this).attr('placeholder'))
              {
                  $(this).removeClass('aw-placeholder').val('');
              }
          });

          $(this).blur(function () {
              if ($(this).val() == '')
              {
                  $(this).addClass('aw-placeholder').val($(this).attr('placeholder'));
              }
          });
        }
		});
	},

	// 回复背景高亮
	hightlight: function(selector, class_name)
	{
	    if (selector.hasClass(class_name))
	    {
	        return true;
	    }

	    var hightlight_timer_front = setInterval(function ()
	    {
	        selector.addClass(class_name);
	    }, 500);

	    var hightlight_timer_background = setInterval(function ()
	    {
	        selector.removeClass(class_name);
	    }, 600);

	    setTimeout(function ()
	    {
	        clearInterval(hightlight_timer_front);
	        clearInterval(hightlight_timer_background);

	        selector.addClass(class_name);
	    }, 1200);

	    setTimeout(function ()
	    {
	        selector.removeClass(class_name);
	    }, 6000);
	},

	nl2br: function(str)
	{
	    return str.replace(new RegExp("\r\n|\n\r|\r|\n", "g"), "<br />");
	},

	content_switcher: function(hide_el, show_el)
	{
	    hide_el.hide();
	    show_el.fadeIn();
	},

	htmlspecialchars: function(text)
	{
	    return text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#039;');
	},

	/*
	 * 用户头像提示box效果
	 *  @params
	 *  type : user/topic
	 *	nTop    : 焦点到浏览器上边距
	 *	nRight  : 焦点到浏览器右边距
	 *	nBottom : 焦点到浏览器下边距
	 *	left    : 焦点距离文档左偏移量
	 *	top     : 焦点距离文档上偏移量
	 **
	 */
	show_card_box: function(selector, type, time) //selector -> .aw-user-name/.topic-tag
	{
		if (!time)
		{
			var time = 300;
		}

	    $(document).on('mouseover', selector, function ()
	    {
	        clearTimeout(AWS.G.card_box_hide_timer);
	        var _this = $(this);
	        AWS.G.card_box_show_timer = setTimeout(function ()
	        {
	            //判断用户id or 话题id 是否存在
	            if (_this.attr('data-id'))
	            {
	                 switch (type)
	                {
	                    case 'user' :
	                        //检查是否有缓存
	                        if (AWS.G.cashUserData.length == 0)
	                        {
	                            _getdata('user', API.user);
	                        }
	                        else
	                        {
	                            var flag = 0;
	                            //遍历缓存中是否含有此id的数据
	                            _checkcash('user');
	                            if (flag == 0)
	                            {
	                                _getdata('user', API.user);
	                            }
	                        }
	                    break;

	                    case 'topic' :
	                        //检查是否有缓存
	                        if (AWS.G.cashTopicData.length == 0)
	                        {
	                            _getdata('topic', API.topic);
	                        }
	                        else
	                        {
	                            var flag = 0;
	                            //遍历缓存中是否含有此id的数据
	                            _checkcash('topic');
	                            if (flag == 0)
	                            {
	                                _getdata('topic', API.topic);
	                            }
	                        }
	                    break;
	                }
	            }

	            //获取数据
	            function _getdata(type, url)
	            {
	                if (type == 'user')
	                {
	                    dm.doAjax("get", url, {"id" : _this.attr("data-id")},function(result)
	                    {
	                        var focus = result.focus, verified = result.verified, focusTxt;

	                        if (focus == 1)
	                        {
	                            focus = 'active';
	                            focusTxt = '取消关注';
	                        }
	                        else
	                        {
	                            focus = '';
	                            focusTxt = '关注';
	                        }

	                        if(result.verified == 'enterprise')
	                        {
	                            verified_enterprise = 'icon-v i-ve';
	                            verified_title = '企业认证';
	                        }
	                        else if(result.verified == 'personal')
	                        {
	                            verified_enterprise = 'icon-v';
	                            verified_title = '个人认证';
	                        }
	                        else
	                        {
	                            verified_enterprise = verified_title = '';
	                        }

	                        //动态插入盒子
	                        $('#aw-ajax-box').html(Hogan.compile(AW_TEMPLATE.userCard).render(
	                        {
	                            'verified_enterprise' : verified_enterprise,
	                            'verified_title' : verified_title,
	                            'uid': result.uid,
	                            'avatar_file': result.avatar_file,
	                            'user_name': result.user_name,
	                            'reputation': result.reputation,
	                            'agree_count': result.agree_count,
	                            'signature': result.signature,
	                            'url' : result.url,
	                            'category_enable' : result.category_enable,
	                            'focus': focus,
	                            'focusTxt': focusTxt,
	                            'ask_name': "'" + result.user_name + "'",
	                            'fansCount': result.fans_count
	                        }));

	                        //判断是否为游客or自己
	                        if (G_USER_ID == '' || G_USER_ID == result.uid || result.uid < 0)
	                        {
	                            $('#aw-card-tips .mod-footer').hide();
	                        }
	                        _init();
	                        //缓存
	                        AWS.G.cashUserData.push($('#aw-ajax-box').html());
	                    }, 'json');
	                }
	                if (type == 'topic')
	                {
	                    dm.doAjax("get", url, {"id" : _this.attr('data-id')}, function(result)
	                    {
	                        var focus = result.focus,
	                            focusTxt;
	                            if (focus == false)
	                            {
	                            	focus = '';
	                                focusTxt = _t('关注');
	                            }
	                            else
	                            {
	                                focus = 'active';
	                                focusTxt = _t('取消关注');
	                            }
	                            //动态插入盒子
	                            $('#aw-ajax-box').html(Hogan.compile(AW_TEMPLATE.topicCard).render(
	                            {
	                                'topic_id': result.topic_id,
	                                'topic_pic': result.topic_pic,
	                                'topic_title': result.topic_title,
	                                'topic_description': result.topic_description,
	                                'discuss_count': result.discuss_count,
	                                'focus_count': result.focus_count,
	                                'focus': focus,
	                                'focusTxt': focusTxt,
	                                'url' : result.url,
	                                'fansCount': result.fans_count
	                            }));
	                            //判断是否为游客
	                            if (G_USER_ID == '')
	                            {
	                                $('#aw-card-tips .mod-footer .follow').hide();
	                            }
	                            _init();
	                            //缓存
	                            AWS.G.cashTopicData.push($('#aw-ajax-box').html());
	                    }, 'json');
	                }
	            }

	            //检测缓存
	            function _checkcash(type)
	            {
	                if (type == 'user')
	                {
	                    $.each(AWS.G.cashUserData, function (i, a)
	                    {
	                        if (a.match('data-id="' + _this.attr('data-id') + '"'))
	                        {
	                            $('#aw-ajax-box').html(a);
	                            $('#aw-card-tips').removeAttr('style');
	                            _init();
	                            flag = 1;
	                        }
	                    });
	                }
	                if (type == 'topic')
	                {

	                    $.each(AWS.G.cashTopicData, function (i, a)
	                    {
	                        if (a.match('data-id="' + _this.attr('data-id') + '"'))
	                        {
	                            $('#aw-ajax-box').html(a);
	                            $('#aw-card-tips').removeAttr('style');
	                            _init();
	                            flag = 1;
	                        }
	                    });
	                }
	            }

	            //初始化
	            function _init()
	            {
	            	var left = _this.offset().left,
	            		top = _this.offset().top + _this.height() + 5,
	            		nTop = _this.offset().top - $(window).scrollTop();

	            	//判断下边距离不足情况
	            	if (nTop + $('#aw-card-tips').innerHeight() > $(window).height())
	            	{
	            		top = _this.offset().top - ($('#aw-card-tips').innerHeight()) - 10;
	            	}

	            	//判断右边距离不足情况
	            	if (left + $('#aw-card-tips').innerWidth() > $(window).width())
	            	{
	            		left = _this.offset().left - $('#aw-card-tips').innerWidth() + _this.innerWidth();
	            	}

	                $('#aw-card-tips').css(
	                {
	                    left: left,
	                    top: top
	                }).fadeIn();
	            }
	        }, time);
	    });

	    $(document).on('mouseout', selector, function ()
	    {
	        clearTimeout(AWS.G.card_box_show_timer);
	        AWS.G.card_box_hide_timer = setTimeout(function ()
	        {
	            $('#aw-card-tips').fadeOut();
	        }, 600);
	    });
	},

	// @人功能
	at_user_lists: function(selector) {
	    $(selector).keyup(function (e) {
	        var _this = $(this),
	            flag = _getCursorPosition($(this)[0]).start; //获取光标当前所在的位置
	        if ($(this).val().charAt(flag - 1) == '@')
	        {
	        	_init(); //初始化插入定位符
	        	$('#aw-ajax-box .content_cursor').html($(this).val().substring(0, flag));
	        } else
	        {
	        	var lis = $('.aw-invite-dropdown li');
	            switch (e.which)
	            {
	                case 38:
	                	var _index;
	                	if (!lis.hasClass('active'))
		            	{
		            		lis.eq(lis.length - 1).addClass('active');
		            	}
	                	else
	                	{
	                		$.each(lis, function (i, e)
		            		{
		            			if ($(this).hasClass('active'))
								{
									$(this).removeClass('active');
									if ($(this).index() == 0)
									{
										_index = lis.length - 1;
									}
									else
									{
										_index = $(this).index() - 1;
									}
								}
		            		});
		            		lis.eq(_index).addClass('active');
	                	}
	                    break;
	                case 40:
	                    var _index;
		            	if (!lis.hasClass('active'))
		            	{
		            		lis.eq(0).addClass('active');
		            	}
		            	else
		            	{
		            		$.each(lis, function (i, e)
		            		{
		            			if ($(this).hasClass('active'))
								{
			            			$(this).removeClass('active');
			            			if ($(this).index() == lis.length - 1)
			            			{
			            				_index = 0;
			            			}
			            			else
			            			{
			            				_index = $(this).index() + 1;
			            			}
			            		}
		            		});
		            		lis.eq(_index).addClass('active');
		            	}
	                    break;
	                case 13:
	                	$.each($('.aw-invite-dropdown li'), function (i, e)
	                	{
	                		if ($(this).hasClass('active'))
	                		{
	                			$(this).click();
	                		}
	                	});
	                    break;
	                default:
	                    if ($('.aw-invite-dropdown')[0])
	                    {
	                        var ti = 0;
	                        for (var i = flag; i > 0; i--)
	                        {
	                            if ($(this).val().charAt(i) == "@")
	                            {
	                                ti = i;
	                                break;
	                            }
	                        }
	                        $.get(G_BASE_URL + '/search/ajax/search/?type=users&q=' + encodeURIComponent($(this).val().substring(flag, ti).replace('@', '')) + '&limit=10', function (result)
	                        {
	                            if ($('.aw-invite-dropdown')[0])
	                            {
	                                if (result.length != 0)
	                                {
	                                    $('.aw-invite-dropdown').html('');
	                                    $.each(result, function (i, a)
	                                    {
	                                        $('.aw-invite-dropdown').append('<li><img src="' + a.detail.avatar_file + '"/><a>' + a.name + '</a></li>')
	                                    });
	                                    _display();
	                                    $('.aw-invite-dropdown li').click(function ()
	                                    {
	                                        _this.val(_this.val().substring(0, ti) + '@' + $(this).find('a').html() + " ").focus();
	                                        $('.aw-invite-dropdown').detach();
	                                    });
	                                }
	                                else
	                                {
	                                    $('.aw-invite-dropdown').hide();
	                                }
	                            }
	                            if (_this.val().length == 0)
	                            {
	                                $('.aw-invite-dropdown').hide();
	                            }
	                        }, 'json');
	                    }
	            }
	        }
	    });

	    $(selector).keydown(function (e) {
	        var key = e.which;
	        if ($('.aw-invite-dropdown').is(':visible')) {
	            if (key == 38 || key == 40 || key == 13) {
	                return false;
	            }
	        }
	    });

	    //初始化插入定位符
	    function _init() {
	        if (!$('.content_cursor')[0]) {
	            $('#aw-ajax-box').append('<span class="content_cursor"></span>');
	        }
	        $('#aw-ajax-box').find('.content_cursor').css({
	            'left': parseInt($(selector).offset().left + parseInt($(selector).css('padding-left')) + 2),
	            'top': parseInt($(selector).offset().top + parseInt($(selector).css('padding-left')))
	        });
	        if (!$('.aw-invite-dropdown')[0])
            {
                $('#aw-ajax-box').append('<ul class="aw-invite-dropdown"></ul>');
            }
	    };

	    //初始化列表和三角型
	    function _display() {
	        $('.aw-invite-dropdown').css({
	            'left': $('.content_cursor').offset().left + $('.content_cursor').innerWidth(),
	            'top': $('.content_cursor').offset().top + 24
	        }).show();
	    };

	    //获取当前textarea光标位置 .aw-comment-text
		function _getCursorPosition(textarea)
		{
		    var rangeData = {
		        text: "",
		        start: 0,
		        end: 0
		    };

		    textarea.focus();

		    if (textarea.setSelectionRange) { // W3C 滚动选择
		        rangeData.start = textarea.selectionStart;
		        rangeData.end = textarea.selectionEnd;
		        rangeData.text = (rangeData.start != rangeData.end) ? textarea.value.substring(rangeData.start, rangeData.end) : "";
		    } else if (document.selection) { // IE 获取选中
		        var i,
		            oS = document.selection.createRange(), //获取选中的文本
		            // Don't: oR = textarea.createTextRange()
		            oR = document.body.createTextRange();
		        oR.moveToElementText(textarea); //指定要移动的对象

		        rangeData.text = oS.text;
		        rangeData.bookmark = oS.getBookmark(); //快速记录指针定位

		        // object.moveStart(sUnit [, iCount])
		        // Return Value: Integer that returns the number of units moved.
		        for (i = 0; oR.compareEndPoints('StartToStart', oS) < 0 && oS.moveStart("character", -1) !== 0; i++) {
		            // Why? You can alert(textarea.value.length)
		            if (textarea.value.charAt(i) == '\n') {
		                i++;
		            }
		        }
		        rangeData.start = i;
		        rangeData.end = rangeData.text.length + rangeData.start;
		    }

		    return rangeData;
		};
	},

	// 错误提示效果
	shake: function(selector)
	{
		var length = 6;
		selector.css('position', 'relative');
	    for (var i = 1; i <= length; i++)
	    {
	    	if (i % 2 == 0)
	    	{
	        	if (i == length)
	        	{
	        		selector.animate({ 'left': 0 }, 50);
	        	}
	        	else
	        	{
	        		selector.animate({ 'left': 10 }, 50);
	        	}
	    	}
	    	else
	    	{
	    		selector.animate({ 'left': -10 }, 50);
	    	}
	    }
	}
}

// 全局变量
AWS.G =
{
	cashUserData: [],
	cashTopicData: [],
	card_box_hide_timer: '',
	card_box_show_timer: '',
	dropdown_list_xhr: '',
	loading_timer: '',
	loading_bg_count: 12,
	loading_mini_bg_count: 9,
	notification_timer: ''
}
// 用户关注
AWS.User =
{
	// 关注用户、话题、问题($(this),user/question/topic,id)
	follow: function(selector, type, data_id)
	{	
		// 判断焦点是否为空
		if (selector.html())
    {
      if (selector.hasClass('active'))
      {
      	selector.find('span').html(_t('关注'));

      	selector.find('b').html(parseInt(selector.find('b').html()) - 1);
      }
      else
      {
      	selector.find('span').html(_t('取消关注'));

      	selector.find('b').html(parseInt(selector.find('b').html()) + 1);
      }
    }
    else
    {
        if (selector.hasClass('active'))
        {
          selector.attr('data-original-title', _t('关注'));
        }
        else
        {
          selector.attr('data-original-title', _t('取消关注'));
        }
    }

	  selector.addClass('disabled');

    switch (type)
    {
    	case 'question':
    		var url = API.focusQuestion ; //获取是否关注问题
    		break;

    	case 'topic':
    		var url = API.focusTopic;
    		break;

    	case 'user':
    		var url = API.focusPeople;
    	break;
    }

    dm.doAjax("get", url, {"id" : data_id}, function (result)
    {
    	if (result.errno == 1)
      { 
    		//关注 无错误时 判断是关注还是取消关注
        if (result.rsm.type == 'add')
        {
          selector.addClass('active');
        }
        else
        {
        	//取消关注
          selector.removeClass('active');
        }
      }
      else
      {
        if (result.err)
        {
          AWS.alert(result.err);
        }
        // 出现错误跳转的页面
        if (result.rsm.url)
        {
          window.location = decodeURIComponent(result.rsm.url);
        }
      }

      selector.removeClass('disabled');

    });
	},

	share_out: function(webid, title, url)
	{
		var url = url || window.location.href;

		if (title)
		{
			var title = title + ' - ' + G_SITE_NAME;
		}
		else
		{
			var title = $('title').text();
		}
		
		shareURL = 'http://www.jiathis.com/send/?webid=' + webid + '&url=' + url + '&title=' + title + '';

		window.open(shareURL);
	},

	// 删除草稿
	delete_draft: function(item_id, type)
	{
		if (type == 'clean')
		{
			$.get(G_BASE_URL + '/account/ajax/delete_draft/', 'type=' + type, function (result)
		    {
		        if (result.errno != 1)
		        {
		            AWS.alert(result.err);
		        }
		    }, 'json');
		}
		else
		{
			dm.doAjax("get", API.deleteDraft, {'item_id' : item_id , 'type' : type}, function (result)
		    {
		        if (result.errno != 1)
		        {
		            AWS.alert(result.err);
		        }
		    }, 'json');
		}
	},

	// 赞成投票
	agree_vote: function(selector, user_name, answer_id)
	{
		dm.doAjax("post", API.answerVote, {'answer_id' : answer_id , 'value':'1'});

	    // 判断是否投票过
	    // 已投过票
	    if ($(selector).parents('.aw-item').find('.aw-agree-by').text().match(user_name))
	    {
	        $.each($(selector).parents('.aw-item').find('.aw-user-name'), function (i, e)
	        {
            if ($(e).html() == user_name)
            {
              if ($(e).prev())
              {
                $(e).prev().remove();
              }
              else
              {
                $(e).next().remove();
              }

              $(e).remove();
            }
	        });

	        $(selector).removeClass('active');

	        if (parseInt($(selector).parents('.operate').find('.count').html()) != 0)
	        {
	        	$(selector).parents('.operate').find('.count').html(parseInt($(selector).parents('.operate').find('.count').html()) - 1);
	        }

	        if ($(selector).parents('.aw-item').find('.aw-agree-by a').length == 0)
	        {
	            $(selector).parents('.aw-item').find('.aw-agree-by').hide();
	        }
	    }
	    else
	    {
	        // 判断是否第一个投票
	        if ($(selector).parents('.aw-item').find('.aw-agree-by .aw-user-name').length == 0)
	        {
	            $(selector).parents('.aw-item').find('.aw-agree-by').append('<a class="aw-user-name">' + user_name + '</a>');
	        }
	        else
	        {
	            $(selector).parents('.aw-item').find('.aw-agree-by').append('<em>、</em><a class="aw-user-name">' + user_name + '</a>');
	        }

	        $(selector).parents('.operate').find('.count').html(parseInt($(selector).parents('.operate').find('.count').html()) + 1);

	        $(selector).parents('.aw-item').find('.aw-agree-by').show();

	        $(selector).parents('.operate').find('a.active').removeClass('active');

	        $(selector).addClass('active');
	    }
	},

	// 反对投票
	disagree_vote: function(selector, user_name, answer_id)
	{
	    dm.doAjax("post", API.answerVote, {'answer_id' : answer_id , 'value':'-1'}, function (result) {});

	    if ($(selector).hasClass('active'))
	    {
	    	$(selector).removeClass('active');
	    }
	    else
	    {
	    	// 判断是否有赞同过
	    	if ($(selector).parents('.operate').find('.agree').hasClass('active'))
	    	{
	    		// 删除赞同操作
		        $.each($(selector).parents('.aw-item').find('.aw-user-name'), function (i, e)
		        {
		            if ($(e).html() == user_name)
		            {
		                if ($(e).prev())
		                {
		                    $(e).prev().remove();
		                }
		                else
		                {
		                    $(e).next().remove();
		                }

		                $(e).remove();
		            }
		        });

		        // 判断赞同来自内是否有人
		        if ($(selector).parents('.aw-item').find('.aw-agree-by a').length == 0)
		        {
		            $(selector).parents('.aw-item').find('.aw-agree-by').hide();
		        }

		        $(selector).parents('.operate').find('.count').html(parseInt($(selector).parents('.operate').find('.count').html()) - 1);

		        $(selector).parents('.operate').find('.agree').removeClass('active');

		        $(selector).addClass('active');
	    	}
	    	else
	    	{
	    		$(selector).addClass('active');
	    	}
	    }
	},

	// 问题不感兴趣 隐藏该精选问题
	question_uninterested: function(selector, question_id)
	{
	    selector.fadeOut();

	    dm.doAjax("post", API.uninterestedQuestion, {'question_id' : question_id}, function (result)
	    {
	        if (result.errno != '1')
	        {
	            AWS.alert(result.err);
	        }
	    }, 'json');
	},

	// 回复折叠
	answer_force_fold: function(selector, answer_id)
	{
		$.post(G_BASE_URL + '/question/ajax/answer_force_fold/', 'answer_id=' + answer_id, function (result) {
			if (result.errno != 1)
			{
				AWS.alert(result.err);
			}
			else if (result.errno == 1)
			{
				if (result.rsm.action == 'fold')
				{
					selector.html(selector.html().replace(_t('折叠'), _t('撤消折叠')));
				}
				else
				{
					selector.html(selector.html().replace(_t('撤消折叠'), _t('折叠')));
				}
			}
		}, 'json');
	},

	// 删除别人邀请我回复的问题
	question_invite_delete: function(selector, question_invite_id)
	{
	    $.post(G_BASE_URL + '/question/ajax/question_invite_delete/', 'question_invite_id=' + question_invite_id, function (result)
	    {
	        if (result.errno == 1)
	        {
	            selector.fadeOut();
	        }
	        else
	        {
	            AWS.alert(result.rsm.err);
	        }
	    }, 'json');
	},

	// 邀请用户回答问题
	invite_user: function(selector, img)
	{
    dm.doAjax("post",API.saveInvite,
    {
        'question_id': QUESTION_ID,
        'uid': selector.attr('data-id')
    }, function (result)
    {
        if (result.errno != -1)
        {
        		// 显示已邀请
            if (selector.parents('.aw-invite-box').find('.invite-list a').length == 0)
            {
                selector.parents('.aw-invite-box').find('.invite-list').show();
            }
            // 点击邀请添加已邀请图片
            selector.parents('.aw-invite-box').find('.invite-list').append(' <a class="text-color-999 invite-list-user" data-toggle="tooltip" data-placement="bottom" data-original-title="'+ selector.attr('data-value') +'"><img src='+ img +' /></a>');
            selector.addClass('active').attr('onclick','AWS.User.disinvite_user($(this))').text('取消邀请');
            selector.parents('.aw-question-detail').find('.aw-invite-replay .badge').text(parseInt(selector.parents('.aw-question-detail').find('.aw-invite-replay .badge').text()) + 1);
        }
        else if (result.errno == -1)
        {
            AWS.alert(result.err);
        }
    });
	},

	// 取消邀请用户回答问题
	disinvite_user: function(selector)
	{
	    dm.doAjax("get", API.cancleInvite ,
    	{
    			"id":  QUESTION_ID,
    			"recipients_uid" : selector.attr('data-id')
    	}, 
	    function (result)
	    {
	        if (result.errno != -1)
	        {
	            $.each($('.aw-question-detail .invite-list a'), function (i, e)
	            {
	                if ($(this).attr('data-original-title') == selector.parents('.main').find('.aw-user-name').text())
	                {
	                    $(this).detach();
	                }
	            });
	            selector.removeClass('active').attr('onclick','AWS.User.invite_user($(this),$(this).parents(\'li\').find(\'img\').attr(\'src\'))').text('邀请');
	            selector.parents('.aw-question-detail').find('.aw-invite-replay .badge').text(parseInt(selector.parents('.aw-question-detail').find('.aw-invite-replay .badge').text()) - 1);
	            if (selector.parents('.aw-invite-box').find('.invite-list').children().length == 0)
	            {
	                selector.parents('.aw-invite-box').find('.invite-list').hide();
	            }
	        }
	    });
	},

	// 问题感谢
	question_thanks: function(selector, question_id)
	{
    dm.doAjax( "get" , API.questionThank,
    	{"question_id": question_id}, 
    function (result)
    {
        if (result.errno != 1)
        {
            AWS.alert(result.err);
        }
        else if (result.rsm.action == 'add')
        {
            selector.html(selector.html().replace(_t('感谢'), _t('已感谢')));
            selector.removeAttr('onclick');
        }
        else
        {
            selector.html(selector.html().replace(_t('已感谢'), _t('感谢')));
        }
    });
	},

	// 感谢评论回复者
	answer_user_rate: function(selector, type, answer_id)
	{
	   dm.doAjax( "post", API.questionAnswerRate, {
	   	'type=' : type ,
	   	'answer_id=' : answer_id
	   }, function (result)
	    {
	        if (result.errno != 1)
	        {
	            AWS.alert(result.err);
	        }
	        else if (result.errno == 1)
	        {
	            switch (type)
	            {
	            case 'thanks':
	                if (result.rsm.action == 'add')
	                {
	                    selector.html(selector.html().replace(_t('感谢'), _t('已感谢')));
	                    selector.removeAttr('onclick');
	                }
	                else
	                {
	                    selector.html(selector.html().replace(_t('已感谢'), _t('感谢')));
	                }
	                break;

	            case 'uninterested':
	                if (result.rsm.action == 'add')
	                {
	                    selector.html(selector.html().replace(_t('没有帮助'), _t('撤消没有帮助')));
	                }
	                else
	                {
	                    selector.html(selector.html().replace(_t('撤消没有帮助'), _t('没有帮助')));
	                }
	                break;
	            }
	        }
	    });
	},

	// 提交评论
	//提交添加的评论qustion-detail.html、commentBox模板
	save_comment: function(selector)
	{
	    selector.addClass('disabled');

	    AWS.ajax_post(selector.parents('form'), AWS.ajax_processer, 'comments_form');
	},

	// 删除评论
	remove_comment: function(selector, type, comment_id)
	{
		$.get(G_BASE_URL + '/question/ajax/remove_comment/type-' + type + '__comment_id-' + comment_id);

		selector.parents('.aw-comment-box li').fadeOut();
	},

	// 文章赞同
	article_vote: function(selector, article_id, rating)
	{
		AWS.loading('show');

		if (selector.hasClass('active'))
		{
			var rating = 0;
		}

		$.post(G_BASE_URL + '/article/ajax/article_vote/', 'type=article&item_id=' + article_id + '&rating=' + rating, function (result) {

			AWS.loading('hide');

			if (result.errno != 1)
		    {
		        AWS.alert(result.err);
		    }
		    else
		    {
				if (rating == 0)
				{
	                selector.removeClass('active').find('b').html(parseInt(selector.find('b').html()) - 1);
				}
	            else if (rating == -1)
	            {
	                if (selector.parents('.aw-article-vote').find('.agree').hasClass('active'))
	                {
	                    selector.parents('.aw-article-vote').find('b').html(parseInt(selector.parents('.aw-article-vote').find('b').html()) - 1);
	                    selector.parents('.aw-article-vote').find('a').removeClass('active');
	                }
	                
	                selector.addClass('active');
	            }
				else
				{
					selector.parents('.aw-article-vote').find('a').removeClass('active');
					selector.addClass('active').find('b').html(parseInt(selector.find('b').html()) + 1);
				}
		    }
		}, 'json');
	},

	// 文章评论赞同
	article_comment_vote: function(selector, comment_id, rating)
	{
		AWS.loading('show');

		if (selector.hasClass('active'))
		{
			var rating = 0;
		}

		$.post(G_BASE_URL + '/article/ajax/article_vote/', 'type=comment&item_id=' + comment_id + '&rating=' + rating, function (result)
		{
			AWS.loading('hide');

			if (result.errno != 1)
		    {
		        AWS.alert(result.err);
		    }
		    else
		    {
				if (rating == 0)
				{
					selector.html(selector.html().replace(_t('我已赞'), _t('赞'))).removeClass('active');
				}
				else
				{
					selector.html(selector.html().replace(_t('赞'), _t('我已赞'))).addClass('active');
				}
		    }
		}, 'json');
	}
}

AWS.Dropdown =
{
	// 下拉菜单功能绑定
	bind_dropdown_list: function(selector, type)
	{
	    if (type == 'search')
	    {
	        $(selector).focus(function()
	        {
	            $(selector).parent().find('.aw-dropdown').show();
	        });
	    }
	    $(selector).keyup(function(e)
	    {
	        if (type == 'search')
	        {
	            $(selector).parent().find('.search').show().children('a').text($(selector).val());
	        }
	        if ($(selector).val().length >= 1)
	        {
	        	if (e.which != 38 && e.which != 40 && e.which != 188 && e.which != 13)
	        	{
	            	AWS.Dropdown.get_dropdown_list($(this), type, $(selector).val());
	        	}
	        }
	        else
	        {
	           $(selector).parent().find('.aw-dropdown').hide();
	        }

	        if (type == 'topic')
	        {
	        	// 逗号或回车提交
	            if (e.which == 188)
	            {
	                if ($('.aw-edit-topic-box #aw_edit_topic_title').val() != ',')
	                {
	                    $('.aw-edit-topic-box #aw_edit_topic_title').val( $('.aw-edit-topic-box #aw_edit_topic_title').val().substring(0,$('.aw-edit-topic-box #aw_edit_topic_title').val().length-1));
	                    $('.aw-edit-topic-box .aw-dropdown').hide();
	                    $('.aw-edit-topic-box .add').click();
	                }
	                return false;
	            }

	            // 回车提交
	            if (e.which == 13)
	            {
	            	$('.aw-edit-topic-box .aw-dropdown').hide();
	                $('.aw-edit-topic-box .add').click();
	            	return false;
	            }

	            var lis = $(selector).parent().find('.aw-dropdown-list li');

	            //键盘往下
	            if (e.which == 40 && lis.is(':visible'))
	            {
	            	var _index;
	            	if (!lis.hasClass('active'))
	            	{
	            		lis.eq(0).addClass('active');
	            	}
	            	else
	            	{
	            		$.each(lis, function (i, e)
	            		{
	            			if ($(this).hasClass('active'))
							{
		            			$(this).removeClass('active');
		            			if ($(this).index() == lis.length - 1)
		            			{
		            				_index = 0;
		            			}
		            			else
		            			{
		            				_index = $(this).index() + 1;
		            			}
		            		}
	            		});
	            		lis.eq(_index).addClass('active');
	            		$(selector).val(lis.eq(_index).text());
	            	}
	            }

	            //键盘往上
	            if (e.which == 38 && lis.is(':visible'))
	            {
	            	var _index;
	            	if (!lis.hasClass('active'))
	            	{
	            		lis.eq(lis.length - 1).addClass('active');
	            	}
	            	else
	            	{
	            		$.each(lis, function (i, e)
	            		{
	            			if ($(this).hasClass('active'))
							{
								$(this).removeClass('active');
								if ($(this).index() == 0)
								{
									_index = lis.length - 1;
								}
								else
								{
									_index = $(this).index() - 1;
								}
							}
	            		});
	            		lis.eq(_index).addClass('active');
	            		$(selector).val(lis.eq(_index).text());
	            	}

	            }
	        }
	    });

	    $(selector).blur(function()
	    {
	        $(selector).parent().find('.aw-dropdown').delay(500).fadeOut(300);
	    });
	},

	// 插入下拉菜单
	set_dropdown_list: function(selector, data, selected)
	{
	    $(selector).append(Hogan.compile(AW_TEMPLATE.dropdownList).render(
	    {
	        'items': data
	    }));

	    $(selector + ' .aw-dropdown-list li a').click(function ()
	    {
	        $('#aw-topic-tags-select').html($(this).text());
	    });

	    if (selected)
	    {
	        $(selector + " .dropdown-menu li a[data-value='" + selected + "']").click();
	    }
	},

	/* 下拉菜单数据获取 */
	/*
	*    type : search, publish, redirect, invite, inbox, topic_question, topic
	*/
	get_dropdown_list: function(selector, type, data)
	{
	    if (AWS.G.dropdown_list_xhr != '')
	    {
	        AWS.G.dropdown_list_xhr.abort(); // 中止上一次ajax请求
	    }
	    var url;
	    switch (type)
	    {
	        case 'search' :
	            url = G_BASE_URL + '/search/ajax/search/?q=' + encodeURIComponent(data) + '&limit=5';
	        break;

	        case 'publish' :
	            url = G_BASE_URL + '/search/ajax/search/?type=questions&q=' + encodeURIComponent(data) + '&limit=5';
	        break;

	        case 'redirect' :
	            url = G_BASE_URL + '/search/ajax/search/?q=' + encodeURIComponent(data) + '&type=questions&limit=30';
	        break;

	        case 'invite' :
	        case 'inbox' :
	            url = G_BASE_URL + '/search/ajax/search/?type=users&q=' + encodeURIComponent(data) + '&limit=10';
	        break;

	        case 'topic_question' :
	            url = G_BASE_URL + '/search/ajax/search/?type=questions,articles&q=' + encodeURIComponent(data) + '&topic_ids=' + CONTENTS_RELATED_TOPIC_IDS + '&limit=50';
	        break;

	        case 'topic' :
	            url = G_BASE_URL + '/search/ajax/search/?type=topics&q=' + encodeURIComponent(data) + '&limit=10';
	        break;

	        case 'questions' :
	        	url = G_BASE_URL + '/search/ajax/search/?type=questions&q=' + encodeURIComponent(data) + '&limit=10';
	        break;

	        case 'articles' :
	        	url = G_BASE_URL + '/search/ajax/search/?type=articles&q=' + encodeURIComponent(data) + '&limit=10';
	        break;

	    }

	    AWS.G.dropdown_list_xhr = $.get(url, function (result)
	    {
	        if (result.length != 0 && AWS.G.dropdown_list_xhr != undefined)
	        {
	            $(selector).parent().find('.aw-dropdown-list').html(''); // 清空内容
	            switch (type)
	            {
	                case 'search' :
	                    $.each(result, function (i, a)
	                    {
	                        switch (a.type)
	                        {
	                            case 'questions':
	                                if (a.detail.best_answer > 0)
	                                {
	                                    var active = 'active';
	                                }
	                                else
	                                {
	                                    var active = ''
	                                }

	                                $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.searchDropdownListQuestions).render(
	                                {
	                                    'url': a.url,
	                                    'active': active,
	                                    'content': a.name,
	                                    'discuss_count': a.detail.answer_count
	                                }));
	                                break;

								case 'articles':
	                                $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.searchDropdownListArticles).render(
	                                {
	                                    'url': a.url,
	                                    'content': a.name,
	                                    'comments': a.detail.comments
	                                }));
	                                break;

	                            case 'topics':
	                                $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.searchDropdownListTopics).render(
	                                {
	                                    'url': a.url,
	                                    'name': a.name,
	                                    'discuss_count': a.detail.discuss_count,
	                                    'topic_id': a.detail.topic_id
	                                }));
	                                break;

	                            case 'users':
	                                if (a.detail.signature == '')
	                                {
	                                    var signature = _t('暂无介绍');
	                                }
	                                else
	                                {
	                                    var signature = a.detail.signature;
	                                }

	                                $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.searchDropdownListUsers).render(
	                                {
	                                    'url': a.url,
	                                    'img': a.detail.avatar_file,
	                                    'name': a.name,
	                                    'intro': signature
	                                }));
	                                break;
	                        }
	                    });
	                break;

	                case 'publish' :
	                case 'topic_question' :
	                    $.each(result, function (i, a)
	                    {
	                        $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.questionDropdownList).render(
	                        {
	                            'url': a.url,
	                            'name': a.name
	                        }));
	                    });
	                	break;

	                case 'topic' :
	                    $.each(result, function (i, a)
	                    {
	                        $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.editTopicDorpdownList).render(
	                        {
	                            'name': a['name']
	                        }));
	                    });
	                	break;

	                case 'redirect' :
	                	$.each(result, function (i, a)
	                    {
	                        $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.questionRedirectList).render(
	                        {
	                            'url': "'" + G_BASE_URL + "/question/ajax/redirect/', 'item_id=" + $(selector).attr('data-id') + "&target_id=" + a['search_id'] + "'",
	                            'name': a['name']
	                        }));
	                    });
	                 	break;

	                case 'questions' :
	                case 'articles' :
	                	$.each(result, function (i, a)
	                    {
	                        $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.questionDropdownList).render(
	                        {
	                            'url': '#',
	                            'name': a['name']
	                        }));
	                    });
	                	break;

	                    $(selector).parent().find('.aw-dropdown-list li').click(function()
	                    {
	                    	$('.aw-question-list').append('<li data-id="'+$(this).attr('data-id')+'"><div class="col-sm-9">' + $(this).html() + '</div> <div class="col-sm-3"><a class="btn btn-danger btn-xs">删除</a></div></li>');

	                    	$('.aw-question-list li').find("a").attr('href',function(){
	                    		return $(this).attr("_href")

	                    	});

	                    	if ($('.question_ids').val() == '')
	                    	{
	                    		$('.question_ids').val($(this).attr('data-id') + ',');
	                    	}
	                    	else
	                    	{
	                    		$('.question_ids').val($('.question_ids').val() + $(this).attr('data-id') + ',');
	                    	}
	                    	$(".alert-box").modal('hide');
	                    });

	                    break;

	                case 'inbox' :
	                case 'invite' :
	                    $.each(result, function (i, a)
	                    {
	                        $(selector).parent().find('.aw-dropdown-list').append(Hogan.compile(AW_TEMPLATE.inviteDropdownList).render(
	                        {
	                            'uid': a.uid,
	                            'name': a.name,
	                            'img': a.detail.avatar_file
	                        }));
	                    });
	                	break;

	            }
	            if (type == 'publish')
	            {
	                $(selector).parent().find('.aw-publish-suggest-question, .aw-publish-suggest-question .aw-dropdown-list').show();
	            }
	            else
	            {
	                $(selector).parent().find('.aw-dropdown, .aw-dropdown-list').show().children().show();
	                $(selector).parent().find('.title').hide();
	                // 关键词高亮
	                $(selector).parent().find('.aw-dropdown-list li.question a').highText(data, 'b', 'active');
	            }
	        }else
	        {
	            $(selector).parent().find('.aw-dropdown').show().end().find('.title').html(_t('没有找到相关结果')).show();
	            $(selector).parent().find('.aw-dropdown-list, .aw-publish-suggest-question').hide();
	        }
	    }, 'json');

	}
}

AWS.Editor =
{
	// 设置编辑器预览状态
  set_editor_preview: function ()
  {
  	if ($.cookie('data_editor_preview') == 'true')
  	{
  		$('.icon-preview').parents('button').addClass('active');

  		$('.wmd-preview').fadeIn();
  	}
  	else
  	{
  		$('.icon-preview').parents('button').removeClass('active');

  		$('.wmd-preview').fadeOut();
  	}
  },

	// 向编辑器插入多媒体内容(图片,视频,超链接)
	add_multimedia: function (type)
	{
		switch (type){
			case 'img' :
				var title = 'imgsAlt',
					url = 'imgsUrl',
					textFeildValue = '![' + ($('#addTxtForms :input[name="' + title + '"]').val()) + '](' + $('#addTxtForms :input[name="' + url + '"]').val() + ')';
			break;

			case 'video' :
				var title = 'videoTitle',
					url = 'videoUrl',
					textFeildValue = '!![' + ($('#addTxtForms :input[name="' + title + '"]').val()) + '](' + $('#addTxtForms :input[name="' + url + '"]').val() + ')';
			break;

			case 'link' :
				var title = 'linkText',
					url = 'linkUrl',
					textFeildValue = '[' + ($('#addTxtForms :input[name="' + title + '"]').val()) + '](' + $('#addTxtForms :input[name="' + url + '"]').val() + ')';;
			break;
		}
		if($('#addTxtForms :input[name="' + url + '"]').val() == '')
    	{
    		return false;
    	}
    	if ($('.aw-mod-replay-box .advanced_editor').length)
        {
        	var selector = $('.aw-mod-replay-box .advanced_editor');
        }
        else if ($('.aw-mod-publish .advanced_editor').length)
        {
        	var selector = $('.aw-mod-publish .advanced_editor');
        }
        else if ($('.aw-mod-topic-edit-box .advanced_editor').length)
        {
        	var selector = $('.aw-mod-topic-edit-box .advanced_editor');
        }
        selector.insertAtCaret(textFeildValue);
	},

    // 向编辑器插入文本
  insert_into_codemirror: function (fn)
  {
      var flag = false, pos = {}, cursor = 0, textarea = $('.advanced_editor');
      if (advanced_editor != null)
      {
          pos = advanced_editor.getCursor('start');

          advanced_editor.toTextArea();

          for (var i = 0, iLen = pos.line; i < iLen; i++)
          {
              cursor += advanced_editor.lineInfo(i).text.length + 1;
          }

          cursor += pos.ch;

          if (textarea.createTextRange)
          {
              // quick fix to make it work on Opera 9.5
              /*if ($.browser.opera && $.browser.version >= 9.5 && advanced_editor.getSelection().length == 0)
              {
                  return false;
              }*/
              range = textarea.createTextRange();
              range.collapse(true);
              range.moveStart('character', cursor);
              range.moveEnd('character', advanced_editor.getSelection().length);
              range.select();
          }
          else if (textarea.setSelectionRange)
          {
              textarea.setSelectionRange(cursor, cursor + advanced_editor.getSelection().length);
          }
          textarea.focus();
          advanced_editor = null;
          flag = true;
      }

      fn(arguments[1]);

      if (flag)
      {
          advanced_editor = CodeMirror.fromTextArea(textarea,
          {
              mode: 'markdown',
              lineNumbers: true,
              theme: "default",
              extraKeys:
              {
                  "Enter": "newlineAndIndentContinueMarkdownList"
              }
          });

          if (typeof arguments[1] != 'undefined' && (arguments[1].offset || arguments[1].addline))
          {
              pos.line += (arguments[1].addline ? arguments[1].addline : 0);
              pos.ch += (arguments[1].offset ? arguments[1].offset : 0);
          }

          advanced_editor.setCursor(pos);
      }
  },

    // 编辑器插入附件
  insert_attach: function(selector, attach_id, attach_tag)
	{
    selector.parents('form').find('textarea').insertAtCaret("\n[" + attach_tag + "]" + attach_id + "[/" + attach_tag + "]\n");
	}
}


// 信息通知
AWS.Message =
{
	// 检测是否有新的通知信息
	check_notifications: function()
	{
		// 检测登录状态
	    if (G_USER_ID == 0)
	    {
	    	// 未登录时，停止AWS.Message.check_notifications()函数的执行
	    	clearInterval(AWS.G.notification_timer);
	        return false;
	    }
	    // 获取通知和私信的数量
	    dm.doAjax("get" ,API.notifications, function (result)
	    {
	    		// 私信未读取数量
	        $('#inbox_unread').html(Number(result.rsm.inbox_num));
	        console.log(result.rsm.inbox_num);
	        // 未读通知信息数量
	        var last_unread_notification = G_UNREAD_NOTIFICATION;
	        // 新通知信息数量
	        G_UNREAD_NOTIFICATION = Number(result.rsm.notifications_num);
	        // G_UNREAD_NOTIFICATION > 0时
	        if (G_UNREAD_NOTIFICATION > 0)
	        {
	            if (G_UNREAD_NOTIFICATION != last_unread_notification)
	            {
	            		// 重新加载通知消息列表
	                AWS.Message.load_notification_list();

	                // 给导航label添加未读消息数量
	                $('#notifications_unread').html(G_UNREAD_NOTIFICATION);
	            }
	            // 显示有私信和通知时的标题
	            document.title = '(' + (Number(result.rsm.notifications_num) + Number(result.rsm.inbox_num)) + ') ' + document_title;

	            $('#notifications_unread').show();
	        }
	        else
	        {
	        	// 当没有新的通知时隐藏通知列表、bage、设置头部通知信息
	            if ($('#header_notification_list').length)
	            {
	                $("#header_notification_list").html('<p class="aw-padding10" align="center">' + _t('没有未读通知') + '</p>');
	            }

	            if ($("#index_notification").length)
	            {
	                $("#index_notification").fadeOut();
	            }

	            document.title = document_title;

	            $('#notifications_unread').hide();
	        }

	        // 私信 判断是否显示私信的数量
	        if (Number(result.rsm.inbox_num) > 0)
	        {
	            $('#inbox_unread').show();
	        }
	        else
	        {
	            $('#inbox_unread').hide();
	        }

	    });
	},

	// 阅读通知
	read_notification: function(selector, notification_id , reload)
	{
	    if (notification_id)
	    {
	        selector.remove();

	        var url = G_BASE_URL + '/notifications/ajax/read_notification/notification_id-' + notification_id;
	    }
	    else
	    {
	        if ($("#index_notification").length)
	        {
	            $("#index_notification").fadeOut();
	        }

	        var url = G_BASE_URL + '/notifications/ajax/read_notification/';
	    }

	    $.get(url, function (result)
	    {
	        AWS.Message.check_notifications();

	        if (reload)
	        {
	            window.location.reload();
	        }
	    });
	},

	// 重新加载通知列表
	load_notification_list: function()
	{
			console.log($("#index_notification").length);
	    if ($("#index_notification").length)
	    {
	    	// 给首页通知box内label添加未读消息数量
	        $("#index_notification").fadeIn().find('[name=notification_unread_num]').html(G_UNREAD_NOTIFICATION);

	        $('#index_notification ul#notification_list').html('<p align="center" style="padding: 15px 0"><img src="../assets2/img/loading_b.gif"/></p>');
	        // 获取消息列表信息
	        dm.doAjax("get", API.notificationsList, function (result)
	        {
	            $('#index_notification ul#notification_list').html(result);
	            // 控制显示的通知数量为5条
	            AWS.Message.notification_show(5);
	        });
	    }

	    if ($("#header_notification_list").length)
	    {
	    		// 获取顶部消息列表返回的数据为空时显示没有未读信息
	       dm.doAjax("get", API.notificationsHeaderList, function (result)
	        {
	            if (result.length)
	            {
	                $("#header_notification_list").html(result);
	            }
	            else
	            {
	                $("#header_notification_list").html('<p class="aw-padding10" align="center">' + _t('没有未读通知') + '</p>');
	            }
	        });
	    }
	},

	// 控制通知数量
	notification_show: function(length)
	{
	    if ($('#index_notification').length > 0)
	    {
	    	// 无通知信息时隐藏消息通知
	    	if ($('#index_notification ul#notification_list li').length == 0)
	        {
	            $('#index_notification').fadeOut();
	        }
	        else
	        {
	        	// 当消息数量超过设定的值时，隐藏多余的消息
	        	$('#index_notification ul#notification_list li').each(function (i, e)
		        {
		            if (i < length)
		            {
		                $(e).show();
		            }
		            else
		            {
		                $(e).hide();
		            }
		        });
	        }
	    }
	}
}

AWS.Init =
{
	// 初始化问题评论框
	init_comment_box: function(selector)
	{
		// 点击添加评论
    $(document).on('click', selector, function ()
    { 
  		// 将邀请、相关问题页面隐藏
      $(this).parents('.aw-question-detail').find('.aw-invite-box, .aw-question-related-box').hide();
      if (typeof COMMENT_UNFOLD != 'undefined')
      {
        if (COMMENT_UNFOLD == 'all' && $(this).attr('data-comment-count') == 0 && $(this).attr('data-first-click') == 'hide')
        {
          $(this).removeAttr('data-first-click');
          return false;
        }
      }

      if (!$(this).attr('data-type') || !$(this).attr('data-id'))
      {
        return true;
      }

      var comment_box_id = '#aw-comment-box-' + $(this).attr('data-type') + '-' + 　$(this).attr('data-id');
      console.log($(comment_box_id).length);
      // 判断是否comment_box_id
      if ($(comment_box_id).length)
      {
    		// 评论框隐藏时，点击显示
        if ($(comment_box_id).css('display') == 'none')
        {
        	$(this).addClass('active');

          $(comment_box_id).fadeIn();
        }// 评论框显示时，点击隐藏
        else
        {
        	$(this).removeClass('active');
            $(comment_box_id).fadeOut();
        }
      }
      else
      {
    	  console.log(11111);
        // 动态插入commentBox
        // 判断div的类型
        switch ($(this).attr('data-type'))
        {
        	// data-type:question
          case 'question':
            var comment_form_action = API.SaveCommentList;
            var comment_data_url = API.getCommentList ;
            break;
          // data-type:answer
          case 'answer':
            var comment_form_action = API.saveAnswer ;
            var comment_data_url = API.getAnswer ;
            break;
        }
        console.log("G_USER_ID:" + G_USER_ID);
        if (G_USER_ID)
        {
      		// 如果已登录加载评论框
          $(this).parents('.aw-item').find('.mod-footer').append(Hogan.compile(AW_TEMPLATE.commentBox).render(
          {
            'comment_form_id': comment_box_id.replace('#', ''),
            'comment_form_action': comment_form_action
          }));
          //设置提交按钮的显示和隐藏
          $(comment_box_id).find('.aw-comment-txt').bind(
          {
            focus: function ()
            {
              $(comment_box_id).find('.aw-comment-box-btn').show();
            },

            blur: function ()
            {
              if ($(this).val() == '')
              {
                  $(comment_box_id).find('.aw-comment-box-btn').hide();
              }
            }
          });
          // 点击取消按钮
          $(comment_box_id).find('.close-comment-box').click(function ()
          {
            $(comment_box_id).fadeOut();
            $(comment_box_id).find('.aw-comment-txt').css('height', $(this).css('line-height'));
          });
        }
        else
        {
      		// 没有登录时
      		console.log(2222);
          $(this).parents('.aw-item').find('.mod-footer').append(Hogan.compile(AW_TEMPLATE.commentBoxClose).render(
          {
              'comment_form_id': comment_box_id.replace('#', ''),
              'comment_form_action': comment_form_action
          }));
        }
        console.log(1111,comment_data_url)
        // 判断是否有评论数据
        // comment_data_url
        dm.doAjax('get', comment_data_url, {"id" : $(this).attr('data-id')}, function (result)
        {
        	console.log("暂无评论");
            if ($.trim(result) == '')
            {
              result = '<div align="center" class="aw-padding10">' + _t('暂无评论') + '</div>';
            }

            $(comment_box_id).find('.aw-comment-list').html(result);
        },"html");

        // textarae自动增高
        $(comment_box_id).find('.aw-comment-txt').autosize();

        $(this).addClass('active');
      }
      // @人功能
      AWS.at_user_lists($(this).parents('.aw-item').find('.aw-comment-txt'));
    });
	},

	// 初始化文章评论框
	init_article_comment_box: function(selector)
	{
	    $(document).on('click', selector, function ()
	    {
	    	var _editor_box = $(this).parents('.aw-item').find('.aw-article-replay-box');
	        if (_editor_box.length)
	        {
	            if (_editor_box.css('display') == 'block')
	            {
	               _editor_box.fadeOut();
	            }
	            else
	            {
	                _editor_box.fadeIn();
	            }
	        }
	        else
	        {
	            $(this).parents('.content').append(Hogan.compile(AW_TEMPLATE.articleCommentBox).render(
	            {
	                'at_uid' : $(this).attr('data-id'),
	                'article_id' : $('.aw-topic-bar').attr('data-id')
	            }));
	        }
	    });
	},

	// 初始化话题编辑box
	init_topic_edit_box: function(selector) //selector -> .aw-edit-topic
	{
	    $(selector).click(function ()
	    {
	        var _topic_editor = $(this).parents('.aw-topic-bar'),
	        	data_id = _topic_editor.attr('data-id'),
	        	data_type = _topic_editor.attr('data-type');

	        if (!_topic_editor.hasClass('active'))
	        {
	        	_topic_editor.addClass('active');

	        	if (!_topic_editor.find('.topic-tag .close').length)
	        	{
	            	_topic_editor.find('.topic-tag').append('<a class="close"><i class="icon icon-delete"></i></a>');
	        	}
	        }
	        else
	        {
	            _topic_editor.addClass('active');
	        }

	        // 判断插入编辑box
	        if (_topic_editor.find('.aw-edit-topic-box').length == 0)
	        {
	            _topic_editor.append(AW_TEMPLATE.editTopicBox);

	            // 给编辑box添加按钮添加事件
	            _topic_editor.find('.add').click(function ()
	            {
	                if (_topic_editor.find('#aw_edit_topic_title').val() != '')
	                {
	                    switch (data_type)
	                    {
	                    	case 'publish':
		                        _topic_editor.find('.tag-bar').prepend('<span class="topic-tag"><a class="text">' + _topic_editor.find('#aw_edit_topic_title').val() + '</a><a class="close" onclick="$(this).parents(\'.topic-tag\').remove();"><i class="icon icon-delete"></i></a><input type="hidden" value="' + _topic_editor.find('#aw_edit_topic_title').val() + '" name="topics[]" /></span>').hide().fadeIn();

		                        _topic_editor.find('#aw_edit_topic_title').val('');
	                        break;

	                    	case 'question':
		                        $.post(G_BASE_URL + '/topic/ajax/save_topic_relation/', 'type=question&item_id=' + data_id + '&topic_title=' + encodeURIComponent(_topic_editor.find('#aw_edit_topic_title').val()), function (result)
		                        {
		                            if (result.errno != 1)
		                            {
		                                AWS.alert(result.err);

		                                return false;
		                            }

		                            _topic_editor.find('.tag-bar').prepend('<span class="topic-tag" data-id="' + result.rsm.topic_id + '"><a href="' + G_BASE_URL + '/topic/' + result.rsm.topic_id + '" class="text">' + _topic_editor.find('#aw_edit_topic_title').val() + '</a><a class="close"><i class="icon icon-delete"></i></a></span>').hide().fadeIn();

		                            _topic_editor.find('#aw_edit_topic_title').val('');
		                        }, 'json');
	                        break;

	                    	case 'article':
		                        $.post(G_BASE_URL + '/topic/ajax/save_topic_relation/', 'type=article&item_id=' + data_id + '&topic_title=' + encodeURIComponent(_topic_editor.find('#aw_edit_topic_title').val()), function (result)
		                        {
		                            if (result.errno != 1)
		                            {
		                                AWS.alert(result.err);

		                                return false;
		                            }

		                            _topic_editor.find('.tag-bar').prepend('<span class="topic-tag" data-id="' + result.rsm.topic_id + '"><a href="' + G_BASE_URL + '/topic/' + result.rsm.topic_id + '" class="text">' + _topic_editor.find('#aw_edit_topic_title').val() + '</a><a class="close"><i class="icon icon-delete"></i></a></span>').hide().fadeIn();

		                            _topic_editor.find('#aw_edit_topic_title').val('');
		                        }, 'json');
	                        break;


	                    	case 'topic':
		                        $.post(G_BASE_URL + '/topic/ajax/save_related_topic/topic_id-' + data_id, 'topic_title=' + encodeURIComponent(_topic_editor.find('#aw_edit_topic_title').val()), function (result)
		                        {
		                            if (result.errno != 1)
		                            {
		                                AWS.alert(result.err);

		                                return false;
		                            }

		                            _topic_editor.find('.tag-bar').prepend('<span class="topic-tag"><a href="' + G_BASE_URL + '/favorite/tag-' + encodeURIComponent(_topic_editor.find('#aw_edit_topic_title').val()) + '" class="text">' + _topic_editor.find('#aw_edit_topic_title').val() + '</a><a class="close"><i class="icon icon-delete"></i></a></span>').hide().fadeIn();

		                            _topic_editor.find('#aw_edit_topic_title').val('');
		                        }, 'json');
	                        break;

	                    	case 'favorite':
		                        $.post(G_BASE_URL + '/favorite/ajax/update_favorite_tag/', 'item_id=' + data_id + '&item_type=' + _topic_editor.attr('data-item-type') + '&tags=' + encodeURIComponent(_topic_editor.find('#aw_edit_topic_title').val()), function (result)
		                        {
		                            if (result.errno != 1)
		                            {
		                                AWS.alert(result.err);

		                                return false;
		                            }

		                            _topic_editor.find('.tag-bar').prepend('<span class="topic-tag"><a href="' + G_BASE_URL + '/favorite/tag-' + encodeURIComponent(_topic_editor.find('#aw_edit_topic_title').val()) + '" class="text">' + _topic_editor.find('#aw_edit_topic_title').val() + '</a><a class="close"><i class="icon icon-delete"></i></a></span>').hide().fadeIn();

		                            _topic_editor.find('#aw_edit_topic_title').val('');
		                        }, 'json');
	                        break;
	                    }
	                }
	            });

	            // 给编辑box取消按钮添加事件
	            _topic_editor.find('.close-edit').click(function ()
	            {
	            	_topic_editor.removeClass('active');
	                _topic_editor.find('.aw-edit-topic-box').hide();
	                _topic_editor.find('.aw-edit-topic').show();
	            });

	       		AWS.Dropdown.bind_dropdown_list($(this).parents('.aw-topic-bar').find('#aw_edit_topic_title'),'topic');
	        }

	        $(this).parents('.aw-topic-bar').find('.aw-edit-topic-box').fadeIn();

	        // 是否允许创建新话题
	        if (!G_CAN_CREATE_TOPIC)
	        {
	            $(this).parents('.aw-topic-bar').find('.add').hide();
	        }

	        $(this).hide();
	    });
	}
}

function _t(string, replace)
{
  if (typeof (aws_lang) != 'undefined')
  {
    if (typeof (aws_lang[string]) != 'undefined')
    {
        string = aws_lang[string];
    }
  }

  if (replace)
  {
    string = string.replace('%s', replace);
  }

  return string;
};

// jQuery扩展
(function ($)
{
	$.fn.extend(
    {
    	insertAtCaret: function (textFeildValue)
	    {
        var textObj = $(this).get(0);
        if (document.all && textObj.createTextRange && textObj.caretPos)
        {
          var caretPos = textObj.caretPos;
          caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == '' ?
                textFeildValue + '' : textFeildValue;
        }
        else if (textObj.setSelectionRange)
        {
          var rangeStart = textObj.selectionStart,
          	rangeEnd = textObj.selectionEnd,
          	tempStr1 = textObj.value.substring(0, rangeStart),
          	tempStr2 = textObj.value.substring(rangeEnd);
          textObj.value = tempStr1 + textFeildValue + tempStr2;
          textObj.focus();
          var len = textFeildValue.length;
          textObj.setSelectionRange(rangeStart + len, rangeStart + len);
          textObj.blur();
        }
        else
        {
          textObj.value += textFeildValue;
        }
    },

    highText: function (searchWords, htmlTag, tagClass)
    {
      return this.each(function ()
      {
        $(this).html(function high(replaced, search, htmlTag, tagClass)
        {
          var pattarn = search.replace(/\b(\w+)\b/g, "($1)").replace(/\s+/g, "|");
          // ig忽略大小写的全局搜索 替换搜索到关键字
          return replaced.replace(new RegExp(pattarn, "ig"), function (keyword)
          {
          	var returnValue = $("<" + htmlTag + " class=" + tagClass + ">" + keyword + "</" + htmlTag + ">").outerHTML();
              return returnValue;
          });


        }($(this).text(), searchWords, htmlTag, tagClass));
      });
    },

    outerHTML: function (s)
    {
    	// 获取this里的html
      return (s) ? this.before(s).remove() : jQuery("<p>").append(this.eq(0).clone()).html();      
    }
 	});

	$.extend(
	{
		// 滚动到指定位置  queue布尔值指示是否在效果队列中放置动画。如果为 false，则动画将立即开始
		scrollTo : function (type, duration, options)
		{
			if (typeof type == 'object')
			{
				// 获取位移的高度
				var type = $(type).offset().top
			}
			//滚动条跳动位移的高度
			$('html, body').animate({
				scrollTop: type
			}, {
				duration: duration,
				queue: options.queue
			});
		}
	})

})(jQuery);