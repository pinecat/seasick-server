/* Package */
package net.arch64.gofish.seasick.serve;

/* Imports */
import java.net.*;
import java.sql.SQLException;

import com.google.gson.Gson;

import java.io.*;

import net.arch64.gofish.seasick.core.Config;
import net.arch64.gofish.seasick.users.Query;
import net.arch64.gofish.seasick.users.User;
import net.arch64.gofish.seasick.queries.*;

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
		Config conf = new Config("seasick.conf");
		Query query = new Query(conf);
		ProfilePageQuery ppq = new ProfilePageQuery(conf);
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			//DataInputStream in = new DataInputStream(sock.getInputStream());
			PrintWriter out = new PrintWriter(sock.getOutputStream());
			Gson gson = new Gson();
			String line = "";
			while (sock != null && !sock.isClosed() && line != null && !line.equals("END")) {
				line = in.readLine();
				if (line != null) {
					//System.out.println("Thread: " + this.getId() + " | " + line);
					Message msg = new Message();
					msg = gson.fromJson(line, msg.getClass());
					
					//System.out.println(msg.getMsg());
					//System.out.println(msg.getUser().getEmail());
					//System.out.println(msg.getUser().getPassword());
					
					switch (msg.getMsg()) {
					case "auth": 
						boolean authd = query.authUser(msg.getUser());
						if (authd == true) {
							User userIdUser = new User();
							userIdUser.setId(ppq.getUserID(msg.getUser().getEmail()));
							Message send = new Message("true", userIdUser);
							out.write(gson.toJson(send) + "\n");
							out.flush();
						} else {
							User userIdUser = new User();
							userIdUser.setId(-1);
							Message send = new Message("false", userIdUser);
							out.write(gson.toJson(send) + "\n");
							out.flush();
						}
						break;
					case "registration": 
						boolean registered = query.insertUser(msg.getUser());
						if (registered == true) {
							out.write("true" + "\n");
							out.flush();
						} else {
							out.write("false" + "\n");
							out.flush();
						}
						break;
					case "profilepage":
						User sendUser = ppq.getProfileData(msg.getUser().getId());
						Message send = new Message("profilepage", sendUser);
						out.write(gson.toJson(send) + "\n");
						out.flush();
						break;
					}
				}
			}
			sock.close();
			//decIncomingConns();
		} catch (IOException e) {}
	}
	
	public static int getIncomingConns() { return incomingConns; } /** getIncomingConns(): @return incomingConns */
	public static void setIncomingConns(int n) { incomingConns = n; } /** setIncomingConns(): @set incomingConns */
	public static void incIncomingConns() { incomingConns++; } /** incIncomingConns(): @increment incomingConns */
	public static void decIncomingConns() { incomingConns--; } /** decIncomingConns(): @decrement incomingConns */
}
