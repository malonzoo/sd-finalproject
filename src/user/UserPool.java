package user;

import java.io.*;
import java.net.*;
import java.util.*;

import publisher.Publisher;

/**
 * Pool of Users for the Message Program on the server side
 * @author camillemalonzo
 *
 */
public class UserPool {

	private ArrayList<User> users = new ArrayList<User>();	// list of all the User runnables currently on the program
	private ArrayList<Thread> threads = new ArrayList<Thread>();	// list of all the Threads currently running 
	private static UserPool instance;	// singleton UserPool instance
	private int userCount = 0;	// the number of users
	private String lastRequest = "";	// get the last request send to the pool of users
	
	/**
	 * Can only have one pool of users
	 */
	private UserPool() {	}
	
	/**
	 * Create a new instance of the UserPool
	 * @return
	 */
	public static UserPool getInstance () {
		if (instance == null) {
			instance = new UserPool();
		}
		return instance;
	}
	
	/**
	 * Remove a user from the pool
	 * @return none
	 */
	public synchronized void removeUser(User u ) {
		users.remove(u);
		u = null;
	}
	
	/**
	 * Add a user to the pool
	 * @return none
	 */
	public synchronized User addUser(Publisher p) {
		// Runnables
		User u = new User(userCount, p);
		users.add(u);
		userCount++;
		
		// Thread
		Thread t = new Thread(u);
		u.setThread(t);
		t.start();
		threads.add(t);
		
		return u;
	}
	
	/**
	 * Add a User to the pool
	 * @param u
	 */
	public synchronized void updateUser(User u) {
		users.add(u);
		notify();
	}
	
	/**
	 * Get list of all the User runnables currently on the program
	 * @return list of all the User runnables currently on the program
	 */
	public ArrayList<User> getAllUsers() {
		return this.users;
	}
	
	/**
	 * Set the last request of the User Pool
	 * @param s of the last request of the User Pool
	 */
	public void setLastRequest(String s) {
		this.lastRequest = s;
	}
	
	/**
	 * Get the last request made to the user pool
	 * @return String of the last request made to the user pool
	 */
	public String getLastRequest() {
		return this.lastRequest;
	}
	
	/**
	 * Get list of all the Threads currently running 
	 * @return list of all the Threads currently running 
	 */
	public ArrayList<Thread> getAllThreads() {
		return this.threads;
	}
}
