package it.polimi.db2.marketing.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

//import java.util.List;

/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@Table(name = "leaderboard", schema = "db_marketing_app")
@NamedQueries({@NamedQuery(name = "Leaderboard.findByUserAndProduct", query = "SELECT l FROM Leaderboard l WHERE l.user= ?1 AND l.product = ?2"), 
	           @NamedQuery(name = "Leaderboard.findByProductOrderByPoints", query = "SELECT l FROM Leaderboard l WHERE l.product = ?1 AND l.daily_points > -1 ORDER BY l.daily_points DESC")})

public class Leaderboard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leaderboard_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date leaderboard_timestamp;
	
	private int daily_points;

	
	 @ManyToOne
	 @JoinColumn(name = "user_id")
	 private User user;

	@ManyToOne
	 @JoinColumn(name = "product_id")
	 private Product product;

	public Leaderboard() {
	}
	
	public Leaderboard(Date leaderboard_timestamp, int daily_points, User user, Product product) {
		this.leaderboard_timestamp = leaderboard_timestamp;
		this.daily_points = daily_points;
		this.user = user;
		this.product = product;
	}

	public int getDaily_points() {
		return daily_points;
	}

	public void setDaily_points(int daily_points) {
		this.daily_points = daily_points;
	}

	 public Date getLeaderboard_timestamp() {
			return leaderboard_timestamp;
		}

		public void setLeaderboard_timestamp(Date leaderboard_timestamp) {
			this.leaderboard_timestamp = leaderboard_timestamp;
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

		public int getLeaderboard_id() {
			return leaderboard_id;
		}

		public void setLeaderboard_id(int leaderboard_id) {
			this.leaderboard_id = leaderboard_id;
		}

		

}