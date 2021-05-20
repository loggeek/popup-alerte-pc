package recv;

import java.awt.event.*;
import java.awt.Font;

import javax.swing.*;


public class Window
{
	JFrame frame;
	
	public static void main(String[] argv) throws ArrayIndexOutOfBoundsException
	{
		Window alertWindow = new Window(argv[0]);
		alertWindow.show();
	}
	
	Window (String text)
    {
		int textLength = text.split("<br />").length;
		
        frame = new JFrame("Alerte");
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationByPlatform(true);
        frame.setAlwaysOnTop(true);
			// CAUTION: setAlwaysOnTop=>true could lead to a soft-lock if the close button is not visible!
		frame.getContentPane().setBackground(new java.awt.Color(0, 0, 255));
		
	    JButton button = new JButton("J'ai compris");  
	    button.setBounds(200, textLength * 40 + 40, 400, 40);
	    button.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		frame.dispose();
	    	}
	    });
	    button.setFont(new Font("Lucida Console", Font.PLAIN, 28));
	    
	    JLabel label = new JLabel (text);
	    label.setBounds(170, 5, 1200, 800);
	    label.setVerticalAlignment(JLabel.NORTH);
	    label.setForeground(new java.awt.Color(255, 255, 255));
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
