package com.test.bitcoinjand;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.bitcoin.core.*;
import com.google.bitcoin.kits.WalletAppKit;
import com.google.bitcoin.params.TestNet3Params;
import com.google.bitcoin.protocols.channels.PaymentChannelClientConnection;
import com.google.bitcoin.protocols.channels.StoredPaymentChannelClientStates;
import com.google.bitcoin.protocols.channels.ValueOutOfRangeException;
import com.google.bitcoin.utils.BriefLogFormatter;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

import static com.google.bitcoin.core.Utils.CENT;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;

import com.test.bitcoinjand.PaymentChannelClient;

public class SyncChain extends AsyncTask<PaymentChannelClient, String, WalletAppKit>{
	
	private HelloAndroidActivity context;
	
	public SyncChain(HelloAndroidActivity ctx) {
		this.context = ctx;	
	}
	
	 @Override
     protected void onPreExecute() {
     }


	@Override
	protected WalletAppKit doInBackground(PaymentChannelClient... appClient) {
		
		publishProgress("Synchronizing");
		
		appClient[0].appKit = new WalletAppKit(TestNet3Params.get(), context.getFilesDir(), "payment_channel_example_client") {
            @Override
            protected void addWalletExtensions() {
                // The StoredPaymentChannelClientStates object is responsible for, amongst other things, broadcasting
                // the refund transaction if its lock time has expired. It also persists channels so we can resume them
                // after a restart.
                wallet().addExtension(new StoredPaymentChannelClientStates(wallet(), peerGroup()));
            }
        };
        
        //appClient[0].appKit.startAndWait();
        		
        return appClient[0].appKit;
        
	}
	
	protected void onProgressUpdate(String... progress) {
		
		final TextView walletBalance = (TextView) context.findViewById(R.id.balance);
		walletBalance.setText(progress[0]);
		
    }

    protected void onPostExecute(WalletAppKit appKit) {
    	
    	Log.e("SyncChain", "Completed making kit");
    	
    	appKit.startAndWait();
    	
//    	final TextView walletBalance = (TextView) context.findViewById(R.id.balance);
//		walletBalance.setText(appKit.wallet().getBalance().toString());
		
        
    }
	
}