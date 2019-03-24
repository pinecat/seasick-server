package net.arch64.gofish.seasick.serve;

import java.net.*;
import java.io.*;

public class ConnThread extends Thread {
	private static int incomingConns;
	private Socket sock;
	
	public ConnThread(Socket sock) {
		this.sock = sock;
	}
	
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
	
	public void close() {
		
	}
	
	public static int getIncomingConns() { return incomingConns; }
	public static void setIncomingConns(int n) { incomingConns = n; }
	public static void incIncomingConns() { incomingConns++; }
	public static void decIncomingConns() { incomingConns--; }
}
