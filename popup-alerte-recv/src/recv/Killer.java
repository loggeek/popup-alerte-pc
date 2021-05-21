package recv;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Killer extends Thread
{
	int port;
	Logger logger;
	
	Killer(int port, Logger logger)
	{
		this.port = port;
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            logger.log(Level.INFO, "Killer Server is listening on port " + port);
            while (true)
            {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "Call gotten");
                new KillerThread(socket, logger).start();
            }
        } catch (IOException ex)
        {
            logger.log(Level.SEVERE, "Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
	}
}
