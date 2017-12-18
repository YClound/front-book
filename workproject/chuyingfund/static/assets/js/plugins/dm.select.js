/*!
 * jquery.dmSelect() - v0.5.1
 * http://adam.co/lab/jquery/dmSelect/
 * 2014-03-19
 *
 * Copyright 2013 Adam Coulombe
 * @license http://www.opensource.org/licenses/mit-license.html MIT License
 * @license http://www.gnu.org/licenses/gpl.html GPL2 License 
 */

(function ($) {
    'use strict';

    $.fn.extend({
        dmSelect: function (options) {
            // filter out <= IE6
            if (typeof document.body.style.maxHeight === 'undefined') {
                return this;
            }
            var defaults = {
                customClass: 'dm-select-',
                mapClass: true,
                mapStyle: true
            }
            options = $.extend(defaults, options)
            var prefix = options.customClass,
                changed = function ($select, dmSelectSpan) {
                    var currentSelected = $select.find(':selected'),
                        dmSelectSpanInner = dmSelectSpan.children(':first'),
                        html = currentSelected.html() || '&nbsp;';

                    dmSelectSpanInner.html(html);

                    if (currentSelected.attr('disabled')) {
                        dmSelectSpan.addClass(getClass('disabled-option'));
                    } else {
                        dmSelectSpan.removeClass(getClass('disabled-option'));
                    }

                    setTimeout(function () {
                        dmSelectSpan.removeClass(getClass('open'));
                        $(document).off('mouseup.dmSelect');
                    }, 60);
                },
                getClass = function (suffix) {
                    return prefix + suffix;
                };

            return this.each(function () {
                var $select = $(this),
                    dmSelectInnerSpan = $('<span></span>').addClass(getClass('inner')),
                    dmSelectSpan = $('<span></span>');

                $select.after(dmSelectSpan.append(dmSelectInnerSpan).append('<i class="if icon-angle-down poa r-0 zi-1"></i>'));

                dmSelectSpan.addClass('dm-select');

                if (options.mapClass) {
                    dmSelectSpan.addClass($select.attr('class'));
                }
                if (options.mapStyle) {
                    dmSelectSpan.attr('style', $select.attr('style'));
                }

                $select
                    .addClass('has-dm-select')
                    .on('render.dmSelect', function () {
                        changed($select, dmSelectSpan);
                        $select.css('width', '');
                        var selectBoxWidth = parseInt($select.outerWidth(), 10) -
                            (parseInt(dmSelectSpan.outerWidth(), 10) -
                            parseInt(dmSelectSpan.width(), 10));

                        // Set to inline-block before calculating outerHeight
                        dmSelectSpan.css({
                            display: 'inline-block'
                        });

                        var selectBoxHeight = dmSelectSpan.outerHeight();

                        if ($select.attr('disabled')) {
                            dmSelectSpan.addClass(getClass('disabled'));
                        } else {
                            dmSelectSpan.removeClass(getClass('disabled'));
                        }

                        dmSelectInnerSpan.css({
                            width: selectBoxWidth,
                            display: 'inline-block'
                        });

                        $select.css({
                            '-webkit-appearance': 'menulist-button',
                            width: dmSelectSpan.outerWidth(),
                            position: 'absolute',
                            opacity: 0,
                            height: selectBoxHeight,
                            fontSize: dmSelectSpan.css('font-size')
                        });
                    })
                    .on('change.dmSelect', function () {
                        dmSelectSpan.addClass(getClass('changed'));
                        changed($select, dmSelectSpan);
                    })
                    .on('keyup.dmSelect', function (e) {
                        if (!dmSelectSpan.hasClass(getClass('open'))) {
                            $select.trigger('blur.dmSelect');
                            $select.trigger('focus.dmSelect');
                        } else {
                            if (e.which == 13 || e.which == 27) {
                                changed($select, dmSelectSpan);
                            }
                        }
                    })
                    .on('mousedown.dmSelect', function () {
                        dmSelectSpan.removeClass(getClass('changed'));
                    })
                    .on('mouseup.dmSelect', function (e) {

                        if (!dmSelectSpan.hasClass(getClass('open'))) {
                            // if FF and there are other selects open, just apply focus
                            if ($('.' + getClass('open')).not(dmSelectSpan).length > 0 && typeof InstallTrigger !== 'undefined') {
                                $select.trigger('focus.dmSelect');
                            } else {
                                dmSelectSpan.addClass(getClass('open'));
                                e.stopPropagation();
                                $(document).one('mouseup.dmSelect', function (e) {
                                    if (e.target != $select.get(0) && $.inArray(e.target, $select.find('*').get()) < 0) {
                                        $select.trigger('blur.dmSelect');
                                    } else {
                                        changed($select, dmSelectSpan);
                                    }
                                });
                            }
                        }
                    })
                    .on('focus.dmSelect', function () {
                        dmSelectSpan.removeClass(getClass('changed')).addClass(getClass('focus'));
                    })
                    .on('blur.dmSelect', function () {
                        dmSelectSpan.removeClass(getClass('focus') + ' ' + getClass('open'));
                    })
                    .on('mouseenter.dmSelect', function () {
                        dmSelectSpan.addClass(getClass('hover'));
                    })
                    .on('mouseleave.dmSelect', function () {
                        dmSelectSpan.removeClass(getClass('hover'));
                    })
                    .trigger('render.dmSelect');
            });
        }
    });
})(jQuery);
