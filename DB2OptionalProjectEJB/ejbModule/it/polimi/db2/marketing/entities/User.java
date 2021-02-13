package it.polimi.db2.marketing.entities;

import java.io.Serializable;
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
			   @NamedQuery(name = "User.checkEmailUniqueness", query = "SELECT r FROM User r  WHERE r.email = ?1")})

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;

	private String password;

	private boolean active;

	private String username;
	
	private int points;
	
	private String type;

	// Bidirectional many-to-one association to Mission
	/*
	 * Fetch type EAGER allows resorting the relationship list content also in the
	 * client Web servlet after the creation of a new mission. If you leave the
	 * default LAZY policy, the relationship is sorted only at the first access but
	 * then adding a new mission does not trigger the reloading of data from the
	 * database and thus the sort method in the client does not actually re-sort the
	 * list of missions. MERGE is not cascaded because we will modify and merge only
	 * username and surname attributes of the user and do not want to cascade
	 * detached changes to relationship.
	 */
	/*@OneToMany(fetch = FetchType.EAGER, mappedBy = "reporter", cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.REFRESH })
	@OrderBy("date DESC")
	private List<Mission> missions;*/

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
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	/*public List<Mission> getMissions() {
		return this.missions;
	}

	public void addMission(Mission mission) {
		getMissions().add(mission);
		mission.setReporter(this);
		// aligns both sides of the relationship
		// if mission is new, invoking persist() on user cascades also to mission
	}

	public void removeMission(Mission mission) {
		getMissions().remove(mission);
	}*/

}