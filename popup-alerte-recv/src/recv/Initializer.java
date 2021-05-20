package recv;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Initializer extends Thread
{
	int port;
	String hostname;
	String ip;
	String name;
	Logger logger;
	
	Initializer (int port, String hostname, String ip, String name, Logger logger)
	{
		this.port = port;
		this.hostname = hostname;
		this.ip = ip;
		this.name = name;
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
        try (Socket socket = new Socket(hostname, port))
        {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            // Response Format: ip__name
        	String msg = ip + "__" + name;
            writer.println(msg); // Sends input to Server
            
            String resp = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine(); // Gets server response
            
        	logger.log(Level.INFO, "Exploitation response: " + resp); // Displays server response
        	if (resp.equals("VALID"))
        	{
        		logger.log(Level.INFO, "Process validated");
        	} else
        	{
        		logger.log(Level.SEVERE, "Fatal error: Initialization failed");
        		System.exit(-1);
        	}
            
            logger.log(Level.INFO, "End of initialization process");
            socket.close();
            
        } catch (UnknownHostException ex)
        {
        	logger.log(Level.SEVERE, "Server not found: " + ex.getMessage());
        	System.exit(-1);
        } catch (IOException ex)
        {
        	logger.log(Level.SEVERE, "I/O error: " + ex.getMessage());
        	System.exit(-1);
        }
    }
}
