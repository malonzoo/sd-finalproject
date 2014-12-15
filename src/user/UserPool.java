package user;

import java.io.*;
import java.net.*;
import java.util.*;

import publisher.Publisher;

public class UserPool {

	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Thread> threads = new ArrayList<Thread>();
	private static UserPool instance;
	private int userCount = 0;
	private String lastRequest = "";
	
	/**
	 * Can only have one pool of users
	 */
	private UserPool() {	
	}
	
	public static UserPool getInstance () {
		if (instance == null) {
			instance = new UserPool();
		}
		return instance;
	}
	
	/**
	 * Remove a user from the pool
	 * @return
	 */
	public synchronized void removeUser(User u ) {
		users.remove(u);
		u = null;
		
	}
	
	/**
	 * Add a user to the pool
	 * @return 
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
	
	public synchronized void updateUser(User u) {
		users.add(u);
		notify();
	}
	
	public ArrayList<User> getAllUsers() {
		return this.users;
	}
	
	public void setLastRequest(String s) {
		this.lastRequest = s;
	}
	
	public String getLastRequest() {
		return this.lastRequest;
	}
	
	public ArrayList<Thread> getAllThreads() {
		return this.threads;
	}
}
