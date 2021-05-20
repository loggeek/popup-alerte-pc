package expl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

public class Poller extends Thread
{
	long interval;
	double percentage;
	int port;
	
	Logger logger;
	public static ConcurrentHashMap<String,String> receivers = new ConcurrentHashMap<String,String>();
	
	Poller(int port, Logger logger)
	{
		this.port = port;
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
            try
        	{
            	Thread.sleep(Main.pollerInterval);
            }
            catch(InterruptedException ex)
        	{
            	Thread.currentThread().interrupt();
            }
            PollerRun poller = new PollerRun(port, logger);
            poller.start();
		}
	}
}
