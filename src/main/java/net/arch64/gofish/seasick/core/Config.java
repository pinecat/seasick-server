/* Package */
package net.arch64.gofish.seasick.core;

/* Imports */
import java.io.*;
import java.util.Scanner;

/* Class: Config */
public class Config {
	private String dbServer;	// URL of the database server (i.e. db.domain.com)
	private String port;		// Port of the database server
	private String driver;		// Driver for the database (in this case, jdbc:mysql)
	private String db;			// Name of the database to connect to
	private String user;		// Username for the database login
	private String pass;		// Password for the database login
	
	/**
	 * Config:
	 * @param filepath
	 * 
	 * Constructor for the Config class.
	 * Calls the private update function which attempts
	 * to update all private members of the Config class.
	 */
	public Config(String filepath) {
		update(filepath);
	}
	
	/**
	 * update:
	 * @param filepath
	 * 
	 * Attempts to update all private members
	 * of the Config class. This is only ever called
	 * from the constructor. Could possibly be made
	 * public if we want to call it from elsewhere.
	 */
	private void update(String filepath) {
		File file = new File(filepath); // open the file
		try {
			Scanner in = new Scanner(file); // create scanner to read file
			
			while (in.hasNextLine()) { // while the file still has lines
				String[] split = in.nextLine().split("="); // split on the equals sign
				if (split.length < 2) { return; } // length of split should always be 2 (config option & config data), so return if this value is less than 2
				switch(split[0]) { // switch on the config option, and store the config data appropriately
				case "dbServer": dbServer = split[1]; break;	// case for dbServer URL
				case "port": port = split[1]; break;			// case for port
				case "driver": driver = split[1]; break;		// case for driver
				case "db": db = split[1]; break;				// case for db (database name)
				case "user": user = split[1]; break;			// case for username for login
				case "pass": pass = split[1]; break;			// case for password for login
				}
			}
		} catch (FileNotFoundException e) {}
	}
	
	public String getDbServer() { return dbServer; }	/** getDbServer: @return dbServer */
	public String getPort() { return port; }			/** getPort: @return port */
	public String getDriver() { return driver; }		/** getDriver: @return driver */
	public String getDb() { return db; }				/** getDb: @return db */
	public String getUser() { return user; }			/** getUser: @return user */
	public String getPass() { return pass; }			/** getPass: @return pass */
	
	/**
	 * getFullUrl:
	 * @return the URL needed for a database connection
	 * 
	 * Uses private members to form the full, necessary
	 * connection URL string for a database connection.
	 */
	public String getFullUrl() {
		return (driver + "://" + dbServer + ":" + port + "/" + db);
	}
}