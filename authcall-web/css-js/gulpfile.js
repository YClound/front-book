const gulp = require('gulp');
const watch = require('gulp-watch');
const less = require('gulp-less');
const autoprefixer = require('gulp-autoprefixer');
const cssMin = require('gulp-minify-css');

gulp.task('live', function () {
    return watch('./common/less/**.less', function () {
        gulp.src('./common/less/**.less')
            .pipe(less())
            .pipe(autoprefixer({
                browsers: ['last 2 versions', 'Android >= 4.0', 'iOS 7', 'last 3 Safari versions', '>5%', 'ie 6-8'],
                cascade: false, //是否美化属性值 默认：true 像这样
            }))
            //.pipe(cssMin())
            .pipe(gulp.dest('./common/css'))
    })
})