package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.Answer;
import it.polimi.db2.marketing.entities.Question;
import it.polimi.db2.marketing.entities.User;

@Stateless
public class AnswerService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public AnswerService() {
	}

	public void createAnswer(String answer_text, User user, Question question) {
		Answer answer = new Answer(answer_text, user, question);
		
		em.persist(answer); // makes also question object managed via cascading
	}	
}
