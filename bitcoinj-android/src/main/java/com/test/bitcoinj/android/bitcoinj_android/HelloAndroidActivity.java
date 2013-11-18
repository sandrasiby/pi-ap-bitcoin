package com.test.bitcoinj.android.bitcoinj_android;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.bitcoin.core.Wallet;
import com.test.bitcoinj.android.bitcoinj_android.PaymentChannelClient;

public class HelloAndroidActivity extends Activity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
	
	String TAG = "Android";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final TextView walletBalance = (TextView) findViewById(R.id.balance);
        
        PaymentChannelClient andClient = new PaymentChannelClient();
        
        try {
        	Log.e(TAG, "Started Client");
			andClient.run("127.0.0.1");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Wallet bitcoinWallet = andClient.displayWallet();
        String bal = bitcoinWallet.getBalance().toString();
        walletBalance.setText(bal);
        
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(com.test.bitcoinj.android.bitcoinj_android.R.menu.main, menu);
	return true;
    }

}

