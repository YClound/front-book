#!/bin/sh

ps -ef | grep node | grep -v grep | xargs killall
killall node

echo server has shutdown.
