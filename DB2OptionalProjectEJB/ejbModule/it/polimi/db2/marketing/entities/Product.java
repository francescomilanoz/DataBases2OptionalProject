package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import java.time.LocalDate;
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
@NamedQueries({@NamedQuery(name = "Product.findByDate", query = "SELECT p FROM Product p WHERE p.date= ?1"), 
	@NamedQuery(name = "Product.checkDateUniqueness", query = "SELECT p FROM Product p  WHERE p.date = ?1")})

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private byte[] productImage;
	
	/*
	@OneToMany(mappedBy = "productID")
	private List<Review> reviews;
	*/
	
	


	public Product() {
	}


	public Product(String name, byte[] productImage, Date date) {
		this.name = name;
		this.date = date;
		this.productImage = productImage;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] getProductImage() {
		return this.productImage;
	}
	
	public String getProductImageData() {
		return Base64.getMimeEncoder().encodeToString(productImage);
	}


	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}
	
	
	
}