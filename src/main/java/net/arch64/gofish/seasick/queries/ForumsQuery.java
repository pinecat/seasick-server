package net.arch64.gofish.seasick.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.arch64.gofish.seasick.core.Config;

public class ForumsQuery {
	private Connection conn;

	/**
	 * Query:
	 * @param conf
	 *
	 * Constructor for the Query class.
	 */
	public ForumsQuery(Config conf) {
		try {
			conn = DriverManager.getConnection(conf.getFullUrl(), conf.getUser(), conf.getPass());
		} catch (SQLException e) {}
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
				//System.out.printf("%d %s\n%s %.1f\n\n", POST_ID, USERNAME, Content, Rating);
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
