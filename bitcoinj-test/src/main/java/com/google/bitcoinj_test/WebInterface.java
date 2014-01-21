package com.google.bitcoinj_test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.protocols.channels.PaymentChannelServerState;

public class WebInterface implements Runnable {
	final WebInterfaceServer srv;

	public WebInterface(ExamplePaymentChannelServer channelServer){
		this.srv = new WebInterfaceServer();
		// setting aliases, for an optional file servlet
		Acme.Serve.Serve.PathTreeDictionary aliases = new Acme.Serve.Serve.PathTreeDictionary();
		aliases.put("/", new java.io.File("src/main/resources"));
		//  note cast name will depend on the class name, since it is anonymous class
		srv.setMappingTable(aliases);
		// setting properties for the server, and exchangeable Acceptors
		java.util.Properties properties = new java.util.Properties();
		properties.put("port", 8000);
		properties.setProperty(Acme.Serve.Serve.ARG_NOHUP, "nohup");
		srv.arguments = properties;
		srv.addDefaultServlets(null); // optional file servlet
		srv.addServlet("/json", new JsonServlet(channelServer)); // optional
		// the pattern above is exact match, use /myservlet/* for mapping any path startting with /myservlet (Since 1.93)
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					srv.notifyStop();
				} catch (IOException e) {
					e.printStackTrace();
				}
				srv.destroyAllServlets();
			}
		}));
	}

	public void run() {
		try {
			srv.init();
			srv.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class WebInterfaceServer extends Acme.Serve.Serve {
		private static final long serialVersionUID = -8538555671402762460L;

		// Overriding method for public access
		public void setMappingTable(PathTreeDictionary mappingtable) { 
			super.setMappingTable(mappingtable);
		}
		
		// add the method below when .war deployment is needed
		public void addWarDeployer(String deployerFactory, String throttles) {
			super.addWarDeployer(deployerFactory, throttles);
		}
	};

	public class JsonServlet extends HttpServlet {
		private static final long serialVersionUID = -7116967784682882970L;
		private final ExamplePaymentChannelServer channelServer;
		private final ObjectMapper objectMapper = new ObjectMapper();

		public JsonServlet(ExamplePaymentChannelServer channelServer) {
			this.channelServer = channelServer;
			this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		}

		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// Set response content type
			response.setContentType("application/json");

			// Build the wallet map:
			Map<String, Object> walletMap = new HashMap<String, Object>();
			Wallet wallet = this.channelServer.getWallet();
			walletMap.put("balance", Utils.bitcoinValueToFriendlyString(wallet.getBalance()));
			
			List<Map<String,Object>> transactions = new LinkedList<Map<String,Object>>();
			walletMap.put("transactions", transactions);
			for(Transaction t : wallet.getTransactions(false)) {
				Map<String, Object> transaction = new HashMap<String, Object>();
				transaction.put("hash", t.getHashAsString());
				transaction.put("value", Utils.bitcoinValueToFriendlyString(t.getValueSentToMe(wallet)));
				transactions.add(transaction);
			}
			
			List<Map<String, Object>> states = new LinkedList<Map<String,Object>>();
			for(Sha256Hash key : channelServer.getChannelStates().keySet()){
				PaymentChannelServerState channelState = channelServer.getChannelStates().get(key);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", key.toString());
				m.put("status", channelState.getState().name());
				m.put("paid", Utils.bitcoinValueToFriendlyString(channelState.getBestValueToMe()));
				long diff = channelState.getRefundTransactionUnlockTime() - System.currentTimeMillis()/1000;
				m.put("timeRemaining", (int)Math.floor(diff/3600) + ":" + (int)Math.floor((diff % 3600) / 60) + ":" + (diff % 60));
				states.add(m);
			}
			
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("wallet", walletMap);
			m.put("channels", states);

			response.getWriter().print(this.objectMapper.writeValueAsString(m));
		}
	}

}
