package expl;

import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;


public class Window implements ActionListener
{
	int alertPort, pollingPort;
	private WhistleBlower whistleBlower;
	private PollerRun poller;
	static JLabel receiversList;
	JFrame frame;
	
	static int scroll = 0;
	
	static String receivers;
	Logger logger;
	
	Window(int alertPort, int pollingPort, Logger logger)
    {
		this.alertPort = alertPort;
		this.pollingPort = pollingPort;
		this.logger = logger;
		
        frame = new JFrame("Panneau de contrôle");
		frame.setLayout(null);
		frame.setLocationByPlatform(true);
		frame.getContentPane().setBackground(new java.awt.Color(31, 31, 31));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent)
		    {
		        int respOfClosing = JOptionPane.showConfirmDialog(frame,
	        		"Êtes-vous sûr(e) de vouloir arrêter l'exploitation?",
	        		"Arrêter l'exploitation?", 
	        		JOptionPane.YES_NO_OPTION,
	        		JOptionPane.WARNING_MESSAGE
		        );
		        if(respOfClosing == JOptionPane.YES_OPTION)
		        {
		        	stop();
		        }
		    }
		});
	    
	    JButton closeButton = new JButton("Fermer");
	    closeButton.setBounds(0, 0, 200, 25);
	    closeButton.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
		        int respOfClosing = JOptionPane.showConfirmDialog(frame,
	        		"Êtes-vous sûr(e) de vouloir arrêter l'exploitation?",
	        		"Arrêter l'exploitation?", 
	        		JOptionPane.YES_NO_OPTION,
	        		JOptionPane.WARNING_MESSAGE
		        );
		        if(respOfClosing == JOptionPane.YES_OPTION)
		        {
		        	stop();
		        }
	    	}
	    });
	    
	    JButton alertButton = new JButton("Alerter");  
	    alertButton.setBounds(0, 25, 200, 25);
	    alertButton.addActionListener(this);
	    
	    JButton pollButton = new JButton("Sonder Maintenant");
	    pollButton.setBounds(0, 50, 200, 25);
	    pollButton.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	            poller = new PollerRun(pollingPort, logger);
	            poller.start();
	    	}
	    });
	    
	    JLabel receiversListLabel = new JLabel();
	    receiversListLabel.setBounds(210, 5, 1200, 800);
	    receiversListLabel.setVerticalAlignment(JLabel.NORTH);
	    receiversListLabel.setForeground(new java.awt.Color(255, 255, 255));
	    receiversListLabel.setFont(new Font("Lucida Console", Font.BOLD, 20));
	    receiversListLabel.setText("Receveurs:");
	    
	    receivers = "<html><p color='00ffff'>Aucun receveur est connecté actuellement.</p></html>";
	    
	    receiversList = new JLabel();
	    receiversList.setBounds(210, 50, 1200, 800);
	    receiversList.setVerticalAlignment(JLabel.NORTH);
	    receiversList.setForeground(new java.awt.Color(255, 255, 255));
	    receiversList.setFont(new Font("Lucida Console", Font.PLAIN, 20));
	    receiversList.setText(receivers);
	    
	    JButton scrollUp = new BasicArrowButton(BasicArrowButton.NORTH);
	    scrollUp.setBounds(0, 100, 50, 50);
	    scrollUp.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		scroll--;
	    		setReceiversList();
	    	}
	    });
	    
	    JButton scrollDown = new BasicArrowButton(BasicArrowButton.SOUTH);
	    scrollDown.setBounds(0, 150, 50, 50);
	    scrollDown.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		scroll++;
	    		setReceiversList();
	    	}
	    	
	    });
	    
        frame.add(alertButton);
        frame.add(closeButton);
        frame.add(pollButton);
        frame.add(receiversListLabel);
        frame.add(receiversList);
        frame.add(scrollUp);
        frame.add(scrollDown);
	    
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
	
	public void show()
	{
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
        whistleBlower = new WhistleBlower(alertPort, logger);
        whistleBlower.start();
	}
	
	public void stop()
	{
    	Stopper stopper = new Stopper(Main.stopPort, logger);
    	stopper.stop();
    	System.exit(0);
	}
	
	public static void setReceiversList()
	{
		ArrayList<String> receiversArray = new ArrayList<String>();
		
		Collections.addAll(receiversArray, receivers.replace("<html>", "").replace("</html>", "")
				.split("</p>", -1));
		receiversArray.remove("");
		
		if (receiversArray.size() < 36)
		{
			scroll = 0;
			receiversList.setText(receivers);
		} else
		{
			if (scroll < 0)
			{
				scroll = receiversArray.size() - 36;
			}
			if (scroll + 36 > receiversArray.size())
			{
				scroll = 0;
			}
			String receiversDisplay = "<html>";
			for (int index = scroll; index < 36 + scroll; index++)
			{
				receiversDisplay += receiversArray.get(index);
				receiversDisplay += "</p>";
			}
			receiversDisplay += "</html>";
			receiversList.setText(receiversDisplay);
		}
	}
	
	public static void addReceiver(String ip, String name)
	{
		if (receivers.equals("<html><p color='00ffff'>Aucun receveur est connecté actuellement.</p></html>"))
		{
			receivers = "<html></html>";
		}
		String text = receivers.replace("</html>", "");
		
		text += "<p color='#00ff00'>" + ip + " / " + name + "</p>";
		
		receivers = text + "</html>";
		setReceiversList();
	}
	
	public static void removeReceiver(String ip, String name)
	{
		receivers = receivers.replace("<p color='#ff0000'>" + ip + " / " + name + "</p>", "");
		if (receivers.equals("<html></html>"))
		{
			receivers = "<html><p color='00ffff'>Aucun receveur est connecté actuellement.</p></html>";
		}
		setReceiversList();
	}
	
	public static void setReceiversRed()
	{
		receivers = receivers.replace("<p color='#00ff00'>", "<p color='#ff0000'>");
		setReceiversList();
	}
	
	public static void validateReceiver(String ip, String name)
	{
		receivers = receivers.replace(
				"<p color='#ff0000'>" + ip + " / " + name + "</p>", 
				"<p color='#00ff00'>" + ip + " / " + name + "</p>");
		setReceiversList();
	}
	
	public static void semiValidateReceiver(String ip, String name)
	{
		receivers = receivers.replace(
				"<p color='#ff0000'>" + ip + " / " + name + "</p>", 
				"<p color='#ffff00'>" + ip + " / " + name + "</p>");
		setReceiversList();
	}
	
	public static void fullyValidateReceiver(String ip, String name)
	{
		receivers = receivers.replace(
				"<p color='#ffff00'>" + ip + " / " + name + "</p>", 
				"<p color='#00ff00'>" + ip + " / " + name + "</p>");
		setReceiversList();
	}
}
