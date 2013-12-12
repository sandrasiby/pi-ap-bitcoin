#!/usr/bin/env python

import os
import subprocess
import cherrypy
import urllib
import shlex, subprocess

from cherrypy import expose
from string import Template

config = {
    "ap_domain": "bitcoin-ap",
    "default_redirect" : "http://www.ethz.ch"
    }

class CaptivePortal(object):

    @cherrypy.expose
    def default(self,*args,**kwargs):
        global config
        redirect_to = config['default_redirect']

        if 'redirect_to' in kwargs.keys():
            redirect_to = kwargs['redirect_to']
        template = Template(open("splash_page.html", "rb").read())

        return template.substitute(redirect_to=redirect_to, ap_domain=config['ap_domain'])

    @cherrypy.expose
    def accept(self, **kwargs):
        
        raise cherrypy.HTTPRedirect(kwargs['redirect_to'])

class Catcher(object):
    """
    A simple CherryPy Web Request handler that takes care of redirecting every 
    user to the access point, passing the originally requested URL as parameter.
    """

    @expose
    def default(self,*args,**kwargs):
        global config
        raise cherrypy.HTTPRedirect("http://" +config['ap_domain'] + ":8080/?redirect_to=" + urllib.quote(cherrypy.url()))

if __name__ == '__main__':
    cherrypy.config.update({
                        'server.socket_port': 8080,
                        'server.socket_host': '0.0.0.0'
                       })
    conf = {
        "/": {
            "request.dispatch": cherrypy.dispatch.VirtualHost(
                **{
                    config['ap_domain'] + ":8080": "/bitcoin_ap",
                    }
                  )
            }
        }

    catcher = Catcher()
    cherrypy.tree.mount(catcher, "/", conf)
    catcher.bitcoin_ap = CaptivePortal()
    cherrypy.engine.start()
    cherrypy.engine.block()
    
    

    
