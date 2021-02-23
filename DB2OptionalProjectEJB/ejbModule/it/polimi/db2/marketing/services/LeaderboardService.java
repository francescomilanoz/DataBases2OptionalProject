package it.polimi.db2.marketing.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.Answer;
import it.polimi.db2.marketing.entities.Leaderboard;
import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.User;

@Stateless
public class LeaderboardService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public LeaderboardService() {
	}

	public void createLeaderboard(Date leaderboard_timestamp, int daily_points, User user, Product product) {
		Leaderboard leaderboard = new Leaderboard(leaderboard_timestamp, daily_points, user, product);
		
		em.persist(leaderboard); // makes also leaderboard object managed via cascading
	}	
	
	
	
	public Leaderboard loadLeaderboardByUserAndProduct(User user, Product product) {
		List<Leaderboard> leaderboard = new ArrayList<>();
		leaderboard = em.createNamedQuery("Leaderboard.findByUserAndProduct", Leaderboard.class).setParameter(1, user).setParameter(2, product)
				.getResultList();
		
		if(leaderboard.size() == 0) {
			return null;
		}
		
		return leaderboard.get(0);
	}
	
	public List<Leaderboard> loadLeaderboardByProductOrderByPoints(Product product) {
		List<Leaderboard> leaderboard = new ArrayList<>();
		leaderboard = em.createNamedQuery("Leaderboard.findByProductOrderByPoints", Leaderboard.class).setParameter(1, product)
				.getResultList();
		
		if(leaderboard.size() == 0) {
			return null;
		}
		
		return leaderboard;
	}
	
	public List<User> loadUsersDeleteProduct(int productId) {
		List<User> users = new ArrayList<>();
		users =  em.createQuery("SELECT l.user FROM Leaderboard l WHERE l.product.product_id = ?1 AND l.daily_points = -1").setParameter(1, productId).getResultList();

		if(users.size() == 0) {
			return null;
		}
		return users;
	}
	
	public List<Leaderboard> loadUsersSubmitProduct(int productId) {
		List<Leaderboard> leaderboards = new ArrayList<>();
		leaderboards =  em.createQuery("SELECT l FROM Leaderboard l WHERE l.product.product_id = ?1 AND l.daily_points > 0").setParameter(1, productId).getResultList();
		List<Answer> answers = new ArrayList();
		for (int i = 0; i<leaderboards.size(); i++) {
			answers = em.createNamedQuery("Answer.findByProductUser", Answer.class).setParameter(1, productId).setParameter(2, leaderboards.get(i).getUser().getId()).getResultList();
			leaderboards.get(i).setDaily_points(answers.size());
		}
		if(leaderboards.size() == 0) {
			return null;
		}
		return leaderboards;
	}
}
