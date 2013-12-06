package com.bitcoinj.test.bca;

import android.content.Context;
import android.util.Log;

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

public class PaymentChannelClient {
    
    private WalletAppKit appKit;
    private final BigInteger channelSize;
    private final ECKey myKey;
    private final NetworkParameters params;
    private String host = "127.0.0.1";              //TO BE CHANGED (URI)
    private int rounds;
    private int interval;  //minutes
    private String TAG = "PaymentClient";
    private Context context;
    
    public PaymentChannelClient(Context ctx) {
    	this.context = ctx;
        channelSize = CENT;
        myKey = new ECKey();
        params = TestNet3Params.get();
        rounds = 10;
        interval = 5;
    }
    
    public void run(final String host) throws Exception {
    	
    	appKit = new WalletAppKit(params, context.getFilesDir(), "payment_channel_example_client") {
            @Override
            protected void addWalletExtensions() {
                // The StoredPaymentChannelClientStates object is responsible for, amongst other things, broadcasting
                // the refund transaction if its lock time has expired. It also persists channels so we can resume them
                // after a restart.
                wallet().addExtension(new StoredPaymentChannelClientStates(wallet(), peerGroup()));
            }
        };
        appKit.startAndWait();
        
        System.out.println(appKit.wallet());
    }
    
    public String displayWallet()
    {
    	return appKit.wallet().getBalance().toString();
    	    	
    }
    
    public void openConnection(){
    	
    	appKit.wallet().addKey(myKey);
    	appKit.wallet().allowSpendingUnconfirmedTransactions();
    	
    	final int timeoutSecs = 15;
        final InetSocketAddress server = new InetSocketAddress(host, 4242);
        
        waitForSufficientBalance(channelSize);
        final String channelID = host;
        
        Log.e(TAG, "Round one ...");
        try {
			openAndSend(timeoutSecs, server, channelID);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ValueOutOfRangeException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        Log.e(TAG, "Waiting ...");
        try {
			Thread.sleep(60 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
        Log.e(TAG, "Stopping ...");
        appKit.stopAndWait();       
   }
    
    public void closeConnection()
    {
    	appKit.stopAndWait();
    }
    
    private void openAndSend(int timeoutSecs, InetSocketAddress server, String channelID) throws IOException, ValueOutOfRangeException, InterruptedException {
        PaymentChannelClientConnection client = new PaymentChannelClientConnection(
                server, timeoutSecs, appKit.wallet(), myKey, channelSize, channelID);
        // Opening the channel requires talking to the server, so it's asynchronous.
        final CountDownLatch latch = new CountDownLatch(1);
        Futures.addCallback(client.getChannelOpenFuture(), new FutureCallback<PaymentChannelClientConnection>() {
            public void onSuccess(PaymentChannelClientConnection client) {
                // Success! We should be able to try making micropayments now. Try doing it 5 times.
                for (int i = 0; i < rounds; i++) {
                    try {
                        client.incrementPayment(CENT.divide(TEN));
                        Thread.sleep(interval * 60 * 1000);
                    } catch (ValueOutOfRangeException e) {
                    	Log.e("Failed to increment payment by a CENT, remaining value is {}", client.state().getValueRefunded().toString());
                        System.exit(-3);
                    } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                        }
                    Log.e("Successfully sent payment of one CENT, total remaining on channel is now {}", client.state().getValueRefunded().toString());
                }
                if (client.state().getValueRefunded().equals(ZERO)) {
                    // Now tell the server we're done so they should broadcast the final transaction and refund us what's
                    // left. If we never do this then eventually the server will time out and do it anyway and if the
                    // server goes away for longer, then eventually WE will time out and the refund tx will get broadcast
                    // by ourselves.
                	Log.e(TAG, "Closing channel for good");
                    client.close();
                } else {
                    // Just unplug from the server but leave the channel open so it can resume later.
                    client.disconnectWithoutChannelClose();
                }
                latch.countDown();
            }

            public void onFailure(Throwable throwable) {
            	Log.e(TAG,"Failed to open connection", throwable);
                latch.countDown();
            }
        });
        latch.await();
    }

    private void waitForSufficientBalance(BigInteger amount) {
        // Not enough money in the wallet.
        BigInteger amountPlusFee = amount.add(Wallet.SendRequest.DEFAULT_FEE_PER_KB);
        // ESTIMATED because we don't really need to wait for confirmation.
        ListenableFuture<BigInteger> balanceFuture = appKit.wallet().getBalanceFuture(amountPlusFee, Wallet.BalanceType.ESTIMATED);
        if (!balanceFuture.isDone()) {
            Log.e(TAG, "Please send " + Utils.bitcoinValueToFriendlyString(amountPlusFee) +
                    " BTC to " + myKey.toAddress(params));
            Futures.getUnchecked(balanceFuture);
        }
    }
}
    	
    

    