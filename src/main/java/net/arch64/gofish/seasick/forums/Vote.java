package net.arch64.gofish.seasick.forums;

import net.arch64.gofish.seasick.users.User;

public class Vote {
	private User user;
	private int postId;
	private int vote;
	
	public Vote() {
		
	}
	
	public User getUser() { return user; }
	public int getPostId() { return postId; }
	public int getVote() { return vote; }
	
	public void setUser(User user) { this.user = user; }
	public void setPostId(int postId) { this.postId = postId; }
	public void setVote(int vote) { this.vote = vote; }
}
