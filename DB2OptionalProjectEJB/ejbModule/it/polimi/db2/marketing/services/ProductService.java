package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.joda.time.LocalDateTime;

import javax.persistence.NonUniqueResultException;

import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.exceptions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public ProductService() {
	}
	
	
	public Product loadProductOfTheDay() {
		java.util.Date date=new java.util.Date();
		
		List<Product> products = new ArrayList<>();
		products = em.createNamedQuery("Product.findByDate", Product.class).setParameter(1, date)
				.getResultList();
		
		return products.get(0);
	}
	
	
	// check if the product of the date already exists
	public boolean checkDateUniqueness(Date date) {

		List<Product> products = null;
		products = em.createNamedQuery("Product.checkDateUniqueness", Product.class).setParameter(1, date)
				.getResultList();
		if (products.isEmpty())
			return true;
		else {
			return false;
		}
	}
	
	

	public void createProduct(String title, byte[] image, Date date) {
		Product product = new Product(title, image, date);
		
		em.persist(product); // makes also mission object managed via cascading
	}
	
	
}
