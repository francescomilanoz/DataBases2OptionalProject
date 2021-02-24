package it.polimi.db2.marketing.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.exceptions.*;

import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");

	}

	public void createUser(String username, String email, String password) {
		User user = new User(username, email, password);

		em.persist(user); // makes also mission object managed via cascading

	}

	// check if the username already exists
	public boolean checkUsernameUniqueness(String username) {
		List<User> users = null;
		users = em.createNamedQuery("User.checkUsernameUniqueness", User.class).setParameter(1, username)
				.getResultList();
		if (users.isEmpty())
			return true;
		else {
			return false;
		}
	}

	// check if the email already exists
	public boolean checkEmailUniqueness(String email) {
		List<User> users = null;
		users = em.createNamedQuery("User.checkEmailUniqueness", User.class).setParameter(1, email).getResultList();
		if (users.isEmpty())
			return true;
		else {
			return false;
		}
	}
	
	
	// check if the email already exists
	public User getUserByUsername(String username) {
		
		em.getEntityManagerFactory().getCache().evictAll();
		
		List<User> users = null;
		users = em.createNamedQuery("User.findByUsername", User.class).setParameter(1, username).getResultList();
		if (users.isEmpty())
			return null;
		else {
			return users.get(0);
		}
	}

	public void blockUser(User u) {
			em.createNamedQuery("User.blockUser", User.class).setParameter(1, u.getUsername()).executeUpdate();
	}
	
}
