package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
//import java.util.List;

/**
 * The persistent class for the login record database table.
 * 
 */
@Entity
@Table(name = "login_record", schema = "db_marketing_app")
@NamedQueries({@NamedQuery(name = "LoginRecord.findByDate", query = "SELECT lr FROM LoginRecord lr WHERE lr.login_timestamp >= ?1 AND lr.login_timestamp < ?2 ORDER BY lr.login_timestamp DESC")})

public class LoginRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int login_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date login_timestamp;

	
	@ManyToOne @JoinColumn(name="user_id")
	private User user;
	


	public LoginRecord() {
	}



	public LoginRecord(Date timestamp, User user) {
		this.login_timestamp = timestamp;
		this.user = user;
	}

	public Date getLogin_timestamp() {
		return login_timestamp;
	}



	public void setLogin_timestamp(Date login_timestamp) {
		this.login_timestamp = login_timestamp;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}
	
	
}