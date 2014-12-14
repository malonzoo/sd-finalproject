package publisher;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import user.*;

/**
 * This publisher keeps track of the messages and topics that the MessageServer has
 * @author camillemalonzo
 *
 */
public class Publisher {
	
	
	private ArrayList<Topic> topics = new ArrayList<Topic>(); // all the topics (held as an ArrayList)
	private ArrayList<Message> messages = new ArrayList<Message>(); // all the messages (held as an array list)
	
	// GUI elements
	private JFrame frame;
	private Container contentPane;
	private JPanel globalFeed;
	private MessageTableView globalMessages;
	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	private final Font tableLabels = new Font("Avenir", Font.BOLD, 16);
	private final Font h1 = new Font("Archer", Font.BOLD, 30);
	private final Font h2 = new Font("Avenir", Font.PLAIN, 20);
	
	public Publisher() {	
		frame = new JFrame();
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		initGlobalFeed();
		
		frame.setSize(400, 600);
		frame.setVisible(true);
	}
	
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
		feedLabel.setMaximumSize(new Dimension(400, 50));
		feedLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		JLabel userLabel = new JLabel("USERNAME");
		userLabel.setFont(tableLabels);
		feedLabel.add(userLabel);
		
		JLabel messageLabel = new JLabel("MESSAGE");
		messageLabel.setFont(tableLabels);
		feedLabel.add(messageLabel);
		
		globalFeed.add(feedLabel);
		
		// table
		globalMessages = new MessageTableView(messages);
		globalFeed.add(globalMessages);
		
		contentPane.add(globalFeed);
	}
	
	/**
	 * TODO: this method:
	 * 1. parses the message into a Message object
	 * 2. handles types of requests:
	 *    a) POST: adds a message to the Messages and also pushes it to the appropriate topic
	 *    b) SUBSCRIBE: subscribes user 1 to the topic of user 2
	 *    c) GETALLSUB: gets a list of all the subscribers in the Message Server
	 * @param s
	 */
	public void handlesMessage(String s, PrintWriter out, User u) {
		if ( (s.substring(0, 7)).compareTo("NEWUSER") == 0 ) {
			topics.add( new Topic( u, s.substring(8, s.length() )) );
			pushOutAllSubscribers(out);
		}
		else {
			Message m = parseMessage(s, u);
			if ( m != null) {
				messages.add(m);
				globalFeed.remove(globalMessages);
				globalMessages = new MessageTableView(messages);
				globalFeed.add(globalMessages);
				contentPane.repaint();
				contentPane.revalidate();
				contentPane.validate();
				
				out.write("Message received. Thanks, " + m.getUserID() + "\n");
				out.flush();
			}
		}
	}
	
	private void pushOutAllSubscribers(PrintWriter out) {
		String toReturn = "ALLSUB_";
		for (Topic t : topics)
			toReturn += "@" + t.getUserId();
		
		toReturn += "\n";
		out.write(toReturn);
		out.flush();
	}
	
//	/**
//	 * I potentially don't need this
//	 * @param u
//	 * @return true if the topic is already in the arraylist of topics
//	 * @return false if the topic is not yet in the arryalist of topics
//	 */
//	private boolean lookThroughTopics(User u) {
//		Iterator<Topic> i = topics.iterator();
//		while( i.hasNext() ) {
//			Topic current = i.next();
//			if ( current.getThisUser().getID() == u.getID() )
//				return true;
//		}
//		
//		return false;
//	}
	
	/** 
	 * TODO: this method takes a message, s, and parses it, creating a new
	 * Message object. Also adds it to the list of all messages
	 * @param s
	 */
	private Message parseMessage(String s, User u) {
		if ( (s.substring(0, 4)).compareTo("POST") == 0 ) {
			System.out.println(s);
			
			boolean ifFound = false;
			int i = 5;
			while ( !ifFound && i < s.length() ) {
				if ( (Character.toString(s.charAt(i))).compareTo("_") == 0 ) {
					ifFound = true;
				}
				else
					i++;
			}
			
			String user = s.substring(7-1, i);
			String message = s.substring(i+1, s.length());
			
			return new Message(user, message, u);
		}
		
		return null;
	}
	
	private void post() {
		
	}
	
	private void subscribe(String u1, String u2) {
		
	}
	
	private List getAllSubscribers() {
		return null;
	}
}
