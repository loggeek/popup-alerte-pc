package expl;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.*;
import java.time.LocalDateTime;

public class Main
{
	final static int DEFAULT_ACCUMULATE_PORT = 8080;
	final static int DEFAULT_POLLING_PORT = 8081;
	final static int DEFAULT_ALERT_PORT = 8082;
	final static int DEFAULT_STOP_PORT = 8083;
	
	final static long POLLER_DEFAULT_INTERVAL = 3600_000; // In milliseconds
	final static String DEFAULT_ALERT_TEXT = "Alerte";
	final static String DEFAULT_BG_COLOR = "0, 0, 255";
	final static String DEFAULT_TEXT_COLOR = "255, 255, 255";
	
	Logger logger;
	static int accumulatePort, pollingPort, alertPort, stopPort;
	static long pollerInterval;
	static String alertText, bgColor, textColor;
	
    public static void main(String[] argv)
    {
    	Main exploitation = new Main();
    	exploitation.start(argv);
    }
    
    Main()
    {
    	logger = Logger.getLogger("logger");
    	ConsoleHandler ch;
    	FileHandler fh;
        try
        {
        	String logpath = System.getenv("APPDATA") + "/popup-alerte-pc/expl";
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
        	String configFile = "expl-config.properties";
        	
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
	        		pollerInterval = config.getProperty("pollerInterval") != null
	        				? Long.parseLong(config.getProperty("pollerInterval").replace("_", ""))
	        				: POLLER_DEFAULT_INTERVAL;
	        		alertText = config.getProperty("alertText") != null
	        				? config.getProperty("alertText")
	        				: DEFAULT_ALERT_TEXT;
	        		bgColor = config.getProperty("bgColor") != null
	        				? config.getProperty("bgColor")
	        				: DEFAULT_BG_COLOR;
	        		textColor = config.getProperty("textColor") != null
	        				? config.getProperty("textColor")
	        				: DEFAULT_TEXT_COLOR;
	        		
	        		logger.log(Level.INFO, "Config values initialized successfully");
        		} catch (NumberFormatException ex)
        		{
                	logger.log(Level.WARNING, "Invalid config values");
                	
                	accumulatePort = DEFAULT_ACCUMULATE_PORT;
                	pollingPort = DEFAULT_POLLING_PORT;
                	alertPort = DEFAULT_ALERT_PORT;
                	stopPort = DEFAULT_STOP_PORT;
            		pollerInterval = POLLER_DEFAULT_INTERVAL;
            		alertText = DEFAULT_ALERT_TEXT;
            		bgColor = DEFAULT_BG_COLOR;
            		textColor = DEFAULT_TEXT_COLOR;
        		}
        	} else
        	{
        		logger.log(Level.WARNING, "Couldn't fetch configuration");
        		
            	accumulatePort = DEFAULT_ACCUMULATE_PORT;
            	pollingPort = DEFAULT_POLLING_PORT;
            	alertPort = DEFAULT_ALERT_PORT;
            	stopPort = DEFAULT_STOP_PORT;
        		pollerInterval = POLLER_DEFAULT_INTERVAL;
        		alertText = DEFAULT_ALERT_TEXT;
        		bgColor = DEFAULT_BG_COLOR;
        		textColor = DEFAULT_TEXT_COLOR;
        	}
        } catch (IOException ex)
        {
        	logger.log(Level.WARNING, "IOException whilst fetching configuration: " + ex.getMessage());
        	
        	accumulatePort = DEFAULT_ACCUMULATE_PORT;
        	pollingPort = DEFAULT_POLLING_PORT;
        	alertPort = DEFAULT_ALERT_PORT;
        	stopPort = DEFAULT_STOP_PORT;
    		pollerInterval = POLLER_DEFAULT_INTERVAL;
    		alertText = DEFAULT_ALERT_TEXT;
    		bgColor = DEFAULT_BG_COLOR;
    		textColor = DEFAULT_TEXT_COLOR;
        }
    }
    
    void start(String[] argv)
    {
        Accumulator accumulator = new Accumulator(accumulatePort, logger);
        accumulator.start();
        
        Poller poller = new Poller(pollingPort, logger);
        poller.start();
        
    	Window controlWindow = new Window(alertPort, pollingPort, logger);
    	controlWindow.show();
    }
}
