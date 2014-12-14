package publisher;

import user.User;

public class Message {

	private final String userId;
	private final String message;
	private final User user;
	
	
	public Message(String userId, String m, User u) {
		this.userId = userId;
		this.message = m;
		this.user = u;
	}
	
	public String getUserID() {
		return this.userId;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public User getUser() {
		return this.user;
	}
	
}
