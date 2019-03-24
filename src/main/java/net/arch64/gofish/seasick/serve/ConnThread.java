/* Package */
package net.arch64.gofish.seasick.serve;

/* Imports */
import java.net.*;
import java.io.*;

/* Class: ConnThread */
public class ConnThread extends Thread {
	private	static int incomingConns;
	private	Socket sock;
	
	/**
	 * ConnThread
	 * @param sock
	 * 
	 * Constructor for the ConnThread class.
	 */
	public ConnThread(Socket sock) {
		this.sock = sock;
	}
	
	/**
	 * run
	 * 
	 * The run subroutine for threads. Called
	 * with .start(). Starts handling of a
	 * client connection to the server.
	 */
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line = "";
			while (sock != null && !sock.isClosed() && line != null && !line.equals("END")) {
				line = in.readLine();
				if (line != null) {
					System.out.println("Thread: " + this.getId() + " | " + line);
				}
			}
			sock.close();
			decIncomingConns();
		} catch (IOException e) {}
	}
	
	public static int getIncomingConns() { return incomingConns; } /** getIncomingConns(): @return incomingConns */
	public static void setIncomingConns(int n) { incomingConns = n; } /** setIncomingConns(): @set incomingConns */
	public static void incIncomingConns() { incomingConns++; } /** incIncomingConns(): @increment incomingConns */
	public static void decIncomingConns() { incomingConns--; } /** decIncomingConns(): @decrement incomingConns */
}