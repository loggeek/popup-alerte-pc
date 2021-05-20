package recv;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class AlerterThread extends Thread
{
	Socket socket;
	Logger logger;
	
	AlerterThread(Socket socket, Logger logger)
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
	    	
	    	if (resp.split("__", 2)[0].equals("ALERT"))
	    	{
	    		logger.log(Level.INFO, "Alerting");
	        	writer.println("ALERTING");
	        	
	            logger.log(Level.INFO, "Creating window");
	            
	        	Window alertWindow = new Window(resp.split("__", 2)[1]);
	        	alertWindow.show();
	        	
	        	writer.println("DISPLAYED");
	        	logger.log(Level.INFO, "Window displayed");
	        	socket.close();
	    	} else
	    	{
	    		logger.log(Level.WARNING, "Don't alert");
	    		writer.println("NO THANKS");
	    		socket.close();
	    		System.exit(0);
	    	}
	    } catch (IOException ex)
	    {
	        logger.log(Level.WARNING, "Server exception: " + ex.getMessage());
	        ex.printStackTrace();
	        System.exit(0);
	    }
	}
}
