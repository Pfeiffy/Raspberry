import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class CopterSteuerung {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CopterSteuerung window = new CopterSteuerung();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public CopterSteuerung() throws UnknownHostException, IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private void initialize() throws UnknownHostException, IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 682, 388);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		Socket s = new Socket("localhost", 9090);
		final PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		final BufferedReader input = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
		

		
		JButton btnOben = new JButton("/\\");
		btnOben.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("$1");
				try {
					System.out.println(input.readLine());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnOben.setBounds(100, 71, 89, 56);
		frame.getContentPane().add(btnOben);
		
		JButton btnUnten = new JButton("V");
		btnUnten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("$2");
			}
		});
		btnUnten.setBounds(100, 181, 89, 56);
		frame.getContentPane().add(btnUnten);
		
		JButton btnLinks = new JButton("<");
		btnLinks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				out.println("$3");
			}
		});
		btnLinks.setBounds(10, 124, 89, 56);
		frame.getContentPane().add(btnLinks);
		
		JButton btnRechts = new JButton(">");
		btnRechts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("$4");
			}
		});
		btnRechts.setBounds(186, 124, 89, 56);
		frame.getContentPane().add(btnRechts);
	}
}
