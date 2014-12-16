package user;

import java.io.*;
import java.net.*;
import publisher.*;

/**
 * User object that is a runnable on the server side
 * @author camillemalonzo
 *
 */
public class User implements Runnable {
	
	/** server instances */
	private Socket socket; // the socket of this User
	private final int userID; // this User's unique userID
	private Publisher publisher; // the publisher that this User talks to
	private BufferedReader in; // this User's stream from a client
	private PrintWriter out; // this Users stream to a client
	private Thread thread; // this thread
	
	/**
	 * Initialize a new User
	 * @param id : username string of the User on the client side
	 * @param p : the corresponding publisher
	 */
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
	
	/**
	 * Set socket for this User
	 * @param s
	 */
	public synchronized void setSocket(Socket s) {
		if (socket != null) {
			throw new IllegalStateException("User is already handling a request");
		}
		socket = s;
		notify();
	}
	
	/**
	 * Handle the connection between the corresponding client
	 * @throws IOException
	 */
	private void handleConnection() throws IOException {
		// Set up the streams to allow 2-way communication with the client.
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		// add new user
		String newUserRequest = in.readLine();
		publisher.handlesMessage(newUserRequest, out, this);
				
		// Get and execute the client's commands.
		String command = in.readLine();
		while (command != null) {
			Topic t = publisher.handlesMessage(command, out, this);
			if( t != null ){
				// if we get here, this means there is a Topic to be flushed
				// get the message to push
				Message m = t.flushMessage();
				
				// for each string in the subscribers list of the Topic to be flushed
				for( String uString : t.getAllSubsList()  ) {
					// get the corresponding User Int Id
					int uIntId = publisher.getUserKey().get(uString);
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
	
	/**
	 * Get this Users stream to a client
	 * @return this Users stream to a client
	 */
	public PrintWriter getOut() {
		return this.out;
	}
	
	/**
	 * Get this thread
	 * @param t this thread
	 */
	public void setThread(Thread t) {
		this.thread = t;
	}

}
