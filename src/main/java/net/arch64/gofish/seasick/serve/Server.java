/* Package */
package net.arch64.gofish.seasick.serve;

/* Imports */
import java.net.*;
import java.io.*;
import java.util.*;

/* Class: Server */
public class Server {
	private ServerSocket serveSock;
	private int connLimit;
	private Queue<Socket> q;
	
	/**
	 * Server
	 * @param port
	 * 
	 * Constructor for the Server class.
	 * @throws IOException 
	 */
	public Server(int port, int limit) {
		try {
			serveSock = new ServerSocket(port);
		} catch (IOException e) {}
		connLimit = limit;
		q = new LinkedList<Socket>();
		ConnThread.setIncomingConns(0);
	}
	
	/**
	 * start
	 * 
	 * Start listening with the server.
	 * @throws IOException 
	 */
	public void start() {
		while (true) {
			try {
				Socket sock = serveSock.accept();
				ConnThread connThread = new ConnThread(sock);
				connThread.start();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * close
	 * 
	 * Stop listening with the server.
	 * Not working right now :(
	 */
	public void stop() {
		
	}
}
