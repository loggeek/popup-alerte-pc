package expl;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

public class Stopper
{
	int port;
	Logger logger;
	ConcurrentHashMap<String,String> receivers = new ConcurrentHashMap<String,String>();
	
	Stopper(int port, Logger logger)
	{
		this.port = port;
		this.logger = logger;
		this.receivers = Poller.receivers;
	}
	
	public void stop()
	{
		logger.log(Level.INFO, "Stopping start");
		
		for (String receiver: receivers.keySet())
        {
        	logger.log(Level.INFO, "Stopping: " + receiver + "/" + receivers.get(receiver));
        	
        	try (Socket socket = new Socket(receiver, port))
            {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                
            	String msg = "STOP";
                writer.println(msg); // Sends input to Server
                
                String resp = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine(); // Gets server response
                
            	logger.log(Level.INFO, "PollGetter Response: " + resp); // Displays server response
            	
            	if (resp.equals("OK"))
            	{
            		logger.log(Level.INFO, "Stopped");
            	} else
            	{
            		logger.log(Level.WARNING, "Stopping failed");
            	}
                socket.close();
                
            } catch (UnknownHostException ex)
            {
        		logger.log(Level.WARNING, "Unknown host; Stopping failed");
            } catch (IOException ex)
            {
            	logger.log(Level.WARNING, "I/O error: " + ex.getMessage());
            }
        }
        logger.log(Level.INFO, "Done with stopping");
        
        System.exit(0);
	}
}
