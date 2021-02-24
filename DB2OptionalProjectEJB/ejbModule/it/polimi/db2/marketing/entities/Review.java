package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import javax.persistence.*;

//import java.util.List;

/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@Table(name = "review", schema = "db_marketing_app")
@NamedQueries({@NamedQuery(name = "Review.findByProduct", query = "SELECT r FROM Review r WHERE r.product= ?1")})

public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int review_id;

	private String review_text;

	
	 @ManyToOne
	 @JoinColumn(name = "user_id")
	 private User user;
	 
	 @ManyToOne
	 @JoinColumn(name = "product_id")
	 private Product product;

	public Review() {
	}
	
	public Review(String review_text, User user, Product product) {
		this.review_text = review_text;
		this.user = user;
		this.product = product;
	}


	public String getReview_text() {
		return review_text;
	}

	public void setReview_text(String review_text) {
		this.review_text = review_text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getReview_id() {
		return review_id;
	}

	public void setReview_id(int review_id) {
		this.review_id = review_id;
	}
	
	

	
}