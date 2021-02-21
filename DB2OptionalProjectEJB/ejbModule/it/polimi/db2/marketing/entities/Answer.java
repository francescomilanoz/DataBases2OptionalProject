package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import javax.persistence.*;

//import java.util.List;

/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@Table(name = "answer", schema = "db_marketing_app")
@NamedQueries({})

public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int answer_id;

	private String answer_text;

	
	 @ManyToOne
	 @JoinColumn(name = "user_id")
	 private User user;
	 
	 @ManyToOne
	 @JoinColumn(name = "question_id")
	 private Question question;

	public Answer() {
	}
	
	public Answer(String answer_text, User user, Question question) {
		this.answer_text = answer_text;
		this.user = user;
		this.question = question;
	}

	public String getAnswer_text() {
		return answer_text;
	}

	public void setAnswer_text(String answer_text) {
		this.answer_text = answer_text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}



}