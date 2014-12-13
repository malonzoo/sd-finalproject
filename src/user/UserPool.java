package user;

import java.io.File;
import java.util.ArrayList;

import publisher.Publisher;

public class UserPool {

	private ArrayList<User> users = new ArrayList<User>();
	private static UserPool instance;
	private int userCount = 0;
	
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
		User u = new User(userCount, p);
		userCount++;
		(new Thread(u)).start();
		return u;
	}
	
	public synchronized void updateUser(User u) {
		users.add(u);
		notify();
	}
}
