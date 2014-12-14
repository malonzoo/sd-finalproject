package server;

import java.io.*;
import java.net.*;

import publisher.Publisher;
import user.User;
import user.UserPool;

public class MessageServer {	
	private static Publisher pub = new Publisher();
	
	public MessageServer() throws IOException {
		
		// attach the server to port 5000
		ServerSocket serverSocket = new ServerSocket(5000);
		UserPool pool = UserPool.getInstance();
		
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
				System.out.println("Getting here");
			}
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		new MessageServer();
	}

}
