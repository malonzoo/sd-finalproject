package publisher;

public class Message {

	private final String user;
	private final String message;
	
	
	public Message(String u, String m) {
		this.user = u;
		this.message = m;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
