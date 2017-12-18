var http = require('http');

http.createServer(function (request, response) {

    // 发送 HTTP 头部 
    // HTTP 状态值: 200 : OK
    // 内容类型: text/plain
    response.writeHead(200, {'Content-Type': 'text/plain'});

    // 发送响应数据 "Hello World"
    response.end('{"status":"0","path":"http://www.chuyingfund.com/files/account/headImg/120/2016/20160612131503913_1280x960.jpg"}');
}).listen(8333);

// 终端打印如下信息
console.log('Server running at http://127.0.0.1:8333/');