package recv;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class KillerThread extends Thread
{
	Socket socket;
	Logger logger;
	
	KillerThread(Socket socket, Logger logger)
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
	        
	    	resp = reader.readLine(); // Get Poller Message
	    	logger.log(Level.INFO, "Exploitation message: " + resp);
	    	
	    	if (resp.equals("STOP"))
	    	{
	    		logger.log(Level.INFO, "Acknowledged");
	        	writer.println("OK");
	        	socket.close();
	    	} else
	    	{
	    		logger.log(Level.WARNING, "Didn't acknowledge");
	    		writer.println("NOT OK");
	    		socket.close();
	    		System.exit(0);
	    	}
	    } catch (IOException ex)
	    {
	        logger.log(Level.WARNING, "Server exception: " + ex.getMessage());
	        ex.printStackTrace();
	        System.exit(0);
	    }
    logger.log(Level.INFO, "End of stopping");
    System.exit(0);
	}
}
