package publisher;

import java.util.*;

import user.*;

public class Topic {
	
	private User user;
	private String userName;
	
	private ArrayList<String> allsubs = new ArrayList<String>(); // a list of subscribers to this topic
	private ArrayList<Message> allmessages = new ArrayList<Message>(); // all of the messages in this topic
	
	public Topic(User u, String id) {
		this.user = u;
		this.userName = id;
	}
	
	/**
	 * Add a subscriber to this topic 
	 */
	public void addSubscriber(String subscriber) {
		if ( allsubs.add(subscriber) )
			allsubs.add(subscriber);
		else
			System.out.println("error: user already exists");
	}
	
	/**
	 * Add a message to this topic
	 */
	public void addMessage(Message message) {
		if ( allmessages.add(message) )
			allmessages.add(message);
		else
			System.out.println("error: message already exists");
	}
	
	public User getThisUser() {
		return this.user;
	}
	
	public String getUserId() {
		return this.userName;
	}

}
