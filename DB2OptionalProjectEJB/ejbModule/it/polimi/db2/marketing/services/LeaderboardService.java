package it.polimi.db2.marketing.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
