/**
 * ueditor完整配置项
 * 可以在这里配置整个编辑器的特性
 */
/**************************提示********************************
 * 所有被注释的配置项均为UEditor默认值。
 * 修改默认配置请首先确保已经完全明确该参数的真实用途。
 * 主要有两种修改方案，一种是取消此处注释，然后修改成对应参数；另一种是在实例化编辑器时传入对应参数。
 * 当升级编辑器时，可直接使用旧版配置文件替换新版配置文件,不用担心旧版配置文件中因缺少新功能所需的参数而导致脚本报错。
 **************************提示********************************/

(function () {

    /**
     * 编辑器资源文件根路径。它所表示的含义是：以编辑器实例化页面为当前路径，指向编辑器资源文件（即dialog等文件夹）的路径。
     * 鉴于很多同学在使用编辑器的时候出现的种种路径问题，此处强烈建议大家使用"相对于网站根目录的相对路径"进行配置。
     * "相对于网站根目录的相对路径"也就是以斜杠开头的形如"/myProject/ueditor/"这样的路径。
     * 如果站点中有多个不在同一层级的页面需要实例化编辑器，且引用了同一UEditor的时候，此处的URL可能不适用于每个页面的编辑器。
     * 因此，UEditor提供了针对不同页面的编辑器可单独配置的根路径，具体来说，在需要实例化编辑器的页面最顶部写上如下代码即可。当然，需要令此处的URL等于对应的配置。
     * window.UEDITOR_HOME_URL = "/xxxx/xxxx/";
     */
    var URL = window.UEDITOR_HOME_URL || getUEBasePath();

    /**
     * 配置项主体。注意，此处所有涉及到路径的配置别遗漏URL变量。
     */
    window.UEDITOR_CONFIG = {

        //为编辑器实例添加一个路径，这个不能被注释
        UEDITOR_HOME_URL: URL

        // 服务器统一请求接口路径
        , serverUrl: window.ueditorServerUrl
        //工具栏上的所有的功能按钮和下拉框，可以在new编辑器的实例时选择自己需要的从新定义
        , toolbars: [[
            'fullscreen', 'source', '|', 'undo', 'redo', '|',
            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
            'indent', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
            'simpleupload', 'insertimage', 'insertvideo','insertframe', 'insertcode', 'pagebreak', 'template', 'background', '|',
            'horizontal', '|',
            'print', 'preview'
        ]],

        initialFrameWidth:600,
        initialFrameHeight:320,
        // 元素路径显示
        elementPathEnabled:false,
        // 工具栏不浮动
        autoFloatEnabled:false,
        // 本地保存
        enableAutoSave: false
        //,imagePopup:true      //图片操作的浮层开关，默认打开

        ,autoSyncData:true
        ,emotionLocalization:false //是否开启表情本地化，默认关闭。若要开启请确保emotion文件夹下包含官网提供的images表情文件夹

        //粘贴只保留标签，去除标签所有属性
        //,retainOnlyLabelPasted: false

        //,pasteplain:false  //是否默认为纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
        //纯文本粘贴模式下的过滤规则
        //'filterTxtRules' : function(){
        //    function transP(node){
        //        node.tagName = 'p';
        //        node.setStyle();
        //    }
        //    return {
        //        //直接删除及其字节点内容
        //        '-' : 'script style object iframe embed input select',
        //        'p': {$:{}},
        //        'br':{$:{}},
        //        'div':{'$':{}},
        //        'li':{'$':{}},
        //        'caption':transP,
        //        'th':transP,
        //        'tr':transP,
        //        'h1':transP,'h2':transP,'h3':transP,'h4':transP,'h5':transP,'h6':transP,
        //        'td':function(node){
        //            //没有内容的td直接删掉
        //            var txt = !!node.innerText();
        //            if(txt){
        //                node.parentNode.insertAfter(UE.uNode.createText(' &nbsp; &nbsp;'),node);
        //            }
        //            node.parentNode.removeChild(node,node.innerText())
        //        }
        //    }
        //}()

        //,allHtmlEnabled:false //提交到后台的数据是否包含整个html字符串

        

        //,autoTransWordToList:false  //禁止word中粘贴进来的列表自动变成列表标签

        //fontsize
        //字号
        //,'fontsize':[10, 11, 12, 14, 16, 18, 20, 24, 36]

        //paragraph
        //段落格式 值留空时支持多语言自动识别，若配置，则以配置值为准
        //,'paragraph':{'p':'', 'h1':'', 'h2':'', 'h3':'', 'h4':'', 'h5':'', 'h6':''}

        //rowspacingtop
        //段间距 值和显示的名字相同
        //,'rowspacingtop':['5', '10', '15', '20', '25']

        //rowspacingBottom
        //段间距 值和显示的名字相同
        //,'rowspacingbottom':['5', '10', '15', '20', '25']

        //lineheight
        //行内间距 值和显示的名字相同
        //,'lineheight':['1', '1.5','1.75','2', '3', '4', '5']

        //customstyle
        //自定义样式，不支持国际化，此处配置值即可最后显示值
        //block的元素是依据设置段落的逻辑设置的，inline的元素依据BIU的逻辑设置
        //尽量使用一些常用的标签
        //参数说明
        //tag 使用的标签名字
        //label 显示的名字也是用来标识不同类型的标识符，注意这个值每个要不同，
        //style 添加的样式
        //每一个对象就是一个自定义的样式
        //,'customstyle':[
        //    {tag:'h1', name:'tc', label:'', style:'border-bottom:#ccc 2px solid;padding:0 4px 0 0;text-align:center;margin:0 0 20px 0;'},
        //    {tag:'h1', name:'tl',label:'', style:'border-bottom:#ccc 2px solid;padding:0 4px 0 0;margin:0 0 10px 0;'},
        //    {tag:'span',name:'im', label:'', style:'font-style:italic;font-weight:bold'},
        //    {tag:'span',name:'hi', label:'', style:'font-style:italic;font-weight:bold;color:rgb(51, 153, 204)'}
        //]

        //打开右键菜单功能
        //,enableContextMenu: true
        //右键菜单的内容，可以参考plugins/contextmenu.js里边的默认菜单的例子，label留空支持国际化，否则以此配置为准
        //,contextMenu:[
        //    {
        //        label:'',       //显示的名称
        //        cmdName:'selectall',//执行的command命令，当点击这个右键菜单时
        //        //exec可选，有了exec就会在点击时执行这个function，优先级高于cmdName
        //        exec:function () {
        //            //this是当前编辑器的实例
        //            //this.ui._dialogs['inserttableDialog'].open();
        //        }
        //    }
        //]

        //快捷菜单
        //,shortcutMenu:["fontfamily", "fontsize", "bold", "italic", "underline", "forecolor", "backcolor", "insertorderedlist", "insertunorderedlist"]

        //elementPathEnabled
        //是否启用元素路径，默认是显示
        //,elementPathEnabled : true

        //wordCount
        ,wordCount:false          //是否开启字数统计
        ,maximumWords:100000       //允许的最大字符数
        //字数统计提示，{#count}代表当前字数，{#leave}代表还可以输入多少字符数,留空支持多语言自动切换，否则按此配置显示
        //,wordCountMsg:''   //当前已输入 {#count} 个字符，您还可以输入{#leave} 个字符
        //超出字数限制提示  留空支持多语言自动切换，否则按此配置显示
        //,wordOverFlowMsg:''    //<span style="color:red;">你输入的字符个数已经超出最大允许值，服务器可能会拒绝保存！</span>

        //tab
        //点击tab键时移动的距离,tabSize倍数，tabNode什么字符做为单位
        //,tabSize:4
        //,tabNode:'&nbsp;'

        //undo
        //可以最多回退的次数,默认20
        //,maxUndoCount:20
        //当输入的字符数超过该值时，保存一次现场
        //,maxInputCount:1

        //autoHeightEnabled
        // 是否自动长高,默认true
        ,autoHeightEnabled:false

        //scaleEnabled
        //是否可以拉伸长高,默认true(当开启时，自动长高失效)
        //,scaleEnabled:false
        //,minFrameWidth:800    //编辑器拖动时最小宽度,默认800
        //,minFrameHeight:220  //编辑器拖动时最小高度,默认220

        //autoFloatEnabled
        //是否保持toolbar的位置不动,默认true
        //,autoFloatEnabled:true
        //浮动时工具栏距离浏览器顶部的高度，用于某些具有固定头部的页面
        //,topOffset:30
        //编辑器底部距离工具栏高度(如果参数大于等于编辑器高度，则设置无效)
        //,toolbarTopOffset:400

        //pageBreakTag
        //分页标识符,默认是_ueditor_page_break_tag_
        //,pageBreakTag:'_ueditor_page_break_tag_'


        //tableDragable
        //表格是否可以拖拽
        ,tableDragable: false

        //,disabledTableInTable:true  //禁止表格嵌套

        //sourceEditor
        //源码的查看方式,codemirror 是代码高亮，textarea是文本框,默认是codemirror
        //注意默认codemirror只能在ie8+和非ie中使用
        //,sourceEditor:"codemirror"
        //如果sourceEditor是codemirror，还用配置一下两个参数
        //codeMirrorJsUrl js加载的路径，默认是 URL + "third-party/codemirror/codemirror.js"
        //,codeMirrorJsUrl:URL + "third-party/codemirror/codemirror.js"
        //codeMirrorCssUrl css加载的路径，默认是 URL + "third-party/codemirror/codemirror.css"
        //,codeMirrorCssUrl:URL + "third-party/codemirror/codemirror.css"
        //编辑器初始化完成后是否进入源码模式，默认为否。
        //,sourceEditorFirst:false

        //iframeUrlMap
        //dialog内容的路径 ～会被替换成URL,垓属性一旦打开，将覆盖所有的dialog的默认路径
        //,iframeUrlMap:{
        //    'anchor':'~/dialogs/anchor/anchor.html',
        //}

        //webAppKey 百度应用的APIkey，每个站长必须首先去百度官网注册一个key后方能正常使用app功能，注册介绍，http://app.baidu.com/static/cms/getapikey.html
        //, webAppKey: ""
    };

    function getUEBasePath(docUrl, confUrl) {

        return getBasePath(docUrl || self.document.URL || self.location.href, confUrl || getConfigFilePath());

    }

    function getConfigFilePath() {

        var configPath = document.getElementsByTagName('script');

        return configPath[ configPath.length - 1 ].src;

    }

    function getBasePath(docUrl, confUrl) {

        var basePath = confUrl;


        if (/^(\/|\\\\)/.test(confUrl)) {

            basePath = /^.+?\w(\/|\\\\)/.exec(docUrl)[0] + confUrl.replace(/^(\/|\\\\)/, '');

        } else if (!/^[a-z]+:/i.test(confUrl)) {

            docUrl = docUrl.split("#")[0].split("?")[0].replace(/[^\\\/]+$/, '');

            basePath = docUrl + "" + confUrl;

        }

        return optimizationPath(basePath);

    }

    function optimizationPath(path) {

        var protocol = /^[a-z]+:\/\//.exec(path)[ 0 ],
            tmp = null,
            res = [];

        path = path.replace(protocol, "").split("?")[0].split("#")[0];

        path = path.replace(/\\/g, '/').split(/\//);

        path[ path.length - 1 ] = "";

        while (path.length) {

            if (( tmp = path.shift() ) === "..") {
                res.pop();
            } else if (tmp !== ".") {
                res.push(tmp);
            }

        }

        return protocol + res.join("/");

    }

    window.UE = {
        getUEBasePath: getUEBasePath
    };

})();
