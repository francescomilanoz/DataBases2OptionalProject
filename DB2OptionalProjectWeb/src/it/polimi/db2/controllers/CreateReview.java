package it.polimi.db2.controllers;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import it.polimi.db2.marketing.services.OffensiveWordService;
import it.polimi.db2.marketing.services.ReviewService;
import it.polimi.db2.marketing.services.UserService;
import it.polimi.db2.marketing.entities.*;

@WebServlet("/CreateReview")
public class CreateReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.marketing.services/ReviewService")
	private ReviewService rService;
	
	@EJB(name = "it.polimi.db2.marketing.services/OffensiveWordService")
	private OffensiveWordService oService;
	
	@EJB(name = "it.polimi.db2.marketing.services/UserService")
	private UserService uService;


	
	public CreateReview() {
		super();
	}

	public void init() throws ServletException {
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}
		
		Product product = (Product) session.getAttribute("productOfTheDay");
		if(product == null) {
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/Home";
			response.sendRedirect(path);
		}

		else {
		// Get and parse all parameters from request
		boolean isBadRequest = false;
		String review_text = null;
		

		try {
			
			review_text = StringEscapeUtils.escapeJava(request.getParameter("review"));
			
			isBadRequest = review_text.isEmpty();
		} catch (NumberFormatException | NullPointerException e) {
			isBadRequest = true;
			e.printStackTrace();
		}
		if (isBadRequest) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		
		//Check if the review contains an offensive word
		boolean offensiveWordFound = false;
		
		String reviewTextElaborated = review_text;
		
		reviewTextElaborated = reviewTextElaborated.toLowerCase();
		
		reviewTextElaborated = reviewTextElaborated.replaceAll("[^a-zA-Z0-9]", " ");
		reviewTextElaborated = reviewTextElaborated.replaceAll("\\s+", " ");
		
		System.out.println("stringa elaborata: " + reviewTextElaborated);
		
		StringTokenizer st = new StringTokenizer(reviewTextElaborated);
	     while (st.hasMoreTokens()) {
	         //System.out.println(st.nextToken());
	         if(oService.isAnOffensiveWord(st.nextToken())) {
	        	 offensiveWordFound = true;
	        	 break;
	         }
	     }
		
		
		if(!offensiveWordFound) {
			
		// Create review in DB
		System.out.println(session);
		
		User user = (User) session.getAttribute("user");
		
		System.out.println(user);
		System.out.println(product);
		
		try {
			rService.createReview(review_text, user, product);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create review");
			return;
		}

		// return the user to the right view
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/Home";
		response.sendRedirect(path);
		} else {
			//An offensive word is found!
			
			uService.blockUser((User) session.getAttribute("user"));
			
			// return the user to the index page
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/Logout";

			response.sendRedirect(path);
		}

		}
	}

	public void destroy() {
	}

}