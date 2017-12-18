/*
 HTML
 .city-select
 input[type=hidden]
 input
 .dropdown-content
 .tab-title
 .tab-content
 .prov-box
 .city-box
 */
(function ($) {
    var cityData;
    cityData = null;
    
    return $.fn.citySelect = function (method, arg1, arg2) {
        var citySelect, that;
        if (!this.length) {
            return;
        }
        that = this;
        citySelect = function () {
            return that.each(function () {
                var $button, $cityBox, $dpc, $inputCity, $inputProv, $provBox, $select, $tabContent, $tabTitle, _provInfo, buildCityDOM, html, i, len, oriCity, oriProv, switchTab, updateValue;
                $select = $(this);
                $dpc = $select.find('.dropdown-content');
                $tabTitle = $dpc.find('.tab-title');
                $tabContent = $dpc.find('.tab-content');
                $button = $select.children('.btn');
                $provBox = $tabContent.children('.prov-box');
                $cityBox = $tabContent.children('.city-box');
                $inputProv = $select.find('[name2=province]');
                $inputCity = $select.find('[name2=city]');
                if(!$inputProv.length){
                    $inputProv = $select.find('[name=province]');
                }
                if(!$inputCity.length){
                    $inputCity = $select.find('[name=city]');
                }
                if ($provBox.html() && !method) {
                    return;
                }
                buildCityDOM = function (provId, city) {
                    var _cityInfo, _k, _provInfo, _v, html, i, len;
                    provId = '' + provId;
                    for (i = 0, len = cityData.length; i < len; i++) {
                        _provInfo = cityData[i];
                        if (provId === '' + _provInfo.id) {
                            _cityInfo = _provInfo.cities;
                            html = '';
                            for (_k in _cityInfo) {
                                _v = _cityInfo[_k];
                                html += '<u data-id="' + _k + '">' + _v + '</u>';
                            }
                            $cityBox.html(html);
                            $cityBox.find('[data-id=' + city + ']').addClass('checked')
                        }
                    }
                };
                updateValue = function (prov, city) {
                    var provName = $provBox.children().removeClass('checked').filter('[data-id=' + (prov || 0) + ']').addClass('checked').text()
                    buildCityDOM(prov, city)
                    var cityName = $cityBox.children().removeClass('checked').filter('[data-id=' + (city || 0) + ']').addClass('checked').text()
                    if (!prov && !city) $button.val('请选择城市')
                    else $button.val(provName + ' / ' + cityName)

                    $inputProv.val(prov)
                    $inputCity.val(city)

                }
                if (method === 'update') {
                    updateValue(arg1, arg2);
                    return
                }
                switchTab = function (target) {
                    $tabTitle.children('[data-target="' + target + '"]').addClass('active').siblings().removeClass('active');
                    $tabContent.children(target).show().siblings().hide();
                }
                if (method === 'reset') {
                    $button.val('请选择城市')
                    $inputProv.val('')
                    $inputCity.val('')
                    $cityBox.html('<b class="c-danger">请选择省份</b>')
                    switchTab('.prov-box')
                    return
                }
                html = '';
                for (i = 0, len = cityData.length; i < len; i++) {
                    _provInfo = cityData[i];
                    html += '<u data-id="' + _provInfo.id + '">' + _provInfo.name + '</u>';
                }
                $provBox.html(html);
                oriProv = $inputProv.val() || '';
                oriCity = $inputCity.val() || '';
                if (oriCity) {
                    updateValue(oriProv, oriCity);
                }
                $select.on('click.stopPropagation', function (e) {
                    e.stopPropagation();
                });
                $(document).on('click.closeDropdown', function () {
                    return $dpc.hide()
                });
                $button.on('click.dropdown', function (e) {
                    $dpc.toggle()
                });

                $tabTitle.on('click.switchTab', 'li', function () {
                    var $this, target;
                    $this = $(this);
                    target = $this.data('target');
                    switchTab(target);
                });
                $provBox.on('click.selectProv', 'u', function () {
                    var $this, provId, provName;
                    $this = $(this);
                    $this.addClass('checked').siblings('.checked').removeClass('checked');
                    switchTab('.city-box');
                    provId = $this.data('id');
                    provName = $this.text();
                    buildCityDOM(provId);
                    $button.val(provName);
                    return $inputProv.val(provId);
                });
                $cityBox.on('click.selectDone', 'u', function () {
                    var $this, _cityArr;
                    $this = $(this);
                    $this.addClass('checked').siblings('.checked').removeClass('checked');
                    _cityArr = $button.val().split('/').map(function (s) {
                        return s.trim();
                    });
                    _cityArr[1] = $this.text();
                    $button.val(_cityArr.join(' / ')).validate();
                    $dpc.hide();
                    return $inputCity.val($this.data('id'));
                });
            });
        }
        if (cityData) {
            citySelect();
        } else {
            if (!api.cities) throw new Error('require api.cities')
            $.getJSON(api.cities).then(function (json) {
                cityData = json;
                return citySelect();
            })
        }
        return that;
    };
})(jQuery);

$(function () {
    $('.city-select').citySelect()
})
