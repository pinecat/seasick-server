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
<<<<<<< HEAD
		/* database testing */
=======
		/* User insertion and deletion testing */
>>>>>>> e9aa7abbee10b5335b9553b0bbd59e507aa84613
		/*
		Config conf = new Config("seasick.conf");
		Query query = new Query(conf);
		User user = new User("testty", "idklol", "gimme@gimme.net", "Hallo", "vad", false);
		query.insertUser(user);
		query.dropUser(user);
		query.close();
		*/
		
<<<<<<< HEAD
		/* server testing */
		Server serve = new Server(12345, 1000);
		serve.start();
=======
		
>>>>>>> e9aa7abbee10b5335b9553b0bbd59e507aa84613
	}
}
