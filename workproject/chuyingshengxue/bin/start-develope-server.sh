#!/bin/sh

npm update --registry=https://registry.npm.taobao.org

node dev.js

export PORT=3000
node www

echo enjoy your coding~
