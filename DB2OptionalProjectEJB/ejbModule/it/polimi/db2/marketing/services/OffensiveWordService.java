package it.polimi.db2.marketing.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.marketing.entities.OffensiveWord;

@Stateless
public class OffensiveWordService {
	@PersistenceContext(unitName = "DB2OptionalProjectEJB")
	private EntityManager em;

	public OffensiveWordService() {
	}
	
	public boolean isAnOffensiveWord(String text) {
		List<OffensiveWord> offensiveWord = new ArrayList<>();
		offensiveWord = em.createNamedQuery("OffensiveWord.findByText", OffensiveWord.class).setParameter(1, text)
				.getResultList();
		
		if(offensiveWord.size() == 0)
			return false;
		else
			return true;
	}
}
