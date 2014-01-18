package com.android.piapclient;

import com.android.piapclient.PaymentChannelClient;
import com.android.piapclient.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	final String TAG = "MainActvity";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final TextView walletBalance = (TextView) findViewById(R.id.balance);
        
        final PaymentChannelClient andClient = new PaymentChannelClient(this); //Create client instance
        
        
        //Capture host details. To be tested. Commenting for now.
        //Uri data = getIntent().getData();
        //String host = data.getHost();
                
        Log.e(TAG, "Starting Thread");
        
        walletBalance.setText("Synchronizing");
        
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
