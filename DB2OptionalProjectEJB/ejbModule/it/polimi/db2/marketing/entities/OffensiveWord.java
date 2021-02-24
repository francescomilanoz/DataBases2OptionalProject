package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import javax.persistence.*;

//import java.util.List;

/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@Table(name = "offensive_words", schema = "db_marketing_app")
@NamedQueries({@NamedQuery(name = "OffensiveWord.findByText", query = "SELECT o FROM OffensiveWord o WHERE o.offensive_word_text = ?1")})

//SELECT o FROM OffensiveWord o WHERE ?1 = CONCAT('%', o.offensive_word_text, '%')
//"SELECT o FROM OffensiveWord o WHERE o.offensive_word_text = ?1"

public class OffensiveWord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int offensive_word_id;

	private String offensive_word_text;

	public OffensiveWord() {
	}
	
	public OffensiveWord(String offensive_word_text) {
		this.setOffensive_word_text(offensive_word_text);
	}

	public String getOffensive_word_text() {
		return offensive_word_text;
	}

	public void setOffensive_word_text(String offensive_word_text) {
		this.offensive_word_text = offensive_word_text;
	}

	public int getOffensive_word_id() {
		return offensive_word_id;
	}

	public void setOffensive_word_id(int offensive_word_id) {
		this.offensive_word_id = offensive_word_id;
	}
	
	

	
}