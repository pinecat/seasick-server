package net.arch64.gofish.seasick.forums;

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
	
	public ResultSet getProfileData() {
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select USERNAME, EMAIL, FNAME, LNAME, REPUTATION from USERS;");
			while(rs.next()) {
				String USERNAME = rs.getString("USERNAME");
				String EMAIL = rs.getString("EMAIL");
				String FNAME = rs.getString("FNAME");
				String LNAME = rs.getString("LNAME");
				double REPUTATION = rs.getDouble("REPUTATION");
				System.out.printf("%s\n%s %.1f\n\n", USERNAME, EMAIL, FNAME, LNAME, REPUTATION);
			}
		}catch(SQLException e) {}
		return rs;
	}
}
