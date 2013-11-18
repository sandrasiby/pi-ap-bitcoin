#!/bin/sh

if [ "$1" = "open" ]; then
   sudo iptables -t mangle -I internet 1 -m mac --mac-source $3 -j RETURN
   sudo rmtrack $2
elif [ "$1" = "close" ]; then
   sudo iptables -t mangle -D internet 1 -m mac --mac-source $3 -j RETURN
   sudo rmtrack $2
else
   echo "Error in arguments"
fi