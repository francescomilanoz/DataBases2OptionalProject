package it.polimi.db2.marketing.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.Review;
import it.polimi.db2.marketing.entities.User;

@Stateless
public class ReviewService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public ReviewService() {
	}

	public void createReview(String review_text, User user, Product product) {
		Review review = new Review(review_text, user, product);
		
		em.persist(review); // makes also question object managed via cascading
	}
	
	public List<Review> loadReviewByProduct(Product product) {
		List<Review> reviews = new ArrayList<>();
		reviews = em.createNamedQuery("Review.findByProduct", Review.class).setParameter(1, product)
				.getResultList();
		
		return reviews;
	}
}
