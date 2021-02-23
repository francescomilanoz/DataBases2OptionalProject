package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.*;
//import java.util.List;

/**
 * The persistent class for the usertable database table.
 * 
 */
@Entity
@Table(name = "user", schema = "db_marketing_app")
@NamedQueries({@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"), 
			   @NamedQuery(name = "User.checkUsernameUniqueness", query = "SELECT r FROM User r  WHERE r.username = ?1"),
			   @NamedQuery(name = "User.checkEmailUniqueness", query = "SELECT r FROM User r  WHERE r.email = ?1"), 
			   @NamedQuery(name = "User.blockUser", query = "UPDATE User u SET u.active = False WHERE u.username = ?1")})

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;

	private String email;

	private String password;

	private boolean active;

	private String username;
	
	private int points;
	
	private String type;
	

	/*
	@OneToMany(mappedBy="user") //When a user account is deleted, the relative login records still remain in the database. 
	private ArrayList<LoginRecord> loginRecords;
	*/

	public User() {
	}
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.type = "User";
		this.points = 0;
		this.active = true;
	}

	public int getId() {
		return this.user_id;
	}

	public void setId(int id) {
		this.user_id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}