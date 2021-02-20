package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.joda.time.LocalDateTime;

import javax.persistence.NonUniqueResultException;

import it.polimi.db2.marketing.entities.LoginRecord;
import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.Question;
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.exceptions.*;
import it.polimi.db2.marketing.utils.QuestionType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		
		em.persist(loginRecord); // makes also the loginrecord object managed via cascading
	}
	
	
}
