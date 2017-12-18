(function () {
    'use strict';
    var s = function a(t) {
        var e = t.parentNode;
        if ("BODY" === e.tagName) return window;
        var i = getComputedStyle(e).getPropertyValue("overflow-y");
        return "auto" === i ? e: a(e)
    };
    var WebPullToRefresh = function($el,onRefresh){
        var data = {
            wins: [],
            child: null,
            startY: 0,
            translate: 0,
            status: null,
            direction: null
        };
        this.damping =  5;
        this.threshold = 60;
        this.$el = $el;
        console.log(this.$el)
        $.extend(this, data);
        this.onRefresh = onRefresh || function(){};
        this.init();


    };
    WebPullToRefresh.prototype = {
        getScrollTop: function() {
            var t = 0;
            return this.wins.forEach(function(e) {
                t += e === window ? pageYOffset: e.scrollTop
            }),
            t
        },
        onTranslate:function(translate){
            var el = $('.pull-content')[0];
            el.style.transform = el.style.webkitTransform = "translateY(" + translate + "px)";

        },
        handleTouchStart: function(t) {
            "loading" !== this.status && 0 === this.getScrollTop() && (this.startY = t.touches[0].clientY);
            console.log('handleTouchStart',this.startY,this.status,this.getScrollTop())
        },
        handleTouchMove: function(t) {
            if ("loading" !== this.status) {
                var e = t.touches[0].clientY,
                i = (e - this.startY) / this.damping; 
                ("down" === this.direction || i > 0 && 0 === this.getScrollTop()) && (this.direction || (this.direction = "down", 0 === this.startY && (this.startY = e, i = 0)), i < 0 && (i = 0), this.direction = "down", this.translate = i, i > this.threshold ? this.status = "can-drop": this.status = "pulling", t.preventDefault(), t.stopPropagation());
                if("pulling" === this.status){
                    $('.pull-icon').html('<div class="mt-10"><i class="if icon-arrow-down"></i> <span class="fz-small">下拉可刷新</span>')
                }else if("can-drop" === this.status){
                    $('.pull-icon').html('<div class="mt-10"><i class="if icon-arrow-down"></i> <span class="fz-small">松开以刷新</span>')
                }
                $('.pull')[0].className = 'pull ' + this.status;
                    
                this.onTranslate(this.translate)
                
                
            }
        },
        handleTouchEnd: function(t) {
            this.startY = 0,
            this.direction = null,
            null != this.status && "loading" !== this.status && (t.preventDefault(), t.stopPropagation(), "can-drop" === this.status ? (this.status = "loading", this.translate = this.threshold) : (this.translate = 0, this.status = null));
            console.log('handleTouchEnd',1);

            if("loading"===this.status){
                $('.pull-icon').html('<div class="dm-loading"></div>');
                $('.pull')[0].className = 'pull ' + this.status;
                this.onTranslate(this.translate);
                this._onRefresh();
            }else {
                this.reset(); 
            }
            
            //this.reset();
        },
        init: function() {
            var t = this.$el,
            e = t.children[0];
            //"auto" === getComputedStyle(e).getPropertyValue("overflow-y") && this.wins.push(e),this.wins.push(s(t)),
            t.addEventListener("touchstart", $.proxy(this.handleTouchStart,this)),
            t.addEventListener("touchmove", $.proxy(this.handleTouchMove,this)),
            t.addEventListener("touchend", $.proxy(this.handleTouchEnd,this)),
            t.addEventListener("touchcancel", $.proxy(this.handleTouchEnd,this))
        },
        reset: function() {
            this.translate = 0,
            this.status = null,
            this.startY = 0,
            this.direction = null;
            this._reset();
            this.onTranslate(0);
            
        },
        _onRefresh:function(){
            var self = this;
            this.onRefresh();
        },
        _reset:function(){
            $('.pull-icon').html('');
            $('.pull').removeClass('pulling can-drop loading');
        }
    };

    window.WebPullToRefresh = WebPullToRefresh;

})();