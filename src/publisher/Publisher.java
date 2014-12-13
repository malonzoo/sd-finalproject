package publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This publisher keeps track of the messages and topics that the MessageServer has
 * @author camillemalonzo
 *
 */
public class Publisher {
	
	// HashMap<String, > subscribers; // all the subscribers (held as a hash map)
	ArrayList<Topic> topics; // all the topics (held as an ArrayList)
	ArrayList<Message> messages; // all the messages (held as an array list)
	
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
		
	}
}
