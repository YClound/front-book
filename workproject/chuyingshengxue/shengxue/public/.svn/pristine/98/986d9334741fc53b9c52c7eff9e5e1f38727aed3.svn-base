/*
 *
 *  web  config
 *
 */
(function() {
	
	var qq = window.qq = {};
	
	qq.extend = function(first, second) {
		for (var prop in second) {
			first[prop] = second[prop];
		}
	};
	qq.indexOf = function(arr, elt, from) {
		if (arr.indexOf) {
			return arr.indexOf(elt, from);
		}
		from = from || 0;
		var len = arr.length;
		if (from < 0) {
			from += len;
		}
		for (; from < len; from++) {
			if (from in arr && arr[from] === elt) {
				return from;
			}
		}
		return -1;
	};
	qq.getUniqueId = (function() {
		var id = 0;
		return function() {
			return id++;
		};
	})();
	qq.attach = function(element, type, fn) {
		if (element.addEventListener) {
			element.addEventListener(type, fn, false);
		} else {
			if (element.attachEvent) {
				element.attachEvent("on" + type, fn);
			}
		}
	};
	qq.detach = function(element, type, fn) {
		if (element.removeEventListener) {
			element.removeEventListener(type, fn, false);
		} else {
			if (element.attachEvent) {
				element.detachEvent("on" + type, fn);
			}
		}
	};
	qq.preventDefault = function(e) {
		if (e.preventDefault) {
			e.preventDefault();
		} else {
			e.returnValue = false;
		}
	};
	qq.insertBefore = function(a, b) {
		b.parentNode.insertBefore(a, b);
	};
	qq.remove = function(element) {
		element.parentNode.removeChild(element);
	};
	qq.contains = function(parent, descendant) {
		if (parent == descendant) {
			return true;
		}
		if (parent.contains) {
			return parent.contains(descendant);
		} else {
			return !!(descendant.compareDocumentPosition(parent) & 8);
		}
	};
	qq.toElement = (function() {
		var div = document.createElement("div");
		return function(html) {
			div.innerHTML = html;
			var element = div.firstChild;
			div.removeChild(element);
			return element;
		};
	})();
	qq.css = function(element, styles) {
		if (styles.opacity != null) {
			if (typeof element.style.opacity != "string" && typeof(element.filters) != "undefined") {
				styles.filter = "alpha(opacity=" + Math.round(100 * styles.opacity) + ")";
			}
		}
		qq.extend(element.style, styles);
	};
	qq.hasClass = function(element, name) {
		var re = new RegExp("(^| )" + name + "( |$)");
		return re.test(element.className);
	};
	qq.addClass = function(element, name) {
		if (!qq.hasClass(element, name)) {
			element.className += " " + name;
		}
	};
	qq.removeClass = function(element, name) {
		var re = new RegExp("(^| )" + name + "( |$)");
		element.className = element.className.replace(re, " ").replace(/^\s+|\s+$/g, "");
	};
	qq.setText = function(element, text) {
		element.innerText = text;
		element.textContent = text;
	};
	qq.children = function(element) {
		var children = [],
			child = element.firstChild;
		while (child) {
			if (child.nodeType == 1) {
				children.push(child);
			}
			child = child.nextSibling;
		}
		return children;
	};
	qq.getByClass = function(element, className) {
		if (element.querySelectorAll) {
			return element.querySelectorAll("." + className);
		}
		var result = [];
		var candidates = element.getElementsByTagName("*");
		var len = candidates.length;
		for (var i = 0; i < len; i++) {
			if (qq.hasClass(candidates[i], className)) {
				result.push(candidates[i]);
			}
		}
		return result;
	};
	window.logger = {
		render : function() {
			var tpl = '<div class="logger-show"><a href="javascript:;" class="loggerBtnShow c-f">展开</a></div><div class="logger-header" id="loggerHeader"> <a href="javascript:;" class="c-f" id="loggerBtnClose"><u>关闭</u> </a> <a href="javascript:;" class="c-f" id="loggerBtnMin"><u>最小</u> </a> <a href="javascript:;" class="c-f" id="loggerBtnZoom"><u>伸缩</u> </a> </div> <div class="logger-main" id="loggerMain"> </div>';
			var dom = document.createElement('div');
			dom.id = "logger";
			dom.className = "logger-wrapper";
			dom.innerHTML = tpl;
			
			document.body.appendChild(dom)
		},
		init : function() {
			this.render();
			document.getElementById('loggerBtnClose').addEventListener('click', function() {
				document.getElementById('logger').style.display = 'none';
			}, false);
			document.getElementById('loggerBtnMin').addEventListener('click', function() {
				//document.getElementById('logger').style.display = 'none';
			}, false);
			document.getElementById('loggerBtnZoom').addEventListener('click', function() {
				
				document.getElementById('logger').style.width = window.innerWidth + 'px';
				document.getElementById('logger').style.height = window.innerHeight + 'px';
			}, false);
		},
		// msg,url,line,col,error
		print : function(data) {
			var dom = document.getElementById('loggerMain');
			var str = '<div>line:' + data.line + '</div>' + '<div>msg:' + data.msg + '</div>' + '<div>url:' + data.url + '</div>';
			dom.innerHTML = str;
			qq.addClass(document.getElementById('logger'), 'show');
			
			
		},
		remove : function() {
		},
		zoom : function() {
		},
		min : function() {
		}
	};
	window.onerror = function(msg, url, line, col, error) {
		//没有URL不上报！上报也不知道错误
		if (msg != "Script error." && !url) {
			return true;
		}
		//采用异步的方式
		//我遇到过在window.onunload进行ajax的堵塞上报
		//由于客户端强制关闭webview导致这次堵塞上报有Network Error
		//我猜测这里window.onerror的执行流在关闭前是必然执行的
		//而离开文章之后的上报对于业务来说是可丢失的
		//所以我把这里的执行流放到异步事件去执行
		//脚本的异常数降低了10倍
		setTimeout(function() {
			var data = {};
			//不一定所有浏览器都支持col参数
			col = col || (window.event && window.event.errorCharacter) || 0;
			
			data.url = url;
			data.line = line;
			data.col = col;
			if (!!error && !!error.stack) {
				//如果浏览器有堆栈信息
				//直接使用
				data.msg = error.stack.toString();
			} else if (!!arguments.callee) {
				//尝试通过callee拿堆栈信息
				var ext = [];
				var f = arguments.callee.caller, c = 3;
				//这里只拿三层堆栈信息
				while (f && (--c > 0)) {
					ext.push(f.toString());
					if (f === f.caller) {
						break;//如果有环
					}
					f = f.caller;
				}
				ext = ext.join(",");
				data.msg = error.stack.toString();
			}
			//把data上报到后台
			console.log('error', data);
			logger.print(data);
		}, 0);
		
		return true;
	};
	
	var UA = window.UA = function(ua, appVersion, platform) {
		return {
			userAgent : ua,
			
			
			win32 : platform === "Win32",
			ie : /MSIE ([^;]+)/.test(ua),
			ieMobile : window.navigator.msPointerEnabled,
			ieVersion : Math.floor((/MSIE ([^;]+)/.exec(ua) || [0, "0"])[1]),
			
			
			ios : (/iphone|ipad/gi).test(appVersion),
			iphone : (/iphone/gi).test(appVersion),
			ipad : (/ipad/gi).test(appVersion),
			iosVersion : parseFloat(('' + (/CPU.*OS ([0-9_]{1,5})|(CPU like).*AppleWebKit.*Mobile/i.exec(ua) || [0, ''])[1])
				.replace('undefined', '3_2').replace('_', '.').replace('_', '')) || false,
			safari : /Version\//gi.test(appVersion) && /Safari/gi.test(appVersion),
			uiWebView : /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(ua),
			
			
			android : (/android/gi).test(appVersion),
			androidVersion : parseFloat("" + (/android ([0-9\.]*)/i.exec(ua) || [0, ''])[1]),
			
			// chrome
			chrome : /Chrome/gi.test(ua),
			chromeVersion : parseInt(( /Chrome\/([0-9]*)/gi.exec(ua) || [0, 0] )[1], 10),
			
			
			webkit : /AppleWebKit/.test(appVersion),
			
			
			Browser : / Browser/gi.test(appVersion),
			MiuiBrowser : /MiuiBrowser/gi.test(appVersion),
			
			
			MicroMessenger : ua.toLowerCase().match(/MicroMessenger/i) == "micromessenger",
			
			
			canTouch : "ontouchstart" in document
		};
	}(navigator.userAgent, navigator.appVersion, navigator.platform);
	
	
	window.dm = {
		// 是否 APP
		isAPP : function() {
			return /dreammove/.test(window.navigator.userAgent.toLowerCase())
		}(),
		
		// 是否 Android
		isAndroid : function() {
			return /dreammove-android/.test(window.navigator.userAgent.toLowerCase())
		}(),
		
		// 是否 IOS
		isIOS : function() {
			return /dreammove-ios/.test(window.navigator.userAgent.toLowerCase())
		}(),
		callAPP : function(fn, arg1) {
			if (dm.isIOS) {
				if (arg1) {
					fn += ('?' + arg1)
				}
				var iFrame
				iFrame = document.createElement("iframe")
				iFrame.setAttribute("src", "dreammove://native?function=" + fn)
				iFrame.setAttribute("style", "display:none;")
				iFrame.setAttribute("height", "0px")
				iFrame.setAttribute("width", "0px")
				iFrame.setAttribute("frameborder", "0")
				document.body.appendChild(iFrame)
				iFrame.parentNode.removeChild(iFrame)
				iFrame = null
			} else if (dm.isAndroid) {
				if (arg1) {
					window.JSBridge && window.JSBridge[fn] && window.JSBridge[fn](arg1)
				} else {
					window.JSBridge && window.JSBridge[fn] && window.JSBridge[fn]()
				}
			}
			console.log(fn)
		}
	};
	
	/*window.addEventListener('error', function (e) {
	 var target = e.target
	 if (target.tagName === 'IMG' && !target.hasAttribute('noerr')) {
	 target.setAttribute('noerr', '1')
	 var dft = target.dataset ? target.dataset.default : target.getAttribute('data-default')
	 target.src = '/Public/Home/mobile/img/' + (dft || 'default-img.jpg')
	 }
	 }, true);*/
	// fz = 320px/20=16px, max-width=640px
	!function() {
		var a = document.documentElement;
		var b = a.clientWidth / 20;
		
		b = Math.floor(b) - 2;
		a.style.fontSize = (b > 16 ? 16 : b) + "px"
	}();
	
	
})();


/*
 
 赋值 cookie('a','value',1)
 获取 cookie('a')
 移除 cookie.remove('a')
 全部清空 cookie.clear()
 获取全部 cookie.all()
 */
;(function(root, factory) {
	
	root.cookie = factory();
	
	/*if (typeof define === 'function' && define.amd) {
	 // AMD. Register as an anonymous module.
	 define([], factory);
	 } else if (typeof exports === 'object') {
	 // Node.js
	 module.exports = factory();
	 } else {
	 // Browser globals (root is window)
	 
	 root.cookie = factory();
	 }*/
}(this, function(root, undefined) {
	var Cookie = {
		cookieAPI : {
			get : function(name) {
				var nameEQ = name + "=";
				var ca = document.cookie.split(';');//把cookie分割成组
				for (var i = 0; i < ca.length; i++) {
					var c = ca[i];//取得字符串
					while (c.charAt(0) == ' ') {//判断一下字符串有没有前导空格
						c = c.substring(1, c.length);//有的话，从第二位开始取
					}
					if (c.indexOf(nameEQ) == 0) {//如果含有我们要的name
						return unescape(c.substring(nameEQ.length, c.length));//解码并截取我们要值
					}
				}
				return false;
			},
			set : function(name, value, options) {
				if (Cookie.isPlainObject(name)) {
					for (var k in name) {
						if (name.hasOwnProperty(k)) this.set(k, name[k], value);
					}
				} else {
					var opt = Cookie.isPlainObject(options) ? options : {expires : options},
						expires = opt.expires !== undefined ? opt.expires : '',
						expiresType = typeof(expires),
						path = opt.path !== undefined ? ';path=' + opt.path : ';path=/',
						domain = opt.domain ? ';domain=' + opt.domain : '',
						secure = opt.secure ? ';secure' : '';
					
					//过期时间
					if (expiresType === 'string' && expires !== '') expires = new Date(expires);
					else if (expiresType === 'number') expires = new Date(+new Date + 1000 * 60 * 60 * 24 * expires);
					if (expires !== '' && 'toGMTString' in expires) expires = ';expires=' + expires.toGMTString();
					
					
					document.cookie = name + "=" + escape(value) + expires + path + domain + secure;   //转码并赋值
				}
			},
			remove : function(names) {
				names = Cookie.isArray(names) ? names : Cookie.toArray(arguments);
				for (var i = 0, l = names.length; i < l; i++) {
					this.set(names[i], '', -1);
				}
				return names;
			},
			clear : function(name) {
				return this.remove(Cookie.getKeys(this.all()));
			},
			all : function() {
				if (document.cookie === '') return {};
				var cookies = document.cookie.split('; '), result = {};
				for (var i = 0, l = cookies.length; i < l; i++) {
					var item = cookies[i].split('=');
					result[unescape(item[0])] = unescape(item[1]);
				}
				return result;
			}
		},
		// Object.names : return []
		getKeys : Object.names || function(obj) {
			var names = [], name = '';
			for (name in obj) {
				if (obj.hasOwnProperty(name)) names.push(name);
			}
			return names;
		},
		// 所谓"纯粹的对象"，就是该对象是通过"{}"或"new Object"创建的
		isPlainObject : function(value) {
			return !!value && Object.prototype.toString.call(value) === '[object Object]';
		},
		isArray : function(value) {
			return value instanceof Array
		},
		toArray : function(value) {
			return Array.prototype.slice.call(value);
		}
	};
	
	cookie = function(name, value, options) {
		//console.log(this)
		var argm = arguments,
			_cookie = function() {
				if (argm.length === 0) return cookie.clear();
				if (Cookie.isPlainObject(name) || (argm.length > 1 && name && value))
					return cookie.set(name, value, options);
				if (value === null) return cookie.remove(name);
				if (argm.length === 1) return cookie.get(name);
				return cookie.all();
			}
		return _cookie()
	}
	for (var a in Cookie.cookieAPI) cookie[a] = Cookie.cookieAPI[a];
	return cookie
}));


