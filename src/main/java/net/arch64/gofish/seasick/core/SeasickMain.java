/********************************************************
 * Seasick Server
 * Server-side comms link for Go Fish App
 * Green Team
 ********************************************************/

/* Package */
package net.arch64.gofish.seasick.core;

/* Imports */
import java.sql.*;
import net.arch64.gofish.seasick.users.*;
import net.arch64.gofish.seasick.queries.*;
import net.arch64.gofish.seasick.serve.*;

/* Class: Main */
public class SeasickMain {
	
	/**
	 * main:
	 * @param args
	 *
	 * Main function of program.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws SQLException {
		Config conf = new Config("seasick.conf");
		Query query = new Query(conf);
		
		/* server testing */
		System.out.println("Server started listening on port 12345....");
		Server serve = new Server(12345, 1000);
		serve.start();
	}
}
