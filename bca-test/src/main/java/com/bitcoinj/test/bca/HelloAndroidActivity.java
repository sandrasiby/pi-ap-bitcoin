package com.bitcoinj.test.bca;

import java.io.File;

import com.google.bitcoin.core.Wallet;
import com.bitcoinj.test.bca.PaymentChannelClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class HelloAndroidActivity extends Activity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	final String TAG = "MainActvity";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final TextView walletBalance = (TextView) findViewById(R.id.balance);
        
        final PaymentChannelClient andClient = new PaymentChannelClient(HelloAndroidActivity.this);    //Create client instance
        
        /*try {
        	Log.e(TAG, "Started Client");
			andClient.run("127.0.0.1");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
//        File sdCard = Environment.getExternalStorageDirectory();
//      File dir = new File (sdCard.getAbsolutePath() + "/wf");
//        dir.mkdirs();
//        Log.e(TAG,sdCard.getAbsolutePath());
        
       andClient.displayWallet();            //Show wallet contents
       Log.e(TAG, "Loaded wallet kit");
       // String bal = bitcoinWallet.getBalance().toString();
        //walletBalance.setText(bal);                    
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(com.bitcoinj.test.bca.R.menu.main, menu);
	return true;
    }

}

