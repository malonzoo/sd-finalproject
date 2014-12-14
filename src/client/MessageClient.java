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
	public static String username;
	public static JTextField nameField;
	public static JFrame namesFrame;
	
	private JFrame frame;
	private Container contentPane;
	private JPanel postPanel;
	private JTextArea messageField;
	
	private JPanel subsPanel;
	private JPanel allUsersPanel = new JPanel();
	private ArrayList<Integer> startUsersIndex;
	
	private JPanel feedPanel;
	private JTextArea feed;
	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	private final Font h2 = new Font("Avenir", Font.PLAIN, 15);

	public MessageClient() {
		frame = new JFrame();
		frame.setTitle(username + "'s Dumb Twitter account");
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		initTopLabel();
		initPostPanel();
		initSubscriberPanel();
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
		welcomeLabel.setBackground(Color.RED);
		contentPane.add(welcomeLabel);		
	}
	
	private void initPostPanel() {
		postPanel = new JPanel();
		postPanel.setMaximumSize(new Dimension(400, 125));
		postPanel.setLayout(new BorderLayout());
		postPanel.setBackground(Color.YELLOW);
		
		// welcome
		JLabel welcomeLabel = new JLabel("Welcome to Messager!");
		welcomeLabel.setFont(new Font("Archer", Font.BOLD, 30));
		postPanel.add(welcomeLabel);
		
		// "write a message"
		JLabel instr = new JLabel("Write a message! No more than 140 characters.");
		instr.setFont(h2);
		postPanel.add(instr, BorderLayout.NORTH);
		
		// field to enter message
		messageField = new JTextArea();
		messageField.setEditable(true);
		messageField.setLineWrap(true);
		messageField.setFont(globalFont);
		postPanel.add(messageField, BorderLayout.CENTER);
		
		// post button
		JButton postButton = new JButton("POST");
		postButton.setFont(globalFont);
		postButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (messageField.getText().length() < 141) {
					String request = "POST_@" + username + "_" + messageField.getText();
					out.println(request);
					messageField.setText("");
					manageServerResponse(in);
				}
				else
					messageField.setText("Only messages less than 140 characters, please!");
			}
		});
		postPanel.add(postButton, BorderLayout.SOUTH);
		postPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
		contentPane.add(postPanel);
	}
	
	private void initSubscriberPanel() {
		subsPanel = new JPanel();
		subsPanel.setBackground(Color.BLUE);
		subsPanel.setMaximumSize(new Dimension(400, 100));
		subsPanel.setLayout(new BorderLayout());
		subsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		
		// labels
		JLabel subscriberh1 = new JLabel("All Users");
		subscriberh1.setFont(h2);
		subscriberh1.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		subsPanel.add(subscriberh1, BorderLayout.NORTH);
		
		JTextArea subscriberInstr = new JTextArea();
		subscriberInstr.setText("Toggle the appropriate buttons to follow each user you'd like to follow. Their \nmessages will automatically be posted to your feed when they post messages.");
		subscriberInstr.setEditable(false);
		subscriberInstr.setOpaque(false);
		subscriberInstr.setFont(new Font("Avenir", Font.PLAIN, 10));
		subsPanel.add(subscriberInstr, BorderLayout.SOUTH);
		
		// create panel to put all the subscribers
		subsPanel.add(allUsersPanel);	
		
		contentPane.add(subsPanel);
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
		namesFrame = new JFrame();
		Container contentPaneName = namesFrame.getContentPane();
		JPanel splash = new JPanel();
		splash.setLayout(new BoxLayout(splash, BoxLayout.Y_AXIS));
		splash.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		
		// Labels
		JLabel nameLabel = new JLabel("Enter a unique username");
		nameLabel.setFont(new Font("Avenir", Font.PLAIN, 20));
		nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		splash.add(nameLabel);
		
		JLabel nameInstr = new JLabel("(Only letters and numbers, no spaces)");
		nameInstr.setFont(new Font("Avenir", Font.PLAIN, 12));
		nameInstr.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		nameInstr.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		splash.add(nameInstr);
		
		// JTextField
		nameField = new JTextField();
		nameField.setFont(new Font("Avenir", Font.PLAIN, 12));
		nameField.setMaximumSize(new Dimension(200, 40));
		nameField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		splash.add(nameField);
		
		// button
		JButton nameButton = new JButton("Create Account");
		nameButton.setFont(new Font("Avenir", Font.PLAIN, 12));
		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				username = nameField.getText();
				namesFrame.setVisible(false);
				MessageClient client = new MessageClient();			
				String ip = "127.0.0.1";	// change to camille's IP address later
				
				try {
					client.connectToServer(ip);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		nameButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		splash.add(nameButton);
		
		contentPaneName.add(splash);
		
		namesFrame.setSize(400, 200);
		namesFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		initNameAskPanel();
	}
	
	private void connectToServer(String ip) throws UnknownHostException, IOException {
		socket = new Socket(ip, 5000);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		requestSubscribersList(out);
	}
	
	private void manageServerResponse(BufferedReader in) {
		try {
			String nextLine = in.readLine();
			if ( (nextLine.substring(0, 6)).compareTo("ALLSUB") == 0 )
				parseSubscribers(nextLine);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void requestSubscribersList(PrintWriter out) {
		out.println("NEWUSER_" + username);
		manageServerResponse(in);
	}
	
	private Color createRandomColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}
	
	private void parseSubscribers(String s) {
		s = s.substring(7, s.length());
		startUsersIndex = new ArrayList<Integer>();
		for(int i = 0; i < s.length(); i++)
			if ((Character.toString(s.charAt(i))).compareTo("@") == 0 )
				startUsersIndex.add(i);
		
		allUsersPanel.setLayout(new BoxLayout(allUsersPanel, BoxLayout.PAGE_AXIS));
		
		for(int j = 0; j < startUsersIndex.size(); j++) {
			JPanel oneUser = new JPanel();
			oneUser.setLayout(new BorderLayout());
			JLabel userLabel;
			if (j < startUsersIndex.size()-1)
				userLabel = new JLabel( s.substring(startUsersIndex.get(j), startUsersIndex.get(j+1)) );
			else
				userLabel = new JLabel( s.substring(startUsersIndex.get(j), s.length()) );
			
			userLabel.setFont(globalFont);
			userLabel.setForeground(createRandomColor());
			userLabel.setOpaque(false);
			oneUser.add(userLabel);
			
			allUsersPanel.add(oneUser);
			
//			ButtonGroup group = new ButtonGroup();
//			JRadioButton yesSub = new JRadioButton("Y");
//			JRadioButton noSub = new JRadioButton("N");
		}
		
		
	}
	
}
