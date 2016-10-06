package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Fenster {

	public JFrame frame;
	public JTextField textField;
	static JLabel label1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenster window = new Fenster();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Fenster() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnStartbutton = new JButton("StartButton");
		btnStartbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			 label1.setText(textField.getText());
			}
		});
		btnStartbutton.setBounds(10, 30, 126, 23);
		frame.getContentPane().add(btnStartbutton);
		
		textField = new JTextField();
		textField.setBounds(175, 31, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		label1 = new JLabel("New label");
		label1.setBounds(10, 101, 46, 14);
		frame.getContentPane().add(label1);
	}
}
