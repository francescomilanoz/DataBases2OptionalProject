package it.polimi.db2.marketing.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.Answer;
import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.Question;
import it.polimi.db2.marketing.entities.User;

@Stateful
public class AnswerService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public AnswerService() {
	}

	public void createAnswer(String answer_text, User user, Question question, Product product) {
		Answer answer = new Answer(answer_text, user, question, product);
		
		em.persist(answer); // makes also question object managed via cascading
	}	

}
