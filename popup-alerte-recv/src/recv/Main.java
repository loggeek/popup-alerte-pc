package recv;

import java.util.Properties;
import java.util.logging.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.time.LocalDateTime;

public class Main
{
	final static int DEFAULT_ACCUMULATE_PORT = 8080;
	final static int DEFAULT_POLLING_PORT = 8081;
	final static int DEFAULT_ALERT_PORT = 8082;
	final static int DEFAULT_STOP_PORT = 8083;
	
	final static String DEFAULT_EXPL_HOSTNAME = "localhost";
	
	Logger logger;
	String hostname;
	String ip;
	String name;
	static int accumulatePort, pollingPort, alertPort, stopPort;
	
    public static void main(String[] argv)
    {
    	Main receiver = new Main();
    	receiver.start(argv);
    }
    
    Main()
    {
    	logger = Logger.getLogger("logger");
    	ConsoleHandler ch;
    	FileHandler fh;
        try
        {
        	String logpath = System.getenv("APPDATA") + "/popup-alerte-pc/recv";
        	Files.createDirectories(Paths.get(logpath));
        	
        	logger.setUseParentHandlers(false);
            ch = new ConsoleHandler();
            ch.setFormatter(new LogFormatter());
            logger.addHandler(ch);
            fh = new FileHandler(logpath + "/log_" +
            		LocalDateTime.now().toString().replace(":", "-")
            		.replace("T", "_").split("\\.")[0] + ".log");
            fh.setFormatter(new LogFormatter());
            logger.addHandler(fh);
        } catch (SecurityException ex)
        {
        	logger.log(Level.WARNING, "Security exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex)
        {
        	logger.log(Level.WARNING, "Couldn't create log file: " + ex.getMessage());
            ex.printStackTrace();
        }
        try
        {
        	Properties config = new Properties();
        	String configFile = "recv-config.properties";
        	
        	InputStream is = getClass().getClassLoader().getResourceAsStream(configFile);
        	if (is != null)
        	{
        		logger.log(Level.INFO, "Fetching configuration");
        		config.load(is);
        		
        		try
        		{
        			accumulatePort = config.getProperty("accumulatePort") != null
	        				? Integer.parseInt(config.getProperty("accumulatePort").replace("_", ""))
	        				: DEFAULT_ACCUMULATE_PORT;
        			pollingPort = config.getProperty("pollingPort") != null
	        				? Integer.parseInt(config.getProperty("pollingPort").replace("_", ""))
	        				: DEFAULT_POLLING_PORT;
	        		alertPort = config.getProperty("alertPort") != null
	        				? Integer.parseInt(config.getProperty("alertPort").replace("_", ""))
	        				: DEFAULT_ALERT_PORT;
	        		stopPort = config.getProperty("stopPort") != null
	        				? Integer.parseInt(config.getProperty("stopPort").replace("_", ""))
	        				: DEFAULT_STOP_PORT;
	        		hostname = config.getProperty("hostname") != null
	        				? config.getProperty("hostname")
	        				: DEFAULT_EXPL_HOSTNAME;
	        		
	        		logger.log(Level.INFO, "Config values initialized successfully");
	        		
        		} catch (NumberFormatException ex)
        		{
                	logger.log(Level.WARNING, "Invalid config values");
                	
                	accumulatePort = DEFAULT_ACCUMULATE_PORT;
                	pollingPort = DEFAULT_POLLING_PORT;
                	alertPort = DEFAULT_ALERT_PORT;
                	stopPort = DEFAULT_STOP_PORT;
            		hostname = DEFAULT_EXPL_HOSTNAME;
        		}
        	} else
        	{
        		logger.log(Level.WARNING, "Couldn't fetch configuration");
        		
            	accumulatePort = DEFAULT_ACCUMULATE_PORT;
            	pollingPort = DEFAULT_POLLING_PORT;
            	alertPort = DEFAULT_ALERT_PORT;
            	stopPort = DEFAULT_STOP_PORT;
        		hostname = DEFAULT_EXPL_HOSTNAME;
        	}
        } catch (IOException ex)
        {
        	logger.log(Level.WARNING, "IOException whilst fetching configuration: " + ex.getMessage());
        	
        	accumulatePort = DEFAULT_ACCUMULATE_PORT;
        	pollingPort = DEFAULT_POLLING_PORT;
        	alertPort = DEFAULT_ALERT_PORT;
        	stopPort = DEFAULT_STOP_PORT;
    		hostname = DEFAULT_EXPL_HOSTNAME;
        }
    }
    
    
    void start(String[] argv)
    {
        try
        {
        	name = InetAddress.getLocalHost().getHostName();
        	ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex)
        {
        	logger.log(Level.SEVERE, "Unknown host: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(-1);
        }
        Initializer initializer = new Initializer(accumulatePort, hostname, ip, name, logger);
        initializer.start();
        
        PollGetter pollGetter = new PollGetter(pollingPort, hostname, logger);
        pollGetter.start();
        
        Alerter alerter = new Alerter(alertPort, hostname, logger);
        alerter.start();
        
        Killer killer = new Killer(stopPort, hostname, logger);
        killer.start();
    }
}
