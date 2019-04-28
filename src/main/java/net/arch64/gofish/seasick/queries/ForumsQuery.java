package net.arch64.gofish.seasick.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.arch64.gofish.seasick.core.Config;
import net.arch64.gofish.seasick.forums.*;

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
	 * Puts each forum post into an ArrayList to return to the front-end to be displayed
	 */
	public ArrayList<Forum> getForumPosts() {
		Forum forum = null;
		ArrayList<Forum> list = new ArrayList<>();
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.prepareStatement("select u.USERNAME, f.POST_ID, f.Content, f.LIKES, f.DISLIKES, f.LOCALE, f.REGION, f.COUNTRY_CODE, f.TIME_STAMP from FORUMS f left join USERS u on f.USERS_ID = u.USERS_ID");
			rs=stmt.executeQuery();
			rs.last();
			do {
				forum = new Forum();
				
				int POST_ID = rs.getInt("f.POST_ID");
				
				String USERNAME = rs.getString("u.USERNAME");
				String CONTENT = rs.getString("f.Content");
				String LOCALE = rs.getString("LOCALE");
				String REGION = rs.getString("REGION");
				String COUNTRY_CODE = rs.getString("COUNTRY_CODE");
				String DATE_TIME = rs.getString("TIME_STAMP");
				
				int LIKES = (int)rs.getDouble("LIKES");
				int DISLIKES = (int)rs.getDouble("DISLIKES");
			
				forum.setId(POST_ID);		

				forum.setContent(CONTENT);
				forum.setUsername(USERNAME);
				forum.setLocale(LOCALE);
				forum.setRegion(REGION);
				forum.setCountryCode(COUNTRY_CODE);
				forum.setTimeStamp(DATE_TIME);
				
				forum.setLikes(LIKES);
				forum.setDislikes(DISLIKES);
				forum.setRating();
				
				list.add(forum);
			}while(rs.previous());
		}catch(SQLException e) {}

		return list;
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
		PreparedStatement stmt=null;
		try {
			
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = dateFormat.format(date);
			
			stmt=conn.prepareStatement("insert into FORUMS(USERS_ID, CONTENT, RATING, LIKES, DISLIKES, TIME_STAMP)"
					+ "values(?, ?, ?, ?, ?, ?)");
			stmt.setInt(1,  userID);
			stmt.setString(2, content);
			stmt.setDouble(3,  0);
			stmt.setDouble(4, 0);
			stmt.setDouble(5, 0);
			stmt.setString(6, dateString);
			
			stmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println("Dumb");
		}
	}

	/**
	 * deletePost:
	 * 
	 * runs a preparedStatement to delete a post with the corresponding POST_ID
	 * @param post
	 */
	public void deletePost(Forum post) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("delete from FORUMS where POST_ID = ?");
			stmt.setInt(1,  post.getId());
			stmt.executeUpdate();
		}catch(SQLException e) {}
	}
	
	
	/**
	 * incrementLikes:
	 * 
	 * Will increment the values of LIKES
	 * @param post
	 */
	public void incrementLikes(Forum post) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update FORUMS set LIKES = LIKES + 1 where POST_ID = ?");
			stmt.setInt(1, post.getId());
			stmt.executeUpdate();
		}catch(SQLException e) {}
	}
	
	/**
	 * incrementDislikes:
	 * 
	 * Will increment only the value of DISLIKES by 1
	 * @param post
	 */
	public void incrementDislikes(Forum post) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update FORUMS set DISLIKES = DISLIKES + 1 where POST_ID = ?");
			stmt.setInt(1, post.getId());
			stmt.executeUpdate();
		}catch(SQLException e) {}
	}
	
	/**
	 * updatePostRating:
	 * 
	 * updates the overall rating of the post using a ratio of like:totalVotes on the post
	 * @param post
	 */
	public void updatePostRating(Forum post) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("select RATING, LIKES, DISLIKES from FORUMS where POST_ID = ?");
			stmt.setInt(1,  post.getId());
			rs = stmt.executeQuery();
			while(rs.next()) {
				double LIKES = rs.getDouble("LIKES");
				double DISLIKES = rs.getDouble("DISLIKES");
				
				post.setLikes(LIKES);
				post.setDislikes(DISLIKES);
				post.setRating();
			}
			stmt.executeUpdate("update FORUMS set RATING = " + post.getRating() + " where POST_ID = " + post.getId());
		}catch(SQLException e) {}
	}
}
