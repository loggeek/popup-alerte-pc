package recv;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class PollGetter extends Thread
{
	int port;
	Logger logger;
	
	PollGetter (int port, Logger logger)
	{
		this.port = port;
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            logger.log(Level.INFO, "PollGetter Server is listening on port " + port);
            while (true)
            {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "Call gotten");
                new PollGetterThread(socket, logger).start();
            }
        } catch (IOException ex)
        {
            logger.log(Level.SEVERE, "Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
	}
}
