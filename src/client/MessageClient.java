package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * Message Client. This is the client side GUI and program that anyone 
 * can run on their computer and connect to the Message Server
 * @author camillemalonzo
 *
 */
public class MessageClient implements Runnable {
	private boolean start = false;	// for sending a call to the server to open a socket
	private Socket socket;	// socket to connect to the MessageServer
	private BufferedReader in;	// stream coming in, with Strings coming from the MessageServer 
	private PrintWriter out;	// stream going to the MessageServer
	
	/**
	 * GUI Elements
	 */
	public static String username;
	public static JTextField nameField;
	public static JFrame namesFrame;
	
	private JFrame frame;
	private Container contentPane;
	private JPanel postPanel;
	private JTextArea messageField;
	
	private JPanel allUsersPanel = new JPanel();
	private JPanel subsPanel = new JPanel();
	private ArrayList<Integer> startUsersIndex;
	private HashSet<String> allUsers = new HashSet<String>();
	private HashSet<JPanel> listAllUsersPanels = new HashSet<JPanel>();
	
	private JPanel feedPanel;
	private JPanel feed;
	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	private final Font h2 = new Font("Avenir", Font.PLAIN, 15);
	private HashMap<String,Color> colorCode = new HashMap<String, Color>(); // color key of usernames

	/**
	 * MessageClient Object that opens up the GUI element of the messager
	 */
	public MessageClient() {
		frame = new JFrame();
		frame.setTitle(username + "'s Messager account");
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		initRefresh();
		initTopLabel();
		initPostPanel();
		initSubscriberPanel();
		initFeedPanel();

		frame.setSize(400, 700);
		frame.setVisible(true);
	}
	
	/**
	 * Initializes the GUI element of the refresh button
	 */
	private void initRefresh() {
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setFont(globalFont);
		refreshButton.setMaximumSize(new Dimension(400, 20));
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// checks if the server sent anything to this client
					checkIfAnythingSent();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// sends a request to the server to update the subscriber list
				out.println("SUBUPD");
				run(); // gets the response
			}
		});
		contentPane.add(refreshButton);
	}
	
	/**
	 * Initializes the GUI element of the label of the program. A simple welcome
	 */
	private void initTopLabel() {
		// welcome label
		JLabel welcomeLabel = new JLabel("Welcome to Messager!");
		welcomeLabel.setFont(new Font("Avenir", Font.BOLD, 30));
		welcomeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		contentPane.add(welcomeLabel);		
	}
	
	/**
	 * Initializes the GUI element to post messages to the User's feed of messages
	 */
	private void initPostPanel() {
		postPanel = new JPanel();
		postPanel.setMaximumSize(new Dimension(400, 125));
		postPanel.setLayout(new BorderLayout());
		
		// welcome label
		JLabel welcomeLabel = new JLabel("Welcome to Messager!");
		welcomeLabel.setFont(new Font("Avenir", Font.BOLD, 30));
		postPanel.add(welcomeLabel);
		
		// "write a message" label
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
					// sends a request to post the message
					String request = "POST_@" + username + "_" + messageField.getText();
					if(messageField.getText().compareTo("") != 0) {
						out.println(request);
					}
					messageField.setText("");
				}
				else
					messageField.setText("Only messages less than 140 characters, please!");
			}
		});
		postPanel.add(postButton, BorderLayout.SOUTH);
		postPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
		contentPane.add(postPanel);
	}
	
	/**
	 * Initializes the GUI element to show all the users with accounts
	 */
	private void initSubscriberPanel() {
		subsPanel = new JPanel();
		subsPanel.setMaximumSize(new Dimension(400, 150));
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
	
	/**
	 * Initializes the GUI element that shows the person's feed of messages
	 * This feed will only display the messages of Users the person is subscribed to
	 */
	private void initFeedPanel() {
		feedPanel = new JPanel();
		feedPanel.setMaximumSize(new Dimension(400, 300));
		feedPanel.setLayout(new BorderLayout());
		feedPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 0, 20));
		
		// Label
		JLabel feedLabel = new JLabel("Posts from your Feed");
		feedLabel.setFont(h2);
		feedPanel.add(feedLabel, BorderLayout.NORTH);
		
		// JPanel for Feed
		feed = new JPanel();
		feed.setLayout(new BoxLayout(feed, BoxLayout.Y_AXIS));
		JScrollPane scrollerpane = new JScrollPane(feed);
		scrollerpane.setBorder(BorderFactory.createEmptyBorder());
		scrollerpane.setOpaque(false);
		feedPanel.add(scrollerpane, BorderLayout.CENTER);
		
		contentPane.add(feedPanel);
	}
	
	/**
	 * Initializes the GUI element that at the start of the program, asks the person what their username is
	 */
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
		
		JLabel nameInstr = new JLabel("(Only letters and numbers, please.)");
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
	
	/**
	 * Starts the program by asking the user to enter a username
	 * @param args
	 */
	public static void main(String[] args) {
		initNameAskPanel();
	}
	
	/**
	 * Connects to the server of the passed ip
	 * @param ip String of the ip address of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void connectToServer(String ip) throws UnknownHostException, IOException {
		socket = new Socket(ip, 5000);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		(new Thread(this)).start();
	}
	
	/**
	 * Creates a new user by sending a request to the server
	 * @param out
	 */
	private void newUser(PrintWriter out) {
		out.println("NEWUSER_" + username);
	}
	
	/**
	 * Create a random color
	 * @return
	 */
	private Color createRandomColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}
	
	/**
	 * At start of the program, parses the users at the start of the program
	 * and updates the GUI element of the list of subscribers 
	 * @param s
	 */
	private void parseSubscribers(String s) {
		// parses the string that was sent to the client from the server
		s = s.substring(7, s.length());
		startUsersIndex = new ArrayList<Integer>();
		for(int i = 0; i < s.length(); i++)
			if ((Character.toString(s.charAt(i))).compareTo("@") == 0 )
				startUsersIndex.add(i);
		
		// initialize the GUI element
		// allUsersPanel.setLayout(new BoxLayout(allUsersPanel, BoxLayout.PAGE_AXIS));
		allUsersPanel.setMaximumSize(new Dimension(400, 100));
		
		for(int j = 0; j < startUsersIndex.size(); j++) {
			JPanel oneUser = new JPanel();
			oneUser.setLayout(new BorderLayout());
			oneUser.setMaximumSize(new Dimension(200, 25));
			JLabel userLabel;
			if (j < startUsersIndex.size()-1) {
				allUsers.add( s.substring(startUsersIndex.get(j), startUsersIndex.get(j+1)) );				
				userLabel = new JLabel( s.substring(startUsersIndex.get(j), startUsersIndex.get(j+1)) );
			}
			else {
				allUsers.add( s.substring(startUsersIndex.get(j), s.length()) );
				userLabel = new JLabel( s.substring(startUsersIndex.get(j), s.length()) );
			}
							
			userLabel.setFont(globalFont);
			Color thisColor = createRandomColor();
			colorCode.put(userLabel.getText(), thisColor);
			userLabel.setForeground(thisColor);
			userLabel.setOpaque(false);
			oneUser.add(userLabel);
			
			// add listener to this JPanel
			listAllUsersPanels.add(oneUser);
			addSubscribeListener(oneUser, userLabel.getText());
			
			allUsersPanel.add(oneUser);
		}
	}
	
	/**
	 * Updates the list of subscribers
	 * @param s
	 */
	private void refSubscribers(String s) {
		// parse the request sent from the server
		s = s.substring(7, s.length());
		startUsersIndex = new ArrayList<Integer>();
		for(int i = 0; i < s.length(); i++)
			if ((Character.toString(s.charAt(i))).compareTo("@") == 0 )
				startUsersIndex.add(i);
		
		// update the GUI
		for(int j = 0; j < startUsersIndex.size(); j++) {
			if (j < startUsersIndex.size()-1) {
				if( allUsers.add( s.substring(startUsersIndex.get(j), startUsersIndex.get(j+1))) ) 
					add1User( s.substring(startUsersIndex.get(j), startUsersIndex.get(j+1)) );
			}
			else {
				if( allUsers.add( s.substring(startUsersIndex.get(j), s.length())) ) 
					add1User( s.substring(startUsersIndex.get(j), s.length()) );
			}
		}
		subsPanel.revalidate();
	}
	
	/**
	 * Adds one User to the list of Users/subscribers + its appropriate GUI element
	 * @param s String, the username of the User to add
	 */
	private void add1User(String s) {
		
		JPanel oneUser = new JPanel();
			   oneUser.setLayout(new BorderLayout());
		       oneUser.setMaximumSize(new Dimension(100, 25));
		
		JLabel userLabel = new JLabel( s );
		Color thisColor = createRandomColor();
		colorCode.put(userLabel.getText(), thisColor);
			   userLabel.setForeground(thisColor);
			   userLabel.setOpaque(false);
		
		oneUser.add(userLabel);	
		addSubscribeListener(oneUser, userLabel.getText());
		listAllUsersPanels.add(oneUser);
		allUsersPanel.add(oneUser);
	}
	
	/**
	 * Add a listener to a User JPanel 
	 * @param p JPanel to add the listener to
	 * @param u User to subscribe to
	 */
	private void addSubscribeListener(JPanel p, String u) {
		final String finalU = u;
		p.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				out.println("SUBSCRIBE_@" + username + ":" + finalU);
				run();
			}

			// dont care about these, sry
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}

		});
	}

	/**
	 * Manages the stream of information between this client and the server
	 */
	public void run() {
		String nextLine;
		if (!start)	{
			newUser(out);
			try {
				nextLine = in.readLine();
				if ( (nextLine.substring(0, 6)).compareTo("ALLSUB") == 0 )
					parseSubscribers(nextLine);
			} catch (IOException e) {	e.printStackTrace();	}
			start = true;
		}
		else{
			try {
				nextLine = in.readLine();
				while (nextLine != null) {
					if ( (nextLine.substring(0, 6)).compareTo("ALLSUB") == 0 )
						refSubscribers(nextLine);
					else if( (nextLine.substring(0, 6)).compareTo("BADSUB") == 0  )
						updateSubscribeListBad(nextLine);
					else if( (nextLine.substring(0, 7)).compareTo("GOODSUB") == 0  )
						updateSubscribeList(nextLine);
					else
						System.out.println("ERROR. RECEIVED:" + nextLine);
					
					nextLine = null;	
				}
			}
			catch (IOException e) {	e.printStackTrace(); }		
		}
	}
	
	/**
	 * Updates the GUI of the subscribers (sets the username as dark gray to indicate subscriptions)
	 * @param s
	 */
	private void updateSubscribeList(String s) {
		// get the user subscribed to successfully
		String subscribed = s.substring(8, s.length());
		for (JPanel p : listAllUsersPanels) {
			if ((((JLabel)(p.getComponent(0))).getText()).compareTo("@" + subscribed) == 0 )
				// set the color to gray
				(p.getComponent(0)).setForeground(Color.DARK_GRAY);
		}		
	}
	
	/**
	 * A User cannot subscribe to itself
	 * @param s
	 */
	private void updateSubscribeListBad(String s) {
		System.out.println("Cannot subscribe to yourself!");
	}
	
	/**
	 * Check if any messages were published to any of the topics this User is subscribed to
	 * @throws IOException
	 */
	private void checkIfAnythingSent() throws IOException {
		while( in.ready() ){
			String l = in.readLine();
			if (l != null) {
				if (l.length() >= 9 && l.substring(0, 7).compareTo("NEWMESS") == 0) {
					// update the GUI with the new message
					String user = l.substring(9, l.indexOf(":"));
					String message = l.substring(l.indexOf(":") + 1, l.length());
				
					JPanel newMessage = new JPanel();
					newMessage.setMaximumSize(new Dimension(400, 25));
					newMessage.setLayout(new BorderLayout());
				
					JLabel userLabel = new JLabel("@" + user + ":");
					userLabel.setFont(globalFont);
					userLabel.setForeground(colorCode.get("@" + user));
				
					JLabel messageLabel = new JLabel(message);
					messageLabel.setFont(globalFont);
				
					newMessage.add(userLabel, BorderLayout.WEST);
					newMessage.add(messageLabel, BorderLayout.CENTER);
				
					feed.add(newMessage);
					frame.revalidate();
				}
			}				
		}
	}	
}
