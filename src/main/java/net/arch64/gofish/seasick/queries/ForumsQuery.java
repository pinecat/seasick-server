package net.arch64.gofish.seasick.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select f.POST_ID, u.USERNAME, f.Content, f.LIKES, f.DISLIKES from FORUMS f left join USERS u on f.USERS_ID = u.USERS_ID");
			while(rs.next()) {
				forum = new Forum();
				int POST_ID = rs.getInt("f.POST_ID");
				String USERNAME = rs.getString("u.USERNAME");
				String CONTENT = rs.getString("f.Content");
				int LIKES = (int)rs.getDouble("LIKES");
				int DISLIKES = (int)rs.getDouble("DISLIKES");
			
				forum.setContent(CONTENT);
				forum.setUsername(USERNAME);
				forum.setId(POST_ID);		
				forum.setLikes(LIKES);
				forum.setDislikes(DISLIKES);
				forum.setRating();
				
				list.add(forum);
			}
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
		Statement stmt=null;
		try {
			stmt=conn.createStatement();
			stmt.executeUpdate("insert into FORUMS (USERS_ID, CONTENT, RATING) values("+userID+", \""+content+"\","+0+")");
		}catch(SQLException e) {}
	}

	/**
	 * incrementLikes:
	 * 
	 * Will increment the values of LIKES
	 * @param post
	 */
	public void incrementLikes(Forum post) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("update FORUMS set LIKES = LIKES + 1 where POST_ID = " + post.getId());
		}catch(SQLException e) {}
	}
	
	/**
	 * incrementDislikes:
	 * 
	 * Will increment only the value of DISLIKES by 1
	 * @param post
	 */
	public void incrementDislikes(Forum post) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("update FORUMS set DISLIKES = DISLIKES + 1 where POST_ID = " + post.getId());
		}catch(SQLException e) {}
	}
	
	/**
	 * updatePostRating:
	 * 
	 * updates the overall rating of the post using a ratio of like:totalVotes on the post
	 * @param post
	 */
	public void updatePostRating(Forum post) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select RATING, LIKES, DISLIKES from FORUMS where POST_ID = " + post.getId());
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
