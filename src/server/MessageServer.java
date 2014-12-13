package server;

import java.io.*;
import java.net.*;

import publisher.Publisher;
import user.User;
import user.UserPool;

public class MessageServer {
	
	private static Publisher pub = new Publisher();
	
	public MessageServer() throws IOException, InterruptedException {
		
		// attach the server to port 5000
		ServerSocket serverSocket = new ServerSocket(5000);
		UserPool pool = UserPool.getInstance();
		
		// wait forever for client connections
		while (true) {
			// when this metho returns, it means a client has connected
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
	
	public static void main(String[] args) throws IOException, InterruptedException {
		new MessageServer();
	}

}