package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
//import java.util.List;

/**
 * The persistent class for the usertable database table.
 * 
 */
@Entity
@Table(name = "login_record", schema = "db_marketing_app")
@NamedQueries({})

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

	
	
}