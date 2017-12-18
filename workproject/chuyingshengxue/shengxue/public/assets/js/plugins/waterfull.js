/*
    瀑布流
*/
(function(){
  //获取图片宽 和 高
  var imgReady = function( url, success, error ) {
      var width, height, intervalId, check, div,
          img = new Image(),
          body = document.body;
          
      img.src = url;
      
      // 从缓存中读取
      if (img.complete) {
          return success(img.width, img.height);
      }
      // 通过占位提前获取图片头部数据
      if (body) {
          div = document.createElement('div');
          div.style.cssText = 'position:absolute;left:-99999px;top:0;';
          div.appendChild(img);
          body.appendChild(div);
          width = img.offsetWidth;
          height = img.offsetHeight;
         
          check = function () {
             
              if (img.offsetWidth !== width || img.offsetHeight !== height) {
                  
                  clearInterval(intervalId);
                  success(img.offsetWidth, img.clientHeight);
                  img.onload = null;
                  div.innerHTML = '';
                  div.parentNode.removeChild(div);
              };

          };
          
          intervalId = setInterval(check, 150);
      }
      
      // 加载完毕后方式获取
      img.onload = function () {
          if(img.complete){
              success(img.width, img.height);
              img.onload = img.onerror = null;
              clearInterval(intervalId);
              body && img.parentNode &&img.parentNode.removeChild(img);
          }
              
      };
      
      // 图片加载错误
      img.onerror = function () {
          error && error();
          clearInterval(intervalId);
          body && img.parentNode && img.parentNode.removeChild(img);
      };
  };
  // 
  var Tiles = window.Tiles = function(container) {
    this.container = $(container);
    this.init = function(config) {
      if(!config) {
        config = {
          ajaxParams:{
            limit:10
          }
        };
      }
      console.log(this.container)
      
      
      if(this.container.length > 0) {
        this.config = this.container.data() || {};
        $.extend(this.config, config);
        
      }else {
        return;
      }
      console.log(this.config)
      //return;
      this.ajaxParams = config.ajaxParams || {};
      this.layouting = false;
      this.loadUrl = this.config.loadUrl;
      this.loader = this.container.find('.tile-loader');
      this.columns = [];
      this.columnCount = null;
      this.baseOffset = null;
      this.itemCount = null;
      this.fadeIndex = 0;
      var columnWidth = itemWidth = window.innerWidth<500? (window.innerWidth/2-10):232;
      this.columnWidth = config.columnWidth || columnWidth;
      this.itemOffset = config.itemOffset || 0;
      this.itemWidth = config.itemWidth || itemWidth;
      this.itemMargin = config.itemMargin || 5; // Space in between items horizontally and vertically.
      this.maxWidth = config.maxWidth || null;
      
      tiles = this;
      this.data = [];
      this.sectionData = null;
      
      
      
      
      this.currentPage = 0;
      
      this.collectData();
      
      
      var clear = this.config.clear?this.config.clear:true;
      
      this.container.html('').css('height', 0);
      if(this.data.length > 0) {
        this.updateLayout();
        //this.initAutoLayout();
        this.startEndlessScroll();
      }
      //onTileResize
     
    };

    this.onLightboxShown = function() {
      this.stopEndlessScroll();
    };

    this.onLightboxHidden = function() {
      this.startEndlessScroll();
    };
    
    this.collectData = function() {
      var sections = this.container;
      
      var i=0, length=sections.length, section;
      for(; i<length; i++) {
        section = $(sections[i]);
        this.addTiles(section);
      }
    };
    
    this.addTiles = function(element) {
      
      var sectionData = {
        container: element,
        tiles: $('.tile', element),
        columns: [],
        heights: [],
        type: element.data('type'),
        root: element.data('root')
      };
      this.sectionData = sectionData;
      this.data.push(sectionData);
      
      return sectionData;
    };

    this.onTileResize = function() {
      this.updateLayout();
    };
    
    this.updateLayout = function() {
      var i=0, length=this.data.length, section;
      for(; i<length; i++) {
        section = this.data[i];
        
        // Update number of tiles.
        section.tiles = $('.tile', section.container);
        
        this.layout(section);
      }
    };
    
    /**
     * Do a full layout update.
     */
    this.layout = function(data) {
      var maxWidth;
      var self = this;
    
      if(!data.tiles.length){
          return;
      }

      if(data.container) {
        maxWidth = data.container.outerWidth();
      }
      
      this.layouting = true;
      
      if(this.maxWidth) maxWidth = this.maxWidth;
      var columnWidth = this.columnWidth + this.itemMargin;
      var maxColumns = Math.floor(maxWidth / columnWidth);
      var baseOffset = Math.round(( maxWidth + this.itemMargin - (maxColumns*columnWidth))/2);
      var itemWidth = this.itemWidth;

      console.log('columnWidth', columnWidth);
      console.log('maxColumns', maxColumns);
      console.log('baseOffset', baseOffset);
      console.log('this.itemMargin', this.itemMargin);
      console.log('maxWidth', maxWidth, data.container.outerWidth());
      
      /*if(maxColumns == data.columns.length && this.baseOffset == baseOffset && this.itemCount == data.tiles.length) {
        this.columnLayout(data, columnCount);
        return;
      }*/
      
      var tileCount = data.tiles.length;
      var columnCount = maxColumns;
      
      data.heights = [];
      while(data.heights.length < maxColumns) {
        data.heights.push(0);
      }
      
      data.columns = [];
      while(data.columns.length < maxColumns) {
        data.columns.push([]);
      }
      
      var tile, top, left;
      var i = 0;
      var shortest = null;
      var shortestIndex = 0;

      var resetContainerHeight = function(){
        var maxHeight = 0;
        var i;
        for(i=0; i<columnCount; i++) {
          maxHeight = Math.max(maxHeight, data.heights[i]);
        }
        
        if(tile) {
          data.container.css({
            'height': maxHeight+'px'
          });
        }
      };
      var findS = function(){
        var shortest = null;
        var shortestIndex = 0;
        var k = 0;
        var result = null;
        shortest = null;
        shortestIndex = 0;
        //  heights 获取最小高度和最小高度的索引
        for(k = 0; k < maxColumns; k++) {
          if(shortest == null || data.heights[k] < shortest) {
            shortest = data.heights[k];
            shortestIndex = k;
          }
        }
        result = {
          shortest:shortest,
          shortestIndex:shortestIndex
        };
        //console.log(result,data.heights)
        return result
      };
      // 假如提供图片宽高直接添加 否则自己计算
      if(false){
        for(; i<tileCount; i++ ) {
          tile = $(data.tiles[i]);
          img = tile.find('.image img');
          src = img.attr('src');
          var result = findS();
          shortest = result.shortest;
          shortestIndex = result.shortestIndex;
          top = shortest;
          left = shortestIndex*columnWidth + baseOffset;
          tile.css({
            width:self.itemWidth+'px',
            position: 'absolute',
            top: top+'px',
            left: left+'px',
            opacity:1
          });
          // tile.outerHeight 容器的高度
          data.heights[shortestIndex] = shortest + tile.outerHeight() + self.itemMargin;
          data.columns[shortestIndex].push(tile);
        }
        self.layouting = false;
        resetContainerHeight();

      }else {
        var loaded = 0;
        for(; i<tileCount; i++ ) {
          tile = $(data.tiles[i]);

          (function(tile){
            var img = tile.find('.image img');
            var src = img.attr('src');
            
            imgReady(src,function(w,h){
              var newH = Math.floor(self.itemWidth*h/w);
              tile.closest('.tilt-img').css('height',newH+'px');
              
              var result = findS();
              var shortest = result.shortest;
              var shortestIndex = result.shortestIndex;
              var top = shortest;
              var left = shortestIndex*columnWidth + baseOffset;
              tile.css({
                width:self.itemWidth+'px',
                position: 'absolute',
                top: top+'px',
                left: left+'px',
                opacity:1
              });
              // tile.outerHeight 容器的高度
              data.heights[shortestIndex] = shortest + tile.outerHeight() + self.itemMargin;
              data.columns[shortestIndex].push(tile);
              
              loaded++;
              if(loaded>=tileCount){
                self.layouting = false;
                resetContainerHeight();
              }
            },function(){
              loaded++;
              if(loaded>=tileCount){
                self.layouting = false;
                resetContainerHeight();
              }
            });
          })(tile);   
        }
        
        
      }
      
    };
    
    /**
     * Lay out height only, based on column setup from previous full layout call.
     */
    this.columnLayout = function(data) {
      data.heights = [];
      
      var verticalOffset = 0;
      if(this.config.verticalOffset) {
        verticalOffset = this.config.verticalOffset;
      }
      
      while(data.heights.length < data.columns.length) {
        if(data.heights.length == 0) data.heights.push(0);
        else data.heights.push(verticalOffset);
      }
      
      var i=0, length = data.columns.length, column;
      var k=0, kLength, tile;
      for(; i<length; i++) {
        column = data.columns[i];
        kLength = column.length;
        for(k=0; k<kLength; k++) {
          tile = column[k];
          
          tile.css({
            top: data.heights[i]+'px'
          });
          
          data.heights[i] += tile.outerHeight() + this.itemOffset;
        }
      }
      
      var maxHeight = 0;
      for(i=0; i<length; i++) {
        maxHeight = Math.max(maxHeight, data.heights[i]);
      }
      data.container.css({
        'height': maxHeight+'px'
      });
    };
    
    /**
     * If browser supports CSS animations, fade images in nicely
     * once they are loaded.
     */
    this.fadeImages = function() {
      //if(Modernizr.opacity && Modernizr.cssanimations) {
        var imgs = $('.polaroid img');
        imgs.each(function(index) {
          // Imgs have complete if they are cached and don't need to be faded.
          if(!this.complete) {
            var t = $(this);
            t.addClass('imageLoading');
            t.one('load', function(index) {
              $(this).addClass('imageLoaded');
            });
          }
        });
      //}
    };
    
    this.initAutoLayout = function() {
      //$(window).resize($.proxy(this.resize, this));
    };
    
    this.resize = function() {
      clearTimeout(this.layoutTimer);
      this.layoutTimer = setTimeout($.proxy(this.updateLayout, this), 500);
    };
    
    this.onAfterUpdateElement = function(li) {
      //$('results').style.position = 'relative';
    };
    
    // Stuff for endless scroll.
    this.startEndlessScroll = function() {
      //console.log('startEndlessScroll');
      clearInterval(this.interval);
      this.interval = setInterval($.proxy(this.evaluateScrollPosition, this), 100);
    };
    
    this.stopEndlessScroll = this.stop = function() {
      clearInterval(this.interval);
    };
    
    this.evaluateScrollPosition = function() {
      var container = this.container;
      //console.log(this.layouting)
      var preloadDistance = 100;
      var position = this.getPageHeight() - this.getScrollHeight();
      //console.log('evaluateScrollPosition', position, this.getPageHeight(), this.getScrollHeight());
      if(position <= preloadDistance && this.layouting == false && !container.is(':hidden')) {
        //console.log(11111,this.layouting)
        this.loadMore();
      }
    };
    
    this.getScrollHeight = function(){
      var y;
      // all except Explorer
      if (self.pageYOffset) {
          y = self.pageYOffset;
      } else if (document.documentElement && document.documentElement.scrollTop) {
          y = document.documentElement.scrollTop;
      } else if (document.body) {
          y = document.body.scrollTop;
      }
      return parseInt(y)+this.getWindowHeight();
    };
    
    this.getWindowHeight = function(){
      var frameWidth;
      var frameHeight;
      if (self.innerWidth) {
        frameWidth = self.innerWidth;
        frameHeight = self.innerHeight;
      } else if (document.documentElement && document.documentElement.clientWidth) {
        frameWidth = document.documentElement.clientWidth;
        frameHeight = document.documentElement.clientHeight; 
      } else if (document.body) {
        frameWidth = document.body.clientWidth;
        frameHeight = document.body.clientHeight;
      }
      return parseInt(frameHeight);
    };

    this.getPageHeight = function(){
      return $(document).height();
    };
    
    this.clear = function() {
      this.container.find('.tile,.tileTrash').remove();
    };
    
    this.loadMore = function() {
      //gbks.events.call(gbks.event.LoadMoreStart);
      var self = this;
      
      var ref = this;
      this.stopEndlessScroll();
      this.showLoader();
      //this.config.page = this.currentPage;
      var ajaxParams = this.ajaxParams;
      var url = this.loadUrl;
      var data = $.extend({}, ajaxParams, {start:this.currentPage++});
      
      window.APP && window.APP.get(url,data).done(function(res){
        self.onLoadMore(res);
      }).fail(function(){
        self.onLoadMoreError(res);
      });
      /*$.ajax({
        url: url,
        data: data,
        success: $.proxy(this.onLoadMore, this),
        error: $.proxy(this.onLoadMoreError, this)
      });*/

      //console.log('loadMore', this.config);
      
      //this.track('loadMore', this.config.type, this.config.page.toString());
    };
    
    this.track = function(one, two, three) {
      if(typeof(_gaq) !== 'undefined') {
        _gaq.push(['_trackEvent', one, two, three]);
      }
    };
    
    this.onLoadMore = function(result) {
      var self = this;
      var container = this.container;
      var config = this.config;
      // setTimeout测试用
      setTimeout(function(){
        try{
          var res = result;
          var list = res.msg || [];
  
          if(list.length){
            var html = APP.makeList('TPL_TILE',list);
            var dom = $(html).appendTo(container);
            
            self.sectionData.tiles = dom;
            self.fadeImages();
            self.startEndlessScroll();
            self.hideLoader();
            self.updateLayout();
            config.callback && config.callback();

          }else {
            self.loader.html('已经没有更多了');
            self.stopEndlessScroll();
          }
            
        }catch(e){
          console.log(e)
          self.stopEndlessScroll();
        }
          
      },10);
        

      //gbks.events.call(gbks.event.LoadMoreDone);
    };
    
    this.onLoadMoreError = function(result) {
      this.hideLoader();

      //gbks.events.call(gbks.event.LoadMoreError);
    };
    
    this.showLoader = function() {
      this.loader.stop();
      this.loader.show();
      this.loader.css('opacity',1);
    };
    
    this.hideLoader = function() {
      this.loader.stop();
      var callback = null;
      if(this.onHideLoader) {
        callback = $.proxy(this.onHideLoader, this);
      }
      this.loader.animate({opacity:0}, 250, callback);
    };
    
    this.onHideLoader = function(event) {
      this.loader.hide().css('opacity', '0');;
    };
  };


})();

  