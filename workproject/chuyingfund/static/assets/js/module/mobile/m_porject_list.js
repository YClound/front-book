define(function(require, exports, module) {
    require('template');
    
    
    var searchHelper = function () {
        var $btnSearch, $viewSearch, $inputSearch
        return {
            init: function () {
                $btnSearch = $('.btn-search')
                $viewSearch = $('.view-search')
                $inputSearch = $viewSearch.find('input[type=search]')
                $btnClear = $viewSearch.find('.btn-clear')

                $btnSearch.on('click', function () {
                    $viewSearch.show().find('input[type=search]').focus()
                })
                $('.btn-finish-search').on('click', function () {
                    $viewSearch.hide()
                })
                $('.btn-search-back').on('click', function () {
                    location.href = $(this).data('href')
                })
                $inputSearch.on('input focus', function (e) {
                    e.preventDefault();
                    if ($inputSearch.val().length) {
                        $btnClear.show()
                    } else {
                        $btnClear.hide()
                    }
                }).on('blur', function () {
                    //$btnClear.hide()
                })

                $btnClear.on('click', function (e) {
                    e.preventDefault();
                    $inputSearch.val('').focus()
                })

            }
        }
    }();
    //form persist
    $(function () {
        $('.s-header').headroom();
        var $infiniteLoading  =window.$infiniteLoading = $('.infinite-loading').infiniteLoading();

        dm.router(['/search', '/filter'], function (path) {
            dm.view.show(path.replace(/^\//, ''))
        });

        $('.view-search').on('shown', function () {
            $(this).find('input[type=search]').focus();
        });

        // 项目按类过滤
        var $formFilter = $('.form-filter');
        var $formPage = $infiniteLoading.find('form');
        var showResult = window.showResult= function () {
            console.log(2423)
            $('.proj-list').html('')
            dm.router.backTo()
            $formPage.find('[name=offset]').val(1)
            $infiniteLoading.infiniteLoading('reset')
            $('.s-tab-title').hide();
        };
        var submitFilter = function () {
            $.each(['type', 'industry', 'city'], function (i, field) {
                $formPage.find('[name=' + field + ']').val($formFilter.find('[name=' + field + ']').filter(':checked').val())
            })
            showResult()
        };
        $formFilter.find(':submit').on('click', function (e) {
            e.preventDefault()
            submitFilter()
        });

        var $formSearch = $('.form-search');
        var searchKeyword = window.searchKeyword = function (keyword) {
            $.each(['type', 'industry', 'city'], function (i, field) {
                $formPage.find('[name=' + field + ']').val('')
            })
            $formPage.find('[name=keyword]').val(keyword)
            var $headerResult = $('.s-header-result')
            $headerResult.removeClass('dn').find('.keyword').html(keyword)
            showResult()
        };
        
        $formSearch.on('submit', function (e) {
            e.preventDefault()
            var val = $formSearch.find('[name=keyword]').blur().val()
            $formSearch[0].reset()
            searchKeyword(val)
            pushSearchHistory(val)
        });

        $('.btn-cancel-search').on('click', function () {
            var $inputSearch = $formSearch.find('[name=keyword]'),
                    $inputKeyword = $formPage.find('[name=keyword]')
            if ($inputKeyword.val()) {
                $inputSearch.val('')
                $.each(['type', 'industry', 'city', 'keyword'], function (i, field) {
                    $formPage.find('[name=' + field + ']').val('')
                })
                $('.proj-list').html('')
                $formPage.find('[name=offset]').val(1)
                $formPage.find('[name=type]').val(dm.qs.type || 'hot')
                $infiniteLoading.infiniteLoading('reset')
            } else {
                $formPage.find('[name=type]').val(dm.qs.type || 'hot')
                $formPage.find('[name=offset]').val(1)
                $inputSearch.val('')
            }
            $('.s-header-result').addClass('dn')
            $('.s-tab-title').show()
        });

        $('.hot-search-list').on('click', 'li', function () {
            $formFilter[0].reset()
            $('#' + $(this).data('filter')).prop('checked', true)
            submitFilter()
        });

        var pushSearchHistory = function (val) {
            if (val == null || val == '') return;
            var searchHistory = store('searchHistory') || []
            var index = searchHistory.indexOf(val)
            if (index > -1) {
                searchHistory.splice(index, 1)
            }
            searchHistory.push(val)
            store('searchHistory', searchHistory)
            showSearchHistory()
        };

        var showSearchHistory = function () {
            var html = '',
                    history = store('searchHistory') || []

            for (var i = history.length; i > 0; i--) {
                html += '<li class="pt-6 pb-6 bb c-b">' + history[i - 1] + '</li>'
            }
            $('.search-history-list').html(html)
        };
        showSearchHistory();
        $('.clear-search-history').click(function () {
            store('searchHistory', null)
            showSearchHistory()
        });

        $('.search-history-list').on('click', 'li', function () {
            var keyword = $(this).text()
            $formSearch.find('[type="search"]').val(keyword)
            searchKeyword(keyword)
        });


        //searchHelper.init();
        var qs = dm.parseQS() || {};
        var query = qs.type || '';
        if(query){
            $('.s-tab-title a').removeClass('active');
            $('.s-tab-title a[data-api="'+query+'"]').addClass('active');
        }else {
            $('.s-tab-title a').eq(0).addClass('active');
        }
        //$('.xx').loading({empty: false, position: 'bottom'})
    });



    
      
});