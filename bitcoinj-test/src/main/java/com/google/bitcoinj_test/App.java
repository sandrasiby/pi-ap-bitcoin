package com.google.bitcoinj_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String remoteip = "192.168.178.1";
    	Process p;
    	String execcommand = "arp -a " + remoteip;
    	String[] cmd = {"./test.sh", "testing"};
    	
    	System.out.println(execcommand);
    	
    	try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			
    		System.out.println(System.getProperty("user.dir"));
			//ProcessBuilder pb = new ProcessBuilder("./test.sh", "testing");
			//Process pc = pb.start();
	   	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        System.out.println( "Hello World!" );
    }
}
