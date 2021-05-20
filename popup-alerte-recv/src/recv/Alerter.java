package recv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;

public class Alerter extends Thread
{
	int port;
	String hostname;
	Logger logger;
	
	Alerter (int port, String hostname, Logger logger)
	{
		this.port = port;
		this.hostname = hostname;
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            logger.log(Level.INFO, "Alerter Server is listening on port " + port);
            while (true)
            {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "Alert gotten");
                new AlerterThread(socket, logger).start();
            }
        } catch (IOException ex)
        {
            logger.log(Level.SEVERE, "Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
	}
}
