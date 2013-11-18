package com.test.bitcoinj.android.bitcoinj_android;

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
    
    public PaymentChannelClient() {
        channelSize = CENT;
        myKey = new ECKey();
        params = TestNet3Params.get();
    }
    
    public void run(final String host) throws Exception {
    	
    	appKit = new WalletAppKit(params, new File("."), "payment_channel_example_client") {
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
    
    public Wallet displayWallet(){
    	
    	return appKit.wallet();
    	
    }
}