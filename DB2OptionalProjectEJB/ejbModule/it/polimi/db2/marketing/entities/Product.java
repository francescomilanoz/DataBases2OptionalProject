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
	@NamedQuery(name = "Product.checkDateUniqueness", query = "SELECT p FROM Product p  WHERE p.product_date = ?1")})

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

	//Aggiungere review


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
	
	
	
}