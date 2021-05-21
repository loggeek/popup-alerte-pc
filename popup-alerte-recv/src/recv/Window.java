package recv;

import java.awt.event.*;
import java.awt.Font;
import java.util.logging.*;

import javax.swing.*;


public class Window
{
	JFrame frame;
	
	Window (String text, String bgColor, String textColor, Logger logger)
    {
		int textLength = text.split("<br />").length;
		
        frame = new JFrame("Alerte");
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationByPlatform(true);
        frame.setAlwaysOnTop(true);
			// CAUTION: setAlwaysOnTop=>true could lead to a soft-lock if the close button is not visible!
        try
        {
			frame.getContentPane().setBackground(new java.awt.Color(
					Integer.parseInt(bgColor.split(",")[0]),
					Integer.parseInt(bgColor.split(",")[1]),
					Integer.parseInt(bgColor.split(",")[2])
				));
        } catch (Exception ex)
        {
        	ex.printStackTrace();
        	logger.log(Level.WARNING, "Invalid Background Color value given; default value used");
			frame.getContentPane().setBackground(new java.awt.Color(
					Integer.parseInt(Main.bgColor.split(",")[0]),
					Integer.parseInt(Main.bgColor.split(",")[1]),
					Integer.parseInt(Main.bgColor.split(",")[2])
				));
        }
		
	    JButton button = new JButton("J'ai compris");  
	    button.setBounds(500, 500, 300, 60);
	    button.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		frame.dispose();
	    	}
	    });
	    button.setFont(new Font("Lucida Console", Font.PLAIN, 36));
	    
	    JLabel label = new JLabel (text);
	    label.setBounds(170, 5, 1200, 800);
	    label.setVerticalAlignment(JLabel.NORTH);
	    try
	    {
	    label.setForeground(new java.awt.Color(
				Integer.parseInt(textColor.split(",")[0]),
				Integer.parseInt(textColor.split(",")[1]),
				Integer.parseInt(textColor.split(",")[2])
			));
	    } catch (Exception ex)
	    {
	    	logger.log(Level.WARNING, "Invalid Text Color value given; default value used");
			frame.getContentPane().setBackground(new java.awt.Color(
					Integer.parseInt(Main.textColor.split(",")[0]),
					Integer.parseInt(Main.textColor.split(",")[1]),
					Integer.parseInt(Main.textColor.split(",")[2])
				));
	    }
	    label.setFont(new Font("Lucida Console", Font.BOLD, 36));
	    
	    JLabel exclmark = new JLabel ("!");
	    exclmark.setBounds(25, -25, 300, 800);
	    exclmark.setVerticalAlignment(JLabel.NORTH);
	    exclmark.setForeground(new java.awt.Color(255, 0, 0));
	    exclmark.setFont(new Font("Times New Roman", Font.BOLD, 300));
	    
        frame.add(button);
	    frame.add(label);
	    frame.add(exclmark);
	    
        frame.setUndecorated(true);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
	
	public void show()
	{
		frame.setVisible(true);
	}
}
