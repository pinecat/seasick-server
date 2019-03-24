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
			serveSock = new ServerSocket(port); // create server socket on specified port
		} catch (IOException e) {}
		connLimit = limit; // set the connection limit
		q = new LinkedList<Socket>(); // create queue for conn limiting purposes
		ConnThread.setIncomingConns(0); // intialize incoming conns to 0
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
				Socket sock = serveSock.accept(); // accept connections
				ConnThread connThread = new ConnThread(sock); // create thread to handle the connection
				connThread.start(); // start thread to handle connections
			} catch (IOException e) {}
		}
		
//		while (true) {
//			try {
//				Socket sock = serveSock.accept();
//				if (sock != null && !sock.isClosed()) {
//					q.add(sock);
//				}
//				while (ConnThread.getIncomingConns() < connLimit) {
//					ConnThread connThread = new ConnThread(q.remove());
//					connThread.start();
//					ConnThread.incIncomingConns();
//				}
//			} catch (IOException e) {}
//		}
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
