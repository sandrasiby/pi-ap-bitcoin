package com.test.bitcoinjand;

import java.io.File;

import com.google.bitcoin.core.Wallet;
import com.test.bitcoinjand.PaymentChannelClient;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
        
        final PaymentChannelClient andClient = new PaymentChannelClient(this); //Create client instance
        
        
        //Capture host details. To be tested. Commenting for now.
        //Uri data = getIntent().getData();
        //String host = data.getHost();
        
        
        //Alternative method to Thread.
        //new SyncChain(this).execute(andClient);
        
        Log.e(TAG, "Starting Thread");
        
        new Thread(new Runnable() {
            public void run() {
              andClient.startWalletAppKit();
            }
          }).start();

              
       Log.e(TAG, "Thread started");
        
       
       
      // final String balance = andClient.displayWallet();
       //walletBalance.setText(balance);
       //Log.e(TAG, "First Balance shown");
       
         final Button button1 = (Button) findViewById(R.id.open);
       button1.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               // Perform action on click
                    andClient.openConnection();
                   walletBalance.setText(andClient.displayWallet());
                    Log.e(TAG, "Opened");
           }
       });
       
       final Button button2 = (Button) findViewById(R.id.close);
       button2.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               // Perform action on click
                    andClient.closeConnection();
                    Log.e(TAG, "Closed");
           }
       });

        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.test.bitcoinjand.R.menu.main, menu);
        return true;
    }

}

