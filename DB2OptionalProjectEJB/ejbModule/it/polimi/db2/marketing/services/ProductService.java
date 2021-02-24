package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.Review;

import java.util.ArrayList;
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
		
		if(products.size() == 0) {
			return null;
		}
		
		return products.get(0);
	}
	
	
	public Product loadProductByDate(Date date) {
		List<Product> products = new ArrayList<>();
		products = em.createNamedQuery("Product.findByDate", Product.class).setParameter(1, date)
				.getResultList();
		
		if(products.size() == 0) {
			return null;
		}
		
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
		
		em.persist(product); // makes also product object managed via cascading
	}
	
	public List<Product> getProductsList(){
		java.util.Date date = new java.util.Date();
		List<Product> products = new ArrayList<>();
		products = em.createQuery("SELECT p FROM Product p WHERE p.product_date < ?1").setParameter(1, date).getResultList();
		return products;
	}
	
	public void deleteProduct(int productId) {
		Product product = em.find(Product.class, productId);
		em.remove(product);
	}
	
	public Product loadProductById(int product_id) {
		List<Product> products = new ArrayList<>();
		products = em.createNamedQuery("Product.findById", Product.class).setParameter(1, product_id)
				.getResultList();
		
		if(products.size() == 0) {
			return null;
		}
		
		return products.get(0);
	}
	
	
}
