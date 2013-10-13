#!/bin/bash

sudo iptables -t mangle -N internet
sudo iptables -t mangle -A PREROUTING -i wlan0 -p tcp -m tcp --dport 80 -j internet
sudo iptables -t mangle -A internet -j MARK --set-mark 99
sudo iptables -t nat -A PREROUTING -i wlan0 -p tcp -m mark --mark 99 -m tcp --dport 80 -j DNAT --to-destination 192.168.42.1:8080