// 去除广告
(function(d) {
	
	var invalidJSDomain = [];
	invalidJSDomain.push("http://61.160.200.252");
	
	function isValid(src) {
		src = src.toLowerCase();
		for (var i = 0; i < invalidJSDomain.length; i++) {
			if (src.indexOf(invalidJSDomain[i]) > -1) {
				return true;
			}
		}
		return false;
	}
	
	setTimeout(function() {
		var head = d.getElementsByTagName('head')[0];
		var body = d.body || {};
		var orgAppendChild = head.appendChild;
		var orgAppendChild2 = body.appendChild;
		head.appendChild = function(node) {
			if (node && node.src && isValid(node.src)) {
				console.log('head fuck you :' + node.src);
			} else {
				orgAppendChild.apply(this, arguments);
			}
			return node;
		};
		body.appendChild = function(node) {
			if (node && node.src && isValid(node.src)) {
				console.log('body fuck you :' + node.src);
			} else {
				orgAppendChild2.apply(this, arguments);
			}
			return node;
		};
		var orgRemoveChild = head.removeChild;
		head.removeChild = function(node) {
			if (node && node.src && isValid(node.src)) {
				console.log('fuck you again :' + node.src);
			} else {
				orgRemoveChild.apply(this, arguments);
			}
			return node;
		}
		
	}, 0);
}(document));

