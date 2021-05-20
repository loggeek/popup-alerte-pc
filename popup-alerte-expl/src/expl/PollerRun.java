package expl;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

public class PollerRun extends Thread
{
	int port;
	Logger logger;
	ConcurrentHashMap<String,String> receivers = new ConcurrentHashMap<String,String>();
	
	PollerRun(int port, Logger logger)
	{
		this.port = port;
		this.logger = logger;
		this.receivers = Poller.receivers;
	}
	
	@Override
	public void run()
	{
		logger.log(Level.INFO, "Polling start");
		Window.setReceiversRed();
		
		for (String receiver: receivers.keySet())
        {
        	logger.log(Level.INFO, "Polling: " + receiver + "/" + receivers.get(receiver));
        	
        	try (Socket socket = new Socket(receiver, port))
            {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                
            	String msg = "POLL";
                writer.println(msg); // Sends input to Server
                
                String resp = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine(); // Gets server response
                
            	logger.log(Level.INFO, "PollGetter Response: " + resp); // Displays server response
            	if (resp.equals("ACK"))
            	{
            		logger.log(Level.INFO, "Poll succeeded; keeping receiver in database");
            		Window.validateReceiver(receiver, receivers.get(receiver));
            	} else
            	{
            		logger.log(Level.WARNING, "Invalid Response; Poll failed");
            		Window.removeReceiver(receiver, receivers.get(receiver));
            		Poller.receivers.remove(receiver);
            	}
                socket.close();
                
            } catch (UnknownHostException ex)
            {
        		logger.log(Level.WARNING, "Unknown host; Poll failed");
        		Window.removeReceiver(receiver, receivers.get(receiver));
        		Poller.receivers.remove(receiver);
            } catch (IOException ex)
            {
            	logger.log(Level.WARNING, "I/O error: " + ex.getMessage());
            	Window.removeReceiver(receiver, receivers.get(receiver));
            	Poller.receivers.remove(receiver);
            }
        }
        logger.log(Level.INFO, "Done with polling");
	}
}
