/* Package */
package net.arch64.gofish.seasick.forums;

/* Imports */

/* Class: Forum */
public class Forum {
	private int		id;
	private int		userId;
	private String 	content;
	private String 	username;
	private double	rating;
	private double 	likes;
	private double 	dislikes;
	
	/**
	 * Forum:
	 * 
	 * Default constructor for the
	 * forum class.
	 */
	public Forum() {
		
	}
	
	public int getId() { return id; }
	public int getUserId() { return userId; }
	public String getContent() { return content; }
	public String getUsername() { return username; }
	public double getRating() { return rating; }
	public double getLikes() { return likes; }
	public double getDislikes() { return dislikes; }
	
	public void setId(int id) { this.id = id; }
	public void setUserId(int userId) { this.userId = userId; }
	public void setContent(String content) { this.content = content; }
	public void setUsername(String username) { this.username = username; }
	public void setLikes(double likes) { this.likes = likes; }
	public void setDislikes(double dislikes) { this.dislikes = dislikes; }
	public void setRating() { 
		if(likes != 0 && (likes+dislikes) != 0)
			this.rating = getLikes()/(getLikes()+getDislikes());
		else
			this.rating = 0.0;
	}
}
