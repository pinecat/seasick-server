/* Package */
package net.arch64.gofish.seasick.users;

/* Imports */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.arch64.gofish.seasick.core.Config;

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
	public boolean authUser(User partUser) {
		Statement stmt = null;
		ResultSet rs = null;
		
		if (emailExists(partUser.getEmail())) {
			try {
				stmt = conn.createStatement();
				String q = "select * from USERS where EMAIL like '" + partUser.getEmail() + "'";
				rs = stmt.executeQuery(q);
				
				User user = new User();
				rs.next();
				user.setUsername(rs.getString("USERNAME"));
				user.setFname(rs.getString("FNAME"));
				user.setLname(rs.getString("LNAME"));
				user.setEmail(rs.getString("EMAIL"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setEmNotify(rs.getBoolean("EMAIL_NOTIFY"));
				user.setRep(rs.getDouble("REPUTATION"));
				
				// TODO: write subroutine to check partUser password against user hashed password
				// TODO: write separate subroutine to query a single user, as opposed to putting it in authUser
					// as we will need to query a single user for other things
			} catch (SQLException e) {}
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

	/**
	 * getForumsPosts
	 * @return
	 * 
	 * Used to update the view for the app that shows the forum posts.
	 * Gives the POST_ID, USERNAME, Content of the post, and Rating of the post (upvotes or likes on the post)
	 */
	public ResultSet getForumPosts() {
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select f.POST_ID, u.USERNAME, f.Content, f.Rating from FORUMS f left join USERS u on f.USERS_ID = u.USERS_ID");
			while(rs.next()) {
				int POST_ID = rs.getInt("f.POST_ID");
				String USERNAME = rs.getString("u.USERNAME");
				String Content = rs.getString("f.Content");
				double Rating = rs.getDouble("f.Rating");
				System.out.printf("%d %s\n%s %.1f\n\n", POST_ID, USERNAME, Content, Rating);
			}
		}catch(SQLException e) {}
		return rs;
	}
	
	/**
	 * makeForumPost:
	 * 
	 * add a forum post to the forum table in the database
	 * the POST_ID is auto-incrementing in the database
	 * needs the UserID, the content, and the rating for the post itself
	 * the post rating should start at zero and be averaged out between the overall rating and how many people have rated it
	 */
	public void makeForumPost(int userID, String content) {
		Statement stmt=null;
		try {
			stmt=conn.createStatement();
			stmt.executeUpdate("insert into FORUMS (USERS_ID, CONTENT, RATING) values("+userID+", \""+content+"\","+0+")");
		}catch(SQLException e) {}
	}
}
