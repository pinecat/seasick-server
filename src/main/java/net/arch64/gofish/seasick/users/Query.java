/* Package */
package net.arch64.gofish.seasick.users;

/* Imports */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

import net.arch64.gofish.seasick.core.Config;
import net.arch64.gofish.seasick.queries.ProfilePageQuery;

/* Class: Query */
public class Query {
	private Connection conn;

	/**
	 * Query:
	 * @param conf
	 *
	 * Constructor for the Query class.
	 */
	public Query(Config conf) {
		try {
			conn = DriverManager.getConnection(conf.getFullUrl(), conf.getUser(), conf.getPass());
		} catch (SQLException e) {}
	}

	/**
	 * getAllUsers:
	 * @return rs - the result set from the query
	 *
	 * Returns the result set for the SQL Command:
	 *
	 * 		SELECT * FROM USERS
	 *
	 * Then returns the result set.
	 */
	public ResultSet getAllUsers() {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from USERS");
		} catch (SQLException e) {}
		return rs;
	}
	
	/**
	 * authUser:
	 * @param user - the user to be authenticated
	 * @return true - if the user is successfully authenticated
	 * 		   false - if the user is not authenticated
	 * 
	 * Attempts to authenticate a user.
	 */
	public boolean authUser(User authReqUser) {
		String email = authReqUser.getEmail();
		String pass = authReqUser.getPassword();
		
		ProfilePageQuery ppq = new ProfilePageQuery(conn);
		int id = ppq.getUserID(email);
		User user = ppq.getUserAuthData(id);
		
		if (BCrypt.checkpw(pass, user.getPassword())) {
			return true;
		}
		
		return false;
	}

	/**
	 * insertUser:
	 * @param user
	 *
	 * Inserts new user into MySQL database.
	 */
	public void insertUser(User user) {
		Statement stmt = null; // create statement variable for use later for the query

		/*
		 * q, is the actual SQL statement to be executed.
		 * This will be executed if no Fname or Fname and Lname is provided
		 */

		if(!userExists(user.getUsername()) && !emailExists(user.getEmail())) {

			String q = "insert into USERS (" +
					"USERNAME, PASSWORD, EMAIL, EMAIL_NOTIFY" +
					") values ('" +
					user.getUsername() + "', '" +
					user.getHashedPass() + "', '" +
					user.getEmail() + "', " +
					user.getEmNotify() + ");";

			/*
			 * This will be executed if both Fname and Lname are provided
			 */
			if (user.getFname() != "" && user.getLname() != "") {
				q = "insert into USERS (" +
						"USERNAME, PASSWORD, EMAIL, FNAME, LNAME, EMAIL_NOTIFY" +
						") values ('" +
						user.getUsername() + "', '" +
						user.getHashedPass() + "', '" +
						user.getEmail() + "', '" +
						user.getFname() + "', '" +
						user.getLname() + "', " +
						user.getEmNotify() + ");";

				/*
				 * This will be executed if just Fname is provided (no Lname)
				 */
			} else if (user.getFname() != "") {
				q = "insert into USERS (" +
						"USERNAME, PASSWORD, EMAIL, FNAME, EMAIL_NOTIFY" +
						") values ('" +
						user.getUsername() + "', '" +
						user.getHashedPass() + "', '" +
						user.getEmail() + "', '" +
						user.getFname() + "', " +
						user.getEmNotify() + ");";
			}

			try {
				stmt = conn.createStatement(); // create statement on the connection
				stmt.executeUpdate(q); // execute the update q
			} catch (SQLException e) {}
		}
	}

	/**
	 * dropUser:
	 * @param user
	 *
	 * Drops specified user from the MySQL database.
	 */
	public void dropUser(User user) {
		Statement stmt = null;
		String q = "delete from USERS where USERNAME = '" + user.getUsername() + "';";

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(q);
		} catch (SQLException e) {}
	}

	/**
	 * close:
	 *
	 * Close the connection to the database.
	 */
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {}
	}

	/**
	 * userExists:
	 * @param username
	 * @return
	 *
	 * Checks to see if a username has already been registered to an account
	 */
	private boolean userExists(String username) {
		try {
			PreparedStatement st = conn.prepareStatement("select * from USERS where USERNAME like '" + username + "'");
			ResultSet result = st.executeQuery();
			if(result.next()) {
				//System.out.println("Username already exists.");
				return true;
			}
		} catch (SQLException e) {}
		return false;
	}

	/**
	 * emailExists:
	 * @param email
	 * @return
	 *
	 * Checks to see if an email has already been registered to an account
	 */
	private boolean emailExists(String email) {
		try {
			PreparedStatement st=conn.prepareStatement("select * from USERS where EMAIL like '" + email + "'");
			ResultSet result = st.executeQuery();
			if(result.next()) {
				//System.out.println("Email already exists");
				return true;
			}
		} catch(SQLException e) {}
		return false;
	}
}
