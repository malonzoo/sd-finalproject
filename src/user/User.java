package user;

import java.io.*;
import java.net.*;
import java.util.*;
import publisher.Publisher;

public class User implements Runnable {
	
	private Socket socket;
	private final int userID;
	private Publisher publisher;
	
	public User(int id, Publisher p) {
		this.userID = id;
		this.publisher = p;
	}
	
	/** 
	 * Returns the unique user ID for this user
	 * @return the unique user ID for this user
	 */
	public int getID() {
		return userID;
	}
	
	public synchronized void setSocket(Socket s) {
		if (socket != null) {
			throw new IllegalStateException("User is already handling a request");
		}
		socket = s;
		notify();
	}
	
	private void handleConnection() throws IOException {
		System.out.println("starting to handle request");
		// Set up the streams to allow 2-way communication with the client.
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		// Get and execute the client's commands.
		String command = in.readLine();
		while (command != null) {
			publisher.handlesMessage(command, out, this);
			command = in.readLine();
		}
		
		in.close();
		out.close();
		
		System.out.println("done handling request");
	}

	/**
	 * Handle client commands until the connection is closed.
	 */
	public void run() {
		synchronized (this) {
			try {
				while(true) {
					while (socket == null) {
						wait();
					}
					try {
						handleConnection();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
					socket = null;
					UserPool.getInstance().updateUser(this);
				}
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}		
	}
	
	/**
	 * TODO: Execute an individual command.
	 */
	private void execute() {
		
	}

}