//bridge support
!function() {
	if (['IPHONE', 'IPAD'].indexOf(navigator.platform.toUpperCase()) != -1) {
		//this is an ios uiwebview
		window.__JSFRBRIDGE = {
			__call : function(cmd, configStr, callbackName) {
				var url = 'jsbridge://' + cmd + '?';
				
				url += 'cmd=' + encodeURIComponent(cmd);
				configStr ? url += '&configStr=' + encodeURIComponent(configStr) : null;
				callbackName ? url += '&callbackName=' + encodeURIComponent(callbackName) : null;
				
				var iframe = document.createElement('iframe');
				iframe.style.width = '1px';
				iframe.style.height = '1px';
				iframe.style.display = 'none';
				iframe.src = url;
				document.body.appendChild(iframe);
				setTimeout(function() {
					iframe.remove();
				}, 100);
			}
		}
	}
	
	if (window.__JSFRBRIDGE) {
		window.JSFRBridge = window.__JSFRBRIDGE;
		// delete window.__JSFRBRIDGE;
		var bridge = window.JSFRBridge;
		bridge.call = function(cmd, config, callback) {
			//create a local callback like jsonp
			var callbackName = null;
			if (callback) {
				callbackName = 'callback' + new Date().getTime();
				
				window[callbackName] = function(res) {
					try {
						callback(res);
					} catch(e) {} finally {
						//delete the callback
						delete window[callbackName];
					}
				}
			}
			
			window.JSFRBridge.__call(cmd, JSON.stringify(config), callbackName);
		};
	} else {
		// mock local
		window.JSFRBridge = {
			call : function(cmd, config, callback) {
				console.log('call::', cmd, config);
				var res = {data : 0};
				callback ? callback(res) : null;
				return res;
			}
		};

		setTimeout(function() {
			!function(element) {
				var event;
				if (document.createEvent) {
					event = document.createEvent("HTMLEvents");
					event.initEvent("jsfrbridgeready", true, true);
				} else {
					event = document.createEventObject();
					event.eventType = "jsfrbridgeready";
				}
				event.eventName = "jsfrbridgeready";
				if (document.createEvent) {
					element.dispatchEvent(event);
				} else {
					element.fireEvent("on" + event.eventType, event);
				}
			}(document);
		}, 2000);
	}
}();