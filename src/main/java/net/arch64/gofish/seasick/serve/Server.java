/* Package */
package net.arch64.gofish.seasick.serve;

/* Imports */
import java.net.*;
import java.io.*;

/* Class: Server */
public class Server {
	private ServerSocket serveSock;
	/**
	 * Server
	 * @param port
	 * 
	 * Constructor for the Server class.
	 * @throws IOException 
	 */
	public Server(int port) throws IOException {
		serveSock = new ServerSocket(port);
	}
	
	/**
	 * start
	 * 
	 * Start listening with the server.
	 * @throws IOException 
	 */
	public void start() throws IOException {
		while (true) {
			Socket sock = serveSock.accept();
			ConnThread connThread = new ConnThread(sock);
			connThread.start();
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
