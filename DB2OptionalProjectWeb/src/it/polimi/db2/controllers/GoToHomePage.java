package it.polimi.db2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.marketing.services.*;
import it.polimi.db2.marketing.entities.*;

@WebServlet("/Home")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "it.polimi.db2.marketing.services/ProductService")
	private ProductService pService;

	@EJB(name = "it.polimi.db2.marketing.services/ReviewService")
	private ReviewService rService;
	
	@EJB(name = "it.polimi.db2.marketing.services/LoginRecordService")
	private LoginRecordService lService;
	
	public GoToHomePage() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		
		//Add the login record
		lService.createLoginRecord(new Date(), (User) session.getAttribute("user"));
		
		// Product of the day
		Product productOfTheDay = null;
		productOfTheDay = pService.loadProductOfTheDay();
		session.setAttribute("productOfTheDay", productOfTheDay);
	

		
		String path = "Home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		
		
		if (productOfTheDay != null) {
			ctx.setVariable("product", productOfTheDay);
		} else {
			Product emptyProduct = new Product("", new byte[0] , new Date());
			ctx.setVariable("product", emptyProduct);
			ctx.setVariable("noProduct", "No product of the day.");
		}
	
		//Reviews of the Product of the day
		List<Review> reviews = new ArrayList<>();
		reviews = rService.loadReviewByProduct(productOfTheDay);
		
		for(int i = 0; i < reviews.size(); i++) {
			String tmp = reviews.get(i).getReview_text();
			tmp.replaceAll("\n",",");
			tmp.replaceAll("\r",",");
			reviews.get(i).setReview_text(tmp);
		}
		
		
		if (reviews.size() != 0) {
			ctx.setVariable("reviews", reviews);
		} else {
			ctx.setVariable("reviews", reviews);
			ctx.setVariable("noReviews", "No reviews for the product of the day.");
		}
		
		templateEngine.process(path, ctx, response.getWriter());


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
