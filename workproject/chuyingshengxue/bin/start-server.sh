#!/bin/sh

mkdir ../logs

npm update --registry=https://registry.npm.taobao.org

export PORT=3001
nohup node www > ../logs/server.log &

echo server has started.
