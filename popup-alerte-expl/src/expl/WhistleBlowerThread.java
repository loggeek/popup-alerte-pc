package expl;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class WhistleBlowerThread extends Thread
{
	String ip, name;
	int port;
	Logger logger;
	
	WhistleBlowerThread(String ip, String name, int port, Logger logger)
	{
		this.ip = ip;
		this.name = name;
		this.port = port;
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
		try (Socket socket = new Socket(ip, port))
        {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
        	String text = "<html>"
    	    		+ Window.alertMessage.getText().replace("<", "&lt;").replace(">", "&gt;").replace("\\n", "<br />").replace("\n", "<br />")
    	    		+ "</html>";
            
        	String msg = "ALERT__" + text;
            writer.println(msg); // Sends input to Server
            
            String resp = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine(); // Gets server response
            
        	logger.log(Level.INFO, "Alerter Response: " + resp); // Displays server response
        	if (resp.equals("ALERTING"))
        	{
        		logger.log(Level.INFO, "Alert succeeded");
        		Window.semiValidateReceiver(ip, name);
        		
                resp = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                logger.log(Level.INFO, "Alerter Response: " + resp);
            	if (resp.equals("DISPLAYED"))
            	{
            		logger.log(Level.INFO, "Alerter displayed window; keeping receiver in database");
            		Window.fullyValidateReceiver(ip, name);
            	} else
            	{
            		logger.log(Level.WARNING, "Unexpected response from alerter");
            		Window.removeReceiver(ip, name);
            		Poller.receivers.remove(ip);
            	}
        	} else
        	{
        		logger.log(Level.WARNING, "Invalid Response; alert failed");
        		Window.removeReceiver(ip, name);
        		Poller.receivers.remove(ip);
        	}
            socket.close();
            
        } catch (UnknownHostException ex)
        {
    		logger.log(Level.WARNING, "Unknown host; alert failed");
    		Window.removeReceiver(ip, name);
    		Poller.receivers.remove(ip);
        } catch (IOException ex)
        {
        	logger.log(Level.WARNING, "I/O error: " + ex.getMessage());
    		Window.removeReceiver(ip, name);
    		Poller.receivers.remove(ip);
        }
	}
}
