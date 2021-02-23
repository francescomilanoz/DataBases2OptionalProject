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
		java.util.Date todayDate =new java.util.Date();
		java.util.Date tomorrowDate =new java.util.Date();
		Calendar todayCalendar = Calendar.getInstance(); 
		Calendar tomorrowCalendar = Calendar.getInstance(); 
		
		// today    
		// reset hour, minutes, seconds and millis
		todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
		todayCalendar.set(Calendar.MINUTE, 0);
		todayCalendar.set(Calendar.SECOND, 0);
		todayCalendar.set(Calendar.MILLISECOND, 0);

		// next day
		// reset hour, minutes, seconds and millis
		tomorrowCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tomorrowCalendar.set(Calendar.MINUTE, 0);
		tomorrowCalendar.set(Calendar.SECOND, 0);
		tomorrowCalendar.set(Calendar.MILLISECOND, 0);
		tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);
	
		
		todayDate = todayCalendar.getTime();
		tomorrowDate = tomorrowCalendar.getTime();
		
		System.out.println("date: " + todayDate + tomorrowDate);
		
		List<LoginRecord> loginRecords = new ArrayList<>();
		loginRecords = em.createNamedQuery("LoginRecord.findByDate", LoginRecord.class).setParameter(1, todayDate).setParameter(2,  tomorrowDate)
				.getResultList();
		
		if(loginRecords.size() == 0) {
			return null;
		}
		
		return loginRecords;
	}
	
	
}
