package expl;

import java.util.logging.*;
import java.util.concurrent.ConcurrentHashMap;

public class WhistleBlower extends Thread
{
	String message;
	int port;
	Logger logger;
	
	private ConcurrentHashMap<String,String> receivers = new ConcurrentHashMap<String,String>();
	
	WhistleBlower(String message, int port, Logger logger)
	{
		this.message = message;
		this.port = port;
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
		logger.log(Level.INFO, "Alerting Start");
		receivers = Poller.receivers;
		Window.setReceiversRed();
		
		for (String receiver: receivers.keySet())
        {
        	logger.log(Level.INFO, "Alerting: " + receiver + "/" + receivers.get(receiver));
        	
        	WhistleBlowerThread whistleBlowerThread =
        			new WhistleBlowerThread(message, receiver, receivers.get(receiver), port, logger);
        	whistleBlowerThread.start();
        }
	}
}
