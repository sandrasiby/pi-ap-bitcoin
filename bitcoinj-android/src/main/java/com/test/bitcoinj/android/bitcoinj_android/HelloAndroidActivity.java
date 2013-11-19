package com.test.bitcoinj.android.bitcoinj_android;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
        
        final PaymentChannelClient andClient = new PaymentChannelClient();    //Create client instance
        
        try {
        	Log.e(TAG, "Started Client");
			andClient.run("127.0.0.1");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Wallet bitcoinWallet = andClient.displayWallet();            //Show wallet contents
        String bal = bitcoinWallet.getBalance().toString();
        walletBalance.setText(bal);                                   //Set balance text with wallet balance
        
        final Button button1 = (Button) findViewById(R.id.open);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	//andClient.openConnection();                       //TO BE TESTED
            }
        });
        
        final Button button2 = (Button) findViewById(R.id.close);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	//andClient.closeConnection();                       //TO BE TESTED
            }
        });


        
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(com.test.bitcoinj.android.bitcoinj_android.R.menu.main, menu);
	return true;
    }

}

