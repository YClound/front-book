var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var proxy = require('express-http-proxy');
var serverConfig = require('./server_config');

var app = express();

//proxy 在localhost的情况下不去获取真实数据 只有在host为ip并且/openapi/的情况下才获取真实数据
app.use('/', proxy(serverConfig.host, {
	filter : function(req, res) {
		
		if(req.host && req.host.indexOf('local')>-1){
			return false;
		}
		return req.path.startsWith('/openapi/');
	},
	decorateRequest : function(proxyReq, originalReq) {
		// you can update headers
		proxyReq.headers['_PROXY_FROM'] = 'jiushi-h5';
		//prefix a context name
		proxyReq.path = serverConfig.contextName + proxyReq.path;
		return proxyReq;
	}
}));

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'shengxue')));

var server = app.listen(3001, function () {
  var host = server.address().address;
  var port = server.address().port;
  console.log('Example app listening at http://%s:%s', host, port);
});
// catch 404 and forward to error handler
/*app.use(function(req, res, next) {
	res.redirect('/404.html');
});*/

// error handler
// app.use(function(err, req, res, next) {
//   // set locals, only providing error in development
//   res.locals.message = err.message;
//   res.locals.error = req.app.get('env') === 'development' ? err : {};
//
//   // render the error page
//   res.status(err.status || 500);
//   res.render('error');
// });
module.exports = app;
