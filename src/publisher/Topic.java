package publisher;

import java.util.*;

import user.*;

public class Topic {
	
	private User user;
	private String userName;
	
	private ArrayList<String> allsubs = new ArrayList<String>(); // a list of subscribers to this topic
	private Queue<Message> queueMessages = new ArrayDeque<Message>(); // all messages
	
	public Topic(User u, String id) {
		this.user = u;
		this.userName = id;
	}
	
	/**
	 * Add a subscriber to this topic 
	 */
	public void addSubscriber(String subscriber) {
		if ( allsubs.add(subscriber) ) {
			System.out.println("server.topic.addsubscriber subscriber: " + subscriber);
		}
		else
			System.out.println("error: user already exists");
	}
	
	/**
	 * Add a message to this topic
	 */
	public void addMessage(Message message) {
		if ( queueMessages.add(message) ) {
			System.out.println("server.topic.message added to queue");
		}
		else
			System.out.println("error: message already exists");
	}
	
	public User getThisUser() {
		return this.user;
	}
	
	public String getUserId() {
		return this.userName;
	}
	
	public String getAllSubscribers() {
		String toReturn = "";
		
		for(int i = 0; i < allsubs.size(); i++){
			toReturn += allsubs.get(i);
			if (i < allsubs.size()-1 )
				toReturn += ", ";
		}
		
		return toReturn;
	}
	
	public ArrayList<String> getAllSubsList() {
		return this.allsubs;
	}
	
	public Message flushMessage() {
		return queueMessages.poll();
	}

}
