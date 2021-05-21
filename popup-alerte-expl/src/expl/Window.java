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
	static JTextArea alertMessage, alertBgColor, alertTextColor;
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
	    closeButton.setFont(new Font("Lucida Console", Font.PLAIN, 14));
	    
	    JButton alertButton = new JButton("Alerter");  
	    alertButton.setBounds(0, 25, 200, 25);
	    alertButton.addActionListener(this);
	    alertButton.setFont(new Font("Lucida Console", Font.PLAIN, 14));
	    
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
	    pollButton.setFont(new Font("Lucida Console", Font.PLAIN, 14));
	    
	    JLabel receiversListLabel = new JLabel();
	    receiversListLabel.setBounds(210, 5, 1200, 45);
	    receiversListLabel.setVerticalAlignment(JLabel.NORTH);
	    receiversListLabel.setForeground(new java.awt.Color(255, 255, 255));
	    receiversListLabel.setFont(new Font("Lucida Console", Font.BOLD, 20));
	    receiversListLabel.setText("Receveurs:");
	    
	    receivers = "<html><p color='00ffff'>Aucun receveur est connecté actuellement.</p></html>";
	    
	    receiversList = new JLabel();
	    receiversList.setBounds(210, 50, 1200, 700);
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
	    
	    JLabel alertMessageLabel = new JLabel();
	    alertMessageLabel.setBounds(5, 225, 195, 25);
	    alertMessageLabel.setVerticalAlignment(JLabel.NORTH);
	    alertMessageLabel.setForeground(new java.awt.Color(255, 255, 255));
	    alertMessageLabel.setFont(new Font("Lucida Console", Font.BOLD, 20));
	    alertMessageLabel.setText("Message:");
	    
	    alertMessage = new JTextArea(Main.alertText);
	    alertMessage.setBounds(0, 250, 200, 250);
	    alertMessage.setFont(new Font("Lucida Console", Font.PLAIN, 16));
	    
	    JLabel alertBgColorLabel = new JLabel();
	    alertBgColorLabel.setBounds(5, 505, 195, 25);
	    alertBgColorLabel.setVerticalAlignment(JLabel.NORTH);
	    alertBgColorLabel.setForeground(new java.awt.Color(255, 255, 255));
	    alertBgColorLabel.setFont(new Font("Lucida Console", Font.BOLD, 20));
	    alertBgColorLabel.setText("Arrière-plan:");
	    
	    alertBgColor = new JTextArea(Main.bgColor);
	    alertBgColor.setBounds(0, 530, 200, 25);
	    alertBgColor.setFont(new Font("Lucida Console", Font.PLAIN, 16));
	    
	    JLabel alertTextColorLabel = new JLabel();
	    alertTextColorLabel.setBounds(5, 560, 195, 25);
	    alertTextColorLabel.setVerticalAlignment(JLabel.NORTH);
	    alertTextColorLabel.setForeground(new java.awt.Color(255, 255, 255));
	    alertTextColorLabel.setFont(new Font("Lucida Console", Font.BOLD, 20));
	    alertTextColorLabel.setText("Texte:");
	    
	    alertTextColor = new JTextArea(Main.textColor);
	    alertTextColor.setBounds(0, 585, 200, 25);
	    alertTextColor.setFont(new Font("Lucida Console", Font.PLAIN, 16));
	    
        frame.add(alertButton);
        frame.add(closeButton);
        frame.add(pollButton);
        frame.add(receiversListLabel);
        frame.add(receiversList);
        frame.add(scrollUp);
        frame.add(scrollDown);
        frame.add(alertMessageLabel);
        frame.add(alertMessage);
        frame.add(alertBgColorLabel);
        frame.add(alertBgColor);
        frame.add(alertTextColorLabel);
        frame.add(alertTextColor);
	    
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
	
	public void show()
	{
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object[] options =
		{
			"Texte personnalisé",
            "Texte n°1",
            "Texte n°2",
            "Texte n°3",
            "Annuler"
        };
		
        int alertOption = JOptionPane.showOptionDialog(frame,
    		"Quel texte d'alerte?",
    		"Quelle alerte?", 1,
    		JOptionPane.INFORMATION_MESSAGE,
    		null, options, null
        );
		
        switch (alertOption)
        {
        	case 0:
                whistleBlower = new WhistleBlower(alertMessage.getText(), alertPort, logger);
                whistleBlower.start();
                break;
        	case 1:
                whistleBlower = new WhistleBlower(Main.presetText1, alertPort, logger);
                whistleBlower.start();
                break;
        	case 2:
                whistleBlower = new WhistleBlower(Main.presetText2, alertPort, logger);
                whistleBlower.start();
                break;
        	case 3:
                whistleBlower = new WhistleBlower(Main.presetText3, alertPort, logger);
                whistleBlower.start();
                break;
        	case 4:
        		break;
        }
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
