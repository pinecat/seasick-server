package net.arch64.gofish.seasick.serve;

import java.net.*;
import java.io.*;

public class ConnThread extends Thread {
	private Socket sock;
	
	public ConnThread(Socket sock) {
		this.sock = sock;
	}
	
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line = "";
			while (sock != null && !sock.isClosed() && line != null && line != "END") {
				line = in.readLine();
				if (line != null) {
					System.out.println("Thread: " + this.getId() + " | " + line);
				}
			}
			sock.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void close() {
		
	}
}
