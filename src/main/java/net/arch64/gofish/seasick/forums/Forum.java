/* Package */
package net.arch64.gofish.seasick.forums;

/* Imports */

/* Class: Forum */
public class Forum {
	private int		id;
	private int		userId;
	private String 	content;
	private double	rating;
	
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
	public double getRating() { return rating; }
	
	public void setId(int id) { this.id = id; }
	public void setUserId(int userId) { this.userId = userId; }
	public void setContent(String content) { this.content = content; }
	public void setRating(double rating) { this.rating = rating; }
}
