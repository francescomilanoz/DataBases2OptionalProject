package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.LoginRecord;
import it.polimi.db2.marketing.entities.User;

import java.util.Date;

@Stateless
public class LoginRecordService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public LoginRecordService() {
	}

	public void createLoginRecord(Date timestamp, User user) {
		LoginRecord loginRecord = new LoginRecord(timestamp, user);
		
		em.persist(loginRecord); // makes also the loginrecord object managed via cascading
	}
	
	
}
