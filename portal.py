#!/usr/bin/env python

import cherrypy
import os
import subprocess

class CaptivePortal:

    @cherrypy.expose
    def default(self,*args,**kwargs):
        return open("C:\\Users\\Sandra\\Documents\\Semester Project 2\\acceptpage.html", "rb").read()
    

    @cherrypy.expose
    def accept(self):
        remoteip = cherrypy.request.headers["Remote-Addr"]
        remotemac = str(os.popen("arp -a " + str(remoteip) + " | awk '{ print $4 }' " ).read())
        
        print remoteip
        print remotemac

        changerule = os.popen("sudo iptables -t mangle -I internet 1 -m mac --mac-source " + remotemac + " -j RETURN")
        remtrack = os.popen("sudo rmtrack " + str(remoteip))

        raise cherrypy.HTTPRedirect("http://www.google.ch")
            

if __name__ == '__main__':
    cherrypy.config.update({
                        'server.socket_port': 8081,
                        'server.socket_host': '0.0.0.0'
                       })
    cherrypy.quickstart(CaptivePortal())
    
    

    
