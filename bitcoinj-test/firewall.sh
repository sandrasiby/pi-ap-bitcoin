#!/bin/sh
if [ "$1" = "open" ]; then
   #sudo iptables -t mangle -I internet 1 -m mac --mac-source $3 -j RETURN
   #sudo rmtrack $2
   echo ok
elif [ "$1" = "close" ]; then
   #sudo iptables -t mangle -D internet 1 -m mac --mac-source $3 -j RETURN
   #sudo rmtrack $2
   echo ok
elif [ "$1" = "mac" ]; then
   arp -a $2 | awk '{ print $4 }'
   echo "aa:bb:cc:dd:ee:ff"
else
   echo "Error in arguments"
fi
