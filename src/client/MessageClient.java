package client;

import java.awt.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class MessageClient {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public MessageClient() {
		
	}
	
	private void initPostPanel() {
		
	}
	
	private void initFeedPanel() {
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		MessageClient client = new MessageClient();
		String ip;
		
		if (args.length == 0)
			ip = "127.0.0.1";	// use localhost
		else
			ip = args[0];
		
		client.connectToServer(ip);
	}
	
	private void connectToServer(String ip) throws UnknownHostException, IOException {
		socket = new Socket(ip, 5000);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
	}
	
	private void manageServerResponse(BufferedReader in) {
		
	}
}
