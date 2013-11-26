#!/bin/sh

if [ "$1" = "mac" ]; then
   arp -a $2 | awk '{ print $4 }'
elif [ "$1" = "testing" ]; then
   echo "Boo"
   sudo iptables -t mangle -N internets
else
   echo "Error"
fi
