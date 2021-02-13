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
	
	/*
	// check if the username/email already exists
		public boolean checkUniqueness(String username, String email) {
			List<User> users = null;
			users = em.createQuery("Select * from User u where u.username = '" + username + "' or u.email = '" + email +"'", User.class).getResultList();
			if (users.isEmpty())
				return true;
			else {
				return false;
			}
		}
		*/


	public void updateProfile(User u) throws UpdateProfileException {
		try {
			em.merge(u);
		} catch (PersistenceException e) {
			throw new UpdateProfileException("Could not change profile");
		}
	}
}
