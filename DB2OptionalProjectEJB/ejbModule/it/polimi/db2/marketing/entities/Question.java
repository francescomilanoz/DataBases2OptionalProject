package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import it.polimi.db2.marketing.utils.QuestionType;
//import java.util.List;

/**
 * The persistent class for the usertable database table.
 * 
 */
@Entity
@Table(name = "question", schema = "db_marketing_app")
@NamedQueries({@NamedQuery(name = "Question.findByProductAndType", query = "SELECT q FROM Question q WHERE q.product = ?1 AND q.question_type = ?2")})

public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int question_id;

	private String question_text;

	private QuestionType question_type;
	
	@OneToMany(mappedBy="question", cascade = CascadeType.REMOVE, orphanRemoval=true) 
	private List<Answer> answers;
	
	 @ManyToOne
	 @JoinColumn(name = "product_id")
	 private Product product;

	public Question() {
	}
	
	public Question(String question_text, QuestionType question_type, Product product) {
		this.setQuestion_text(question_text);
		this.setQuestion_type(question_type);
		this.product = product;
	}

	public String getQuestion_text() {
		return question_text;
	}

	public void setQuestion_text(String question_text) {
		this.question_text = question_text;
	}

	public QuestionType getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(QuestionType question_type) {
		this.question_type = question_type;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	


}