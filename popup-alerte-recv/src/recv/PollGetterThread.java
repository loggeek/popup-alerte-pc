package recv;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class PollGetterThread extends Thread
{
	Socket socket;
	Logger logger;
	
	PollGetterThread(Socket socket, Logger logger)
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
	    	
	    	if (resp.equals("POLL"))
	    	{
	    		logger.log(Level.INFO, "Acknowledged");
	        	writer.println("ACK");
	        	socket.close();
	    	} else
	    	{
	    		logger.log(Level.WARNING, "Didn't acknowledge");
	    		writer.println("NAK");
	    		socket.close();
	    		System.exit(0);
	    	}
	    } catch (IOException ex)
	    {
	        logger.log(Level.WARNING, "Server exception: " + ex.getMessage());
	        ex.printStackTrace();
	        System.exit(0);
	    }
    logger.log(Level.INFO, "End of polling");
	}
}
