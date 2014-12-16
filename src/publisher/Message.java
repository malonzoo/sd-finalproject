package publisher;

import user.User;

/**
 * Message object of each message posted in the Message application
 * @author camillemalonzo
 *
 */
public class Message {

	// information held by the message
	private final String userId;	// the username of the person who posted this message
	private final String message;	// the message itself
	private final User user;	// the corresponding User (thread) who posted this message
	
	/**
	 * Initialize this Message
	 * @param userId: username of the person who posted this message
	 * @param m : the message itself
	 * @param u : the corresponding User (thread) who posted this message
	 */
	public Message(String userId, String m, User u) {
		this.userId = userId;
		this.message = m;
		this.user = u;
	}
	
	/**
	 * Get this's message user id
	 * @return message user id
	 */
	public String getUserID() {
		return this.userId;
	}
	
	/**
	 * Get this message's message text
	 * @return message text
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Get this message's User runnable
	 * @return User runnable
	 */
	public User getUser() {
		return this.user;
	}
	
}
