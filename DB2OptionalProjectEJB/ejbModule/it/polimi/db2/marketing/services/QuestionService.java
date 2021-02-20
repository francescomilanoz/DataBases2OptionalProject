package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.joda.time.LocalDateTime;

import javax.persistence.NonUniqueResultException;

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
public class QuestionService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public QuestionService() {
	}

	public void createQuestion(String question_text, QuestionType question_type, Product product) {
		Question question = new Question(question_text, question_type, product);
		
		em.persist(question); // makes also question object managed via cascading
	}
	
	public List<Question> loadQuestionsByProductAndType(Product product, QuestionType questionType) {
		List<Question> questions = new ArrayList<>();
		questions = em.createNamedQuery("Question.findByProductAndType", Question.class).setParameter(1, product).setParameter(2, questionType)
				.getResultList();

		return questions;
	}
	
	
}
