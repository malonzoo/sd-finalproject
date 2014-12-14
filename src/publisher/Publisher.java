package publisher;

import java.awt.Container;
import java.util.*;

import javax.swing.*;

/**
 * This publisher keeps track of the messages and topics that the MessageServer has
 * @author camillemalonzo
 *
 */
public class Publisher {
	
	// HashMap<String, > subscribers; // all the subscribers (held as a hash map)
	private ArrayList<Topic> topics = new ArrayList<Topic>(); // all the topics (held as an ArrayList)
	private ArrayList<Message> messages = new ArrayList<Message>(); // all the messages (held as an array list)
	
	// GUI elements
	private JFrame frame;
	private Container contentPane;
	private JPanel globalFeed;
	
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
		//globalFeed.set
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
	public synchronized void handlesMessage(String s) {
		
	}
	
	/** 
	 * TODO: this method takes a message, s, and parses it, creating a new
	 * Message object. Also adds it to the list of all messages
	 * @param s
	 */
	private Message parseMessage(String s) {
		return null;
	}
	
	private void post() {
		
	}
	
	private void subscribe(String u1, String u2) {
		
	}
	
	private List getAllSubscribers() {
		return null;
	}
	
	/**
	 * Create the GUI to show whats happening
	 * @param args
	 */
	public static void main(String[] args) {
		new Publisher();
	}
}
