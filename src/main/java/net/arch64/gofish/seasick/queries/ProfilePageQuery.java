package net.arch64.gofish.seasick.queries;

import net.arch64.gofish.seasick.users.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.arch64.gofish.seasick.core.Config;

public class ProfilePageQuery {
	private Connection conn;

	/**
	 * Query:
	 * @param conf
	 *
	 * Constructor for the Query class.
	 */
	public ProfilePageQuery(Config conf) {
		try {
			conn = DriverManager.getConnection(conf.getFullUrl(), conf.getUser(), conf.getPass());
		} catch (SQLException e) {}
	}
	
	public ProfilePageQuery(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * getProfileData:
	 * 
	 * queries the database with the userID and returns a user class that contains the username, email, firstName, and lastName
	 * @param userID
	 * @return
	 */
	public User getProfileData(int userID) {
		User user = new User();
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.prepareStatement("select USERNAME, EMAIL, FNAME, LNAME, REPUTATION from USERS where USERS_ID = ?");
			stmt.setInt(1, userID);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String USERNAME = rs.getString("USERNAME");
				String EMAIL = rs.getString("EMAIL");
				String FNAME = rs.getString("FNAME");
				String LNAME = rs.getString("LNAME");
				double REPUTATION = rs.getDouble("REPUTATION");
				user.setUsername(USERNAME);
				user.setEmail(EMAIL);
				user.setFname(FNAME);
				user.setLname(LNAME);
				user.setRep(REPUTATION);
			}
		}catch(SQLException e) {}
		return user;
	}
	
	/**
	 * getUserAuthData:
	 * @param id
	 * @return
	 * 
	 * Queries database with user id and returns user class with username, password, email.
	 * Primarily used to authentication.
	 */
	public User getUserAuthData(int id) {
		User user = new User();
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.prepareStatement("select USERNAME, PASSWORD, EMAIL from USERS where USERS_ID = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String USERNAME = rs.getString("USERNAME");
				String PASSWORD = rs.getString("PASSWORD");
				String EMAIL = rs.getString("EMAIL");
				user.setUsername(USERNAME);
				user.setPassword(PASSWORD);
				user.setEmail(EMAIL);
			}
		}catch(SQLException e) {}
		return user;
	}
	
	/**
	 * getUserID:
	 * 
	 * takes in a user's email and checks the database for that email and returns the corresponding userID
	 * @param EMAIL
	 * @return
	 */
	public int getUserID(String EMAIL) {
		PreparedStatement stmt=null;
		ResultSet rs=null;
		int userID=0;
		try {
			stmt=conn.prepareStatement("select USERS_ID from USERS where EMAIL like ?");
			stmt.setString(1, EMAIL);
			rs=stmt.executeQuery();
			if(rs.next()) 
				userID = rs.getInt("USERS_ID");
			else
				System.out.println("Invalid email");
		}catch(SQLException e) {}
		return userID;
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
}
