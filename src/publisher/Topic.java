package publisher;

import java.util.*;
import user.*;

/**
 * Topic object that a User can subscribe to and also holds a list of the
 * users that are subscribed to this topic and a queue of messages to push out
 * @author camillemalonzo
 *
 */
public class Topic {
	
	private User user;	// the corresponding user that this Topic is about
	private String userName;	// the corresponding user's String username
	
	private ArrayList<String> allsubs = new ArrayList<String>(); // a list of subscribers to this topic
	private Queue<Message> queueMessages = new ArrayDeque<Message>(); // all messages
	
	/**
	 * Initialize a Topic
	 * @param u	: the corresponding user that this Topic is about
	 * @param id : the corresponding user's String username
	 */
	public Topic(User u, String id) {
		this.user = u;
		this.userName = id;
	}
	
	/**
	 * Add a subscriber to this topic 
	 * @param subscriber : the username of the subscriber that is going to subscribe to this Topic
	 */
	public void addSubscriber(String subscriber) {
		if ( allsubs.add(subscriber) ) { }
		else
			System.out.println("error: user already exists");
	}
	
	/**
	 * Add a message to this topic
	 * @param message : the message added to this topic
	 */
	public void addMessage(Message message) {
		if ( queueMessages.add(message) ) {	}
		else
			System.out.println("error: message already exists");
	}
	
	/**
	 * Get the user that this Topic is about
	 * @return user that this Topic is about
	 */
	public User getThisUser() {
		return this.user;
	}
	
	/**
	 * Get this user's String username
	 * @return this user's String username
	 */
	public String getUserId() {
		return this.userName;
	}
	
	/**
	 * Get a list of subscribers to this topic
	 * @return a list of subscribers to this topic
	 */
	public String getAllSubscribers() {
		String toReturn = "";
		
		for(int i = 0; i < allsubs.size(); i++){
			toReturn += allsubs.get(i);
			if (i < allsubs.size()-1 )
				toReturn += ", ";
		}
		
		return toReturn;
	}
	
	/**
	 * Get a list of subscribers to this topic
	 * @return a list of subscribers to this topic
	 */
	public ArrayList<String> getAllSubsList() {
		return this.allsubs;
	}
	
	/**
	 * Get the first Message in the queue of this topic's queue of messages
	 * @return the first Message in the queue of this topic's queue of messages
	 */
	public Message flushMessage() {
		return queueMessages.poll();
	}

}
