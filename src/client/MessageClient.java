package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

public class MessageClient {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private ArrayList feedMessages;
	
	private JFrame frame;
	private Container contentPane;
	private JPanel postPanel;
	private JTextArea messageField;
	private JPanel feedPanel;
	private JTextArea feed;
	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	private final Font h2 = new Font("Avenir", Font.PLAIN, 20);

	public MessageClient() {
		frame = new JFrame();
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		initTopLabel();
		initPostPanel();
		initFeedPanel();
		
		frame.setSize(400, 600);
		frame.setVisible(true);
	}
	
	private void initTopLabel() {
		// welcome label
		JLabel welcomeLabel = new JLabel("Welcome to Messager!");
		welcomeLabel.setFont(new Font("Archer", Font.BOLD, 30));
		welcomeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		contentPane.add(welcomeLabel);		
	}
	
	private void initPostPanel() {
		postPanel = new JPanel();
		postPanel.setMaximumSize(new Dimension(400, 125));
		postPanel.setLayout(new BorderLayout());
		
		// welcome
		JLabel welcomeLabel = new JLabel("Welcome to Messager!");
		welcomeLabel.setFont(new Font("Archer", Font.BOLD, 30));
		postPanel.add(welcomeLabel);
		
		// "write a message"
		JLabel instr = new JLabel("Write a message!");
		instr.setFont(h2);
		postPanel.add(instr, BorderLayout.NORTH);
		
		// field to enter message
		messageField = new JTextArea();
		messageField.setEditable(true);
		messageField.setFont(globalFont);
		postPanel.add(messageField, BorderLayout.CENTER);
		
		// post button
		JButton postButton = new JButton("POST");
		postButton.setFont(globalFont);
		postButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				out.println("POST_" + messageField.getText());
			}
		});
		postPanel.add(postButton, BorderLayout.SOUTH);
		postPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
		contentPane.add(postPanel);
	}
	
	private void initFeedPanel() {
		feedPanel = new JPanel();
		feedPanel.setMaximumSize(new Dimension(400, 400));
		feedPanel.setLayout(new BorderLayout());
		feedPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 0, 20));
		
		// Label
		JLabel feedLabel = new JLabel("Posts from your Feed");
		feedLabel.setFont(h2);
		feedPanel.add(feedLabel, BorderLayout.NORTH);
		
		// JPanel for Feed
		feed = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(feed);
		scrollpane.setBorder(null);
		feedPanel.add(scrollpane, BorderLayout.CENTER);
		
		contentPane.add(feedPanel);
	}
	
	private static void initNameAskPanel() {
		JFrame namesFrame = new JFrame();
		Container contentPaneName = namesFrame.getContentPane();
		
		// Labels
		JLabel nameLabel = new JLabel("Enter a unique username");
		nameLabel.setFont(new Font("Avenir", Font.PLAIN, 20));
		nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		JLabel nameInstr = new JLabel("(Only letters and numbers, no spaces)");
		nameInstr.setFont(new Font("Avenir", Font.PLAIN, 12));
		nameInstr.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		namesFrame.setSize(400, 200);
		namesFrame.setVisible(true);
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		initNameAskPanel();
		
		//  MessageClient client = new MessageClient();
		
		String ip;
		
		if (args.length == 0)
			ip = "127.0.0.1";	// use localhost
		else
			ip = args[0];
		
		// client.connectToServer(ip);
	}
	
	private void connectToServer(String ip) throws UnknownHostException, IOException {
		socket = new Socket(ip, 5000);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
	}
	
	private void manageServerResponse(BufferedReader in) {
		
	}
}
