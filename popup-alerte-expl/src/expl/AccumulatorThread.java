package expl;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class AccumulatorThread extends Thread
{
	Socket socket;
	Logger logger;
	
    public AccumulatorThread(Socket socket, Logger logger)
    {
        this.socket = socket;
        this.logger = logger;
    }
    
    @Override
    public void run()
    {
        try
        {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            String resp;
            String[] respArray;
            String ip;
            String name;
            
        	resp = reader.readLine(); // Gets Client's IP and name
        	logger.log(Level.INFO, "Receiver message: " + resp);
        	
        	// Response Format: ip__name
        	try
        	{
        		respArray = resp.split("__", 2);
            	ip = respArray[0];
            	name = respArray[1];
            	
            	writer.println("VALID");
            	if (!Poller.receivers.containsKey(ip))
            	{
            		Poller.receivers.put(ip, name);
            		Window.addReceiver(ip, name);
            	}
        	} catch (ArrayIndexOutOfBoundsException ex)
        	{
        		logger.log(Level.WARNING, "Invalid info");
        		writer.println("INVALID");
        	} finally
        	{
        		socket.close();
        	}
        } catch (IOException ex)
        {
            logger.log(Level.WARNING, "Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        logger.log(Level.INFO, "End of initialization process");
    }
    
    boolean isInteger (String s)
    {
    	try
    	{
    		Integer.parseInt(s);
    	}
    	catch(NumberFormatException ex)
    	{
    		return false;
    	}
    	return true;
    }
}
