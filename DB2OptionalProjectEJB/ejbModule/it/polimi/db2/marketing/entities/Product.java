package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
//import java.util.List;

/**
 * The persistent class for the usertable database table.
 * 
 */
@Entity
@Table(name = "product", schema = "db_marketing_app")
@NamedQueries({@NamedQuery(name = "Product.findByDate", query = "SELECT p FROM Product p WHERE p.product_date= ?1"), 
	@NamedQuery(name = "Product.checkDateUniqueness", query = "SELECT p FROM Product p  WHERE p.product_date = ?1"),
	@NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p  WHERE p.product_id = ?1")})

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int product_id;

	private String product_name;
	
	@Temporal(TemporalType.DATE)
	private Date product_date;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private byte[] product_image;

	
	@OneToMany(mappedBy="product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true ) //FetchType.LAZY is also the default, but it's written for clearity
	 private List<Question> questions;


	
	@OneToMany(mappedBy="product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true ) //FetchType.LAZY is also the default, but it's written for clearity
	 private List<Answer> answers;

	
	@OneToMany(mappedBy="product", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true ) //FetchType.LAZY is also the default, but it's written for clearity
	 private List<Review> reviews;

	
	@OneToMany(mappedBy="product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true ) //FetchType.LAZY is also the default, but it's written for clearity
	 private List<Leaderboard> leaderboard;
	



	public Product() {
	}


	public Product(String name, byte[] productImage, Date date) {
		this.product_name = name;
		this.product_date = date;
		this.product_image = productImage;
	}

	public int getId() {
		return product_id;
	}
	
	public String getName() {
		return product_name;
	}


	public void setName(String name) {
		this.product_name = name;
	}
	
	public byte[] getProductImage() {
		return this.product_image;
	}
	
	public String getProductImageData() {
		return Base64.getMimeEncoder().encodeToString(product_image);
	}


	public void setProductImage(byte[] productImage) {
		this.product_image = productImage;
	}


	public int getProduct_id() {
		return product_id;
	}


	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}


	public String getProduct_name() {
		return product_name;
	}


	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}


	public Date getProduct_date() {
		return product_date;
	}


	public void setProduct_date(Date product_date) {
		this.product_date = product_date;
	}


	public byte[] getProduct_image() {
		return product_image;
	}


	public void setProduct_image(byte[] product_image) {
		this.product_image = product_image;
	}


	public List<Question> getQuestions() {
		return questions;
	}


	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}


	public List<Answer> getAnswers() {
		return answers;
	}


	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}


	public List<Review> getReviews() {
		return reviews;
	}


	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}


	public List<Leaderboard> getLeaderboard() {
		return leaderboard;
	}


	public void setLeaderboard(List<Leaderboard> leaderboard) {
		this.leaderboard = leaderboard;
	}
	
	
	
	
}