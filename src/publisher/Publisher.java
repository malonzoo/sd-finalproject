package publisher;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import publisher.view.MessageTableView;
import publisher.view.TopicsTableView;
import user.*;

/**
 * This publisher keeps track of the messages and topics that the MessageServer manages
 * @author camillemalonzo
 *
 */
public class Publisher {
	
	private UserPool userpool;	// pool of User threads
	private ArrayList<Topic> topics = new ArrayList<Topic>(); // all the topics (held as an ArrayList)
	private ArrayList<Message> messages = new ArrayList<Message>(); // all the messages (held as an array list)
	
	private HashMap<String, Integer> userKey = new HashMap<String, Integer>(); // value: Integer of User, key: String of UserName
	
	/**
	 *  GUI elements
	 */
	private JFrame frame;
	private Container contentPane;
	private JPanel globalFeed;
	private MessageTableView globalMessages;
	
	private JScrollPane scrollFeed;
	private JScrollPane scrollUsers;
	
	private JPanel globalUsers;
	private TopicsTableView globalUsersView;
	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	private final Font tableLabels = new Font("Avenir", Font.BOLD, 16);
	private final Font h1 = new Font("Avenir", Font.BOLD, 30);
	private final Font h2 = new Font("Avenir", Font.PLAIN, 20);
	
	/**
	 * Create a Publisher GUI that publishes all the messages that are posted by the
	 * users connected to the server and also shows the users currently connected to the server
	 * @param pool the UserPool of User Threads that the MessageServer Uses
	 */
	public Publisher(UserPool pool) {
		this.userpool = pool;
		
		// initialize GUI elements
		frame = new JFrame();
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));		
		initGlobalFeed();
		initGlobalUsers();		
		frame.setSize(500, 650);
		frame.setVisible(true);
	}
	
	/**
	 * Initialize the GUI element for the feed of messages
	 */
	private void initGlobalFeed() {
		globalFeed = new JPanel();
		globalFeed.setLayout(new BoxLayout(globalFeed, BoxLayout.Y_AXIS));
		
		// label
		JLabel streamLabel = new JLabel("Stream of all Messages");
		streamLabel.setFont(h1);
		streamLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		streamLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		globalFeed.add(streamLabel);
		
		// stream labels
		JPanel feedLabel = new JPanel();
		feedLabel.setLayout(new GridLayout(1, 2));
		feedLabel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		feedLabel.setMaximumSize(new Dimension(500, 50));
		feedLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		JLabel userLabel = new JLabel("USERNAME");
		userLabel.setFont(tableLabels);
		feedLabel.add(userLabel);
		
		JLabel messageLabel = new JLabel("MESSAGE");
		messageLabel.setFont(tableLabels);
		feedLabel.add(messageLabel);
		
		globalFeed.add(feedLabel);
		
		// table
		initScrollMessages();
		
		globalFeed.add(scrollFeed);
		
		contentPane.add(globalFeed);
	}
	
	/**
	 * Initialize the scroll feed of messages
	 */
	private void initScrollMessages() {
		globalMessages = new MessageTableView(messages);
		globalMessages.setMaximumSize(new Dimension(500, 175));
		
		scrollFeed = new JScrollPane(globalMessages);
		scrollFeed.setBorder(BorderFactory.createEmptyBorder());
		scrollFeed.setOpaque(false);
		scrollFeed.getViewport().setOpaque(false);
		scrollFeed.setMaximumSize(new Dimension(500, 175));
	}
	
	/**
	 * Initialize the GUI element of the feed of Users connected to the server
	 */
	private void initGlobalUsers() {
		globalUsers = new JPanel();
		globalUsers.setLayout(new BoxLayout(globalUsers, BoxLayout.Y_AXIS));
		
		// label
		JLabel topLabel = new JLabel("List of all Users Online");
		topLabel.setFont(h1);
		topLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		topLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 5, 0));
		globalUsers.add(topLabel);
		
		// stream labels
		JPanel userTableLabels = new JPanel();
		userTableLabels.setLayout(new GridLayout(1, 2));
		userTableLabels.setAlignmentY(JPanel.TOP_ALIGNMENT);
		userTableLabels.setMaximumSize(new Dimension(500, 50));
		userTableLabels.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		JLabel userLabel = new JLabel("USERNAME");
		userLabel.setFont(tableLabels);
		userTableLabels.add(userLabel);
		
		JLabel messageLabel = new JLabel("THEIR SUBSCRIBERS");
		messageLabel.setFont(tableLabels);
		userTableLabels.add(messageLabel);
		
		globalUsers.add(userTableLabels);
		
		// table
		initScrollUsers();				
		globalUsers.add(scrollUsers);
				
		contentPane.add(globalUsers);
	}
	
	/**
	 * Initialize the GUI element of the feed of users in this server
	 */
	private void initScrollUsers() {
		globalUsersView = new TopicsTableView(topics);
		globalUsersView.setMaximumSize(new Dimension(500, 175));
				
		scrollUsers = new JScrollPane(globalUsersView);
		scrollUsers.setBorder(BorderFactory.createEmptyBorder());
		scrollUsers.setOpaque(false);
		scrollUsers.getViewport().setOpaque(false);
		scrollUsers.setMaximumSize(new Dimension(500, 175));
	}
	
	/**
	 * 1. parses the message into a Message object
	 * 2. handles types of requests:
	 *    a) POST: adds a message to the Messages and also pushes it to the appropriate topic
	 *    b) SUBSCRIBE: subscribes user 1 to the topic of user 2
	 *    c) GETALLSUB: gets a list of all the subscribers in the Message Server
	 * @param s String current request
	 * @param out the stream to push a message out
	 * @param u the thread currently "on"
	 * @return a Topic that has bee updated. Returned to the User Thread, the User will then
	 */
	public Topic handlesMessage(String s, PrintWriter out, User u) {
		// if the request is to get the list of subscribers in the network
		if ( s.compareTo("SUBUPD") == 0 ) {
			pushOutAllSubscribers(out);
			return null;
		}
		// if the request is to add a new user to the network
		else if ( (s.substring(0, 7)).compareTo("NEWUSER") == 0 ) {
			topics.add( new Topic( u, s.substring(8, s.length() )) );
			pushOutAllSubscribers(out);
			
			// update the key
			userKey.put(s.substring(8, s.length()), u.getID());
			
			// update topics view of server
			refreshTheUsersView();
			return null;
		}
		// if the request is to subscribe one user to another user's topic
		else if ( (s.substring(0, 9)).compareTo("SUBSCRIBE") == 0 ) {
			String subscriber = s.substring(11, s.indexOf(":"));
			String topic = s.substring( s.indexOf(":") + 2 , s.length());
			if (subscriber.compareTo(topic) != 0) {
				// add the new subscriber to the topic
				for(Topic t: topics) {
					if( t.getUserId().compareTo(topic) == 0 ) {
						t.addSubscriber(subscriber);
					}
				}

				// update the Publisher GUI
				refreshTheUsersView();
				
				// tell the subscriber that it was a successful subscription
				out.println("GOODSUB@" + topic);
			}
			else {	out.println("BADSUB");	}
			return null;
		}
		else {
			Message m = parseMessage(s, u);
			if ( m != null) {
				messages.add(m);
				globalFeed.remove(scrollFeed);
				globalMessages = new MessageTableView(messages);
				globalMessages.setMaximumSize(new Dimension(500, 175));
				scrollFeed = new JScrollPane(globalMessages);
				scrollFeed.setBorder(BorderFactory.createEmptyBorder());
				scrollFeed.setOpaque(false);
				scrollFeed.getViewport().setOpaque(false);
				scrollFeed.setMaximumSize(new Dimension(500, 175));
				globalFeed.add(scrollFeed);
				contentPane.revalidate();
				contentPane.validate();
			}
			
			// go through the message's topic and push the message off the queue
			for(Topic t : topics) {
				if( t.getUserId().compareTo(m.getUserID()) == 0) {
					return t;
				}
			}
			return null;
		}
	}
	
	/**
	 * Refresh the GUI View of the Users list
	 */
	private void refreshTheUsersView() {		
		globalUsers.remove(scrollUsers);
		globalUsersView = new TopicsTableView(topics);
		globalUsersView.setMaximumSize(new Dimension(500, 175));
		scrollUsers = new JScrollPane(globalUsersView);
		scrollUsers.setBorder(BorderFactory.createEmptyBorder());
		scrollUsers.setOpaque(false);
		scrollUsers.getViewport().setOpaque(false);
		scrollUsers.setMaximumSize(new Dimension(500, 175));
		globalUsers.add(scrollUsers);
		contentPane.revalidate();
		contentPane.validate();
	}
	
	/**
	 * Push out a list of all the users in the network
	 * @param out
	 */
	private void pushOutAllSubscribers(PrintWriter out) {
		out.write(getAllSubscribers());
		out.flush();
	}
	
	/**
	 * Get the String of all the subscribers in the network
	 * @return String of all the subscribers in the network
	 */
	public String getAllSubscribers() {
		String toReturn = "ALLSUB_";
		for (Topic t : topics)
			toReturn += "@" + t.getUserId();
		
		toReturn += "\n";
		return toReturn;
	}
	
	
	/** 
	 * this method takes a message, s, and parses it, creating a new
	 * Message object. Also adds it to the list of all messages
	 * @param s
	 */
	private Message parseMessage(String s, User u) {
		if ( (s.substring(0, 4)).compareTo("POST") == 0 ) {
			boolean ifFound = false;
			int i = 5;
			while ( !ifFound && i < s.length() ) {
				if ( (Character.toString(s.charAt(i))).compareTo("_") == 0 )
					ifFound = true;
				else
					i++;
			}
			
			String user = s.substring(7-1, i);
			String message = s.substring(i+1, s.length());
			Message m = new Message(user, message, u);
			
			for(Topic t : topics) {
				if( t.getUserId().compareTo(user) == 0) {
					t.addMessage(m);
				}
			}		
			return m;
		}		
		return null;
	}
	
	/**
	 * Get the UserPool of this publisher
	 * @return
	 */
	public UserPool getPool() {
		return this.userpool;
	}
	
	/**
	 * Get the key of Users 
	 * @return
	 */
	public HashMap<String, Integer> getUserKey() {
		return this.userKey;
	}
}
