#!/bin/sh

if [ "$1" = "testing" ]; then
   sudo iptables -t mangle -N internets
else
   echo "Error"
fi
