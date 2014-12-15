package user;

import java.io.*;
import java.net.*;
import java.util.*;

import publisher.Message;
import publisher.Publisher;
import publisher.Topic;

public class User implements Runnable {
	
	private Socket socket;
	private final int userID;
	private Publisher publisher;
	private BufferedReader in;
	private PrintWriter out;
	private Thread thread;
	
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
		// Set up the streams to allow 2-way communication with the client.
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		// add new user
		String newUserRequest = in.readLine();
		publisher.handlesMessage(newUserRequest, out, this);
				
		// Get and execute the client's commands.
		String command = in.readLine();
		System.out.println("server.reading command: " + command);
		while (command != null) {
			Topic t = publisher.handlesMessage(command, out, this);
			if( t != null ){
				// if we get here, this means there is a Topic to be flushed
				System.out.println("server.user.topic is gonna flush");
				// get the message to push
				Message m = t.flushMessage();
				
				// for each string in the subscribers list of the Topic to be flushed
				for( String uString : t.getAllSubsList()  ) {
					// get the corresponding User Int Id
					int uIntId = publisher.getUserKey().get(uString);
					System.out.println("server.user.topic uReceiver: " + uIntId + ", message: " + m.getMessage());
					thread.interrupt();
					(publisher.getPool().getAllUsers().get(uIntId)).getOut().println( "NEWMESS_@" + m.getUserID() + ":" + m.getMessage());
				}
			}
			
			// check if the topic needs to be flushed out, in which case go through each User and push a message out		
			command = in.readLine();
		}
		
		in.close();
		out.close();
		socket.close();
	}

	/**
	 * Handle client commands until the connection is closed.
	 */
	public void run() {
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
	
//	/**
//	 * TODO: Execute an individual command.
//	 */
//	private void execute() {
//		
//	}
	
	public PrintWriter getOut() {
		return this.out;
	}
	
	public void setThread(Thread t) {
		this.thread = t;
	}

}
