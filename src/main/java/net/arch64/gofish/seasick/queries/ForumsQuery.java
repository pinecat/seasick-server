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
import net.arch64.gofish.seasick.serve.ForumRequest;
import net.arch64.gofish.seasick.users.User;

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

	public ForumRequest getForumPosts(String countryCode, String region, String locale) {
		ForumRequest forumReq = new ForumRequest();
		Forum forum = null;
		Vote vote = null;
		ArrayList<Forum> list = new ArrayList<>();
		ArrayList<Vote> votes = new ArrayList<>();
		PreparedStatement stmt=null;
		ResultSet rs=null;
		int count = 0;
		try {
			stmt=conn.prepareStatement("select * from GOFISH.FORUMS f " + 
					"left join GOFISH.USERS u " + 
					"on f.USERS_ID = u.USERS_ID " + 
					"left join GOFISH.FORUM_RATINGS r " + 
					"on f.POST_ID = r.POST_ID");
			//stmt=conn.prepareStatement("select u.USERS_ID, u.USERNAME, f.POST_ID, f.Content, f.LIKES, f.DISLIKES, f.LOCALE, f.REGION, f.COUNTRY_CODE, f.TIME_STAMP from FORUMS f left join USERS u on f.USERS_ID = u.USERS_ID");
			rs=stmt.executeQuery();
			rs.last();
			do {
				forum = new Forum();
				vote = new Vote();
				
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
				
				User user = new User();
				int USERS_ID = rs.getInt("r.USER_ID");
				int rating = rs.getInt("r.POST_RATING");
				
				user.setId(USERS_ID);
				vote.setPostId(POST_ID);
				vote.setUser(user);
				vote.setVote(rating);
				votes.add(vote);
				
				if (countryCode != null && region != null && locale != null) {
					if (countryCode.equals(COUNTRY_CODE) && region.equals(REGION) && locale.equals(LOCALE)) {
						list.add(forum);
					}
				}
				
				count++;
			}while(rs.previous() && count < 50);
		}catch(SQLException e) {}
		
		forumReq.setList(list);
		forumReq.setVotes(votes);
		return forumReq;
	}
	
	/**
	 * makeForumPost:
	 * 
	 * add a forum post to the forum table in the database
	 * the POST_ID is auto-incrementing in the database
	 * needs the UserID, the content, and the rating for the post itself
	 * the post rating should start at zero and be averaged out between the overall rating and how many people have rated it
	 */
	public void makeForumPost(int userID, String content, String countryCode, String region, String locale) {
		PreparedStatement stmt=null;
		try {
			
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = dateFormat.format(date);
			
			stmt=conn.prepareStatement("insert into FORUMS(USERS_ID, CONTENT, RATING, LIKES, DISLIKES, LOCALE, REGION, COUNTRY_CODE, TIME_STAMP)"
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1,  userID);
			stmt.setString(2, content);
			stmt.setDouble(3,  0);
			stmt.setDouble(4, 0);
			stmt.setDouble(5, 0);
			stmt.setString(6, locale);
			stmt.setString(7, region);
			stmt.setString(8, countryCode);
			stmt.setString(9, dateString);
			
			stmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println("Dumb");
		}
	}

	/**
	 * incrementLikes:
	 * 
	 * Will increment the values of LIKES
	 * @param post
	 */
	public void incrementLikes(Vote vote) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update FORUMS set LIKES = LIKES + 1 where POST_ID = ?");
			stmt.setInt(1, vote.getPostId());
			stmt.executeUpdate();
		}catch(SQLException e) {}
	}
	
	public void decrementLikes(Vote vote) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update FORUMS set LIKES = LIKES - 1 where POST_ID = ?");
			stmt.setInt(1, vote.getPostId());
			stmt.executeUpdate();
		} catch (SQLException e) {}
	}
	
	/**
	 * incrementDislikes:
	 * 
	 * Will increment only the value of DISLIKES by 1
	 * @param post
	 */
	public void incrementDislikes(Vote vote) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update FORUMS set DISLIKES = DISLIKES + 1 where POST_ID = ?");
			stmt.setInt(1, vote.getPostId());
			stmt.executeUpdate();
		}catch(SQLException e) {}
	}
	
	public void decrementDislikes(Vote vote) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("update FORUMS set DISLIKES = DISLIKES - 1 where POST_ID = ?");
			stmt.setInt(1, vote.getPostId());
			stmt.executeUpdate();
		} catch (SQLException e) {}
	}
	
	public void updateForumRatings(String action, Vote vote) {
		PreparedStatement stmt = null;
		if (action.equals("upvote")) {
			try {
				stmt = conn.prepareStatement("insert into FORUM_RATINGS (POST_ID, USER_ID, POST_RATING) values (?, ?, ?)");
				stmt.setInt(1, vote.getPostId());
				stmt.setInt(2, vote.getUser().getId());
				stmt.setInt(3, vote.getVote());
				stmt.executeUpdate();
			} catch (SQLException e) {}
		} else if (action.equals("deupvote")) {
			try {
				stmt = conn.prepareStatement("delete from FORUM_RATINGS where POST_ID = ? and USER_ID = ?");
				stmt.setInt(1, vote.getPostId());
				stmt.setInt(2, vote.getUser().getId());
				stmt.executeUpdate();
			} catch (SQLException e) {}
		} else if (action.equals("downvote")) {
			try {
				stmt = conn.prepareStatement("insert into FORUM_RATINGS (POST_ID, USER_ID, POST_RATING) values (?, ?, ?)");
				stmt.setInt(1, vote.getPostId());
				stmt.setInt(2, vote.getUser().getId());
				stmt.setInt(3, vote.getVote());
				stmt.executeUpdate();
			} catch (SQLException e) {}
		} else if (action.equals("dedownvote")) {
			try {
				stmt = conn.prepareStatement("delete from FORUM_RATINGS where POST_ID = ? and USER_ID = ?");
				stmt.setInt(1, vote.getPostId());
				stmt.setInt(2, vote.getUser().getId());
				stmt.executeUpdate();
			} catch (SQLException e) {}
		}
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
