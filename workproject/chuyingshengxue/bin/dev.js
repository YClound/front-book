var bs = require("browser-sync").create();
var app = require('../app');
var http = require('http');

var port = 3000;
app.set('port', port);
var server = http.createServer(app);
server.listen(port);

bs.init({
	open : false,
	ui : false,
	proxy: "http://localhost:" + port,
	files : ['../public']
});

console.log(`在浏览器中打开 http://localhost:${port} 边编写代码边查看效果，不用刷新~`);