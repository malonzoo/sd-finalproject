package server;

import java.io.*;
import java.net.*;
import publisher.Publisher;
import user.*;

/**
 * MessageServer that is the server for this program
 * @author camillemalonzo
 *
 */
public class MessageServer {	
	
	/* the handler that manages the requests between this server and its many clients */
	private static Publisher pub;
	
	/**
	 * Initialize the Message Server
	 * @throws IOException
	 */
	public MessageServer() throws IOException {
		
		// attach the server to port 5000
		ServerSocket serverSocket = new ServerSocket(5000);
		UserPool pool = UserPool.getInstance();
		pub = new Publisher(pool);
		
		// wait forever for client connections
		while (true) {
			// when this method returns, it means a client has connected
			Socket s = serverSocket.accept();
			User u = pool.addUser(pub);
			if (u == null) {
				s.close();
			}
			else {
				u.setSocket(s);
			}
		}
	}
	
	/**
	 * Create a new MessageServer at the start of this program
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		new MessageServer();
	}

}
