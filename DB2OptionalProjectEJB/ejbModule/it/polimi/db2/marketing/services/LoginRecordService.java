package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.LoginRecord;
import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Stateless
public class LoginRecordService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public LoginRecordService() {
	}

	public void createLoginRecord(Date timestamp, User user) {
		LoginRecord loginRecord = new LoginRecord(timestamp, user);
		
		em.persist(loginRecord); // makes also the login record object managed via cascading
	}

	public List<LoginRecord> loadLoginRecordOfTheDay() {
		java.util.Date date=new java.util.Date();
		java.util.Date dt=new java.util.Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, -1);
		dt = c.getTime();
		
		List<LoginRecord> loginRecords = new ArrayList<>();
		loginRecords = em.createNamedQuery("LoginRecord.findByDate", LoginRecord.class).setParameter(1, dt).setParameter(2,  date)
				.getResultList();
		
		if(loginRecords.size() == 0) {
			return null;
		}
		
		return loginRecords;
	}
	
	
}
