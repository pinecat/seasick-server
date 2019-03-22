/* Package */
package net.arch64.gofish.seasick.users;

/* Imports */
import org.mindrot.jbcrypt.BCrypt;

/* Class: User */
public class User {
	private int 	id;
	private String 	username;
	private String	password;
	private String	email;
	private String	fname;
	private String	lname;
	private boolean	em_notify;
	private double	rep;

	/**
	 * User:
	 * @param username
	 *
	 * Constructor for the User class. This is used
	 * for searching for users by username.
	 */
	public User(String username) {
		this.username = username;
	}

	/**
	 * User:
	 * @param id
	 *
	 * Constructor for the User class. This is used
	 * for searching for users by id.
	 */
	public User(int id) {
		this.id = id;
	}
	
	/**
	 * User:
	 * @param username
	 * @param password
	 * @param email
	 * @param fname
	 * @param lname
	 * @param em_notify
	 *
	 * Constructor for the User class. This is used
	 * to insert users who add their firstname and
	 * their lastname.
	 */
	public User(String username, String password, String email, String fname, String lname, boolean em_notify) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.fname = fname;
		this.lname = lname;
		this.em_notify = em_notify;
	}
	
	/**
	 * getHashedPass:
	 * @param password
	 * @return hashedPass - the encrypted password
	 * 
	 * Takes the user's password and hashes it using BCrypt
	 */
	public String getHashedPass() {
		String hashedPass = BCrypt.hashpw(this.password, BCrypt.gensalt());
		return hashedPass;
	}
	
	public int getId() { return id; }					/** getId: @return id */
	public String getUsername() { return username; }	/** getUsername: @return username */
	public String getPassword() { return password; }	/** getPassword: @return password */
	public String getEmail() { return email; }			/** getEmail: @return email */
	public String getFname() { return fname; }			/** getFname: @return fname */
	public String getLname() { return lname; }			/** getLname: @return lname */
	public boolean getEmNotify() { return em_notify; }	/** getEmNotify: @return em_notify */
	public double getRep() { return rep; }				/** getRep: @return rep */
}