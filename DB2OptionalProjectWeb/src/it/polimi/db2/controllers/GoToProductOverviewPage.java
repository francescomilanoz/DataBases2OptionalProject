package it.polimi.db2.controllers;

import java.io.IOException;
import java.util.Date;
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
import it.polimi.db2.marketing.utils.QuestionType;
import it.polimi.db2.marketing.entities.*;

@WebServlet("/ProductCreated")
public class GoToProductOverviewPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.marketing.services/ProductService")
	private ProductService mService;
	
	@EJB(name = "it.polimi.db2.marketing.services/QuestionService")
	private QuestionService qService;
	
	private TemplateEngine templateEngine;

	public GoToProductOverviewPage() {
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

		
		HttpSession session = request.getSession(true);
		
		if(request.getParameter("Save").equals("Save")) {
			//Check if the attribute numQuestions is defined, and then load it (to avoid NullPointerException)
			int numQuestions = 0;
			if(session.getAttribute("numQuestions") != null) {
				numQuestions = (int) session.getAttribute("numQuestions");
			}
			
			//Load the various questions (only if the admin hasn't deleted them)
			Date date = new Date();
			if(session.getAttribute("productDate") != null) {
				date = (Date) session.getAttribute("productDate");
			}
			Product product = mService.loadProductByDate(date);

			for(int i = 0; i < numQuestions; i++) {
				String tmp = (String) session.getAttribute("question" + i);
				if(!tmp.equals("null")) {
					qService.createQuestion(tmp, QuestionType.MARKETING, product);
				}
			}	

		}
		
	
		// Shows the page
		String path = "ProductOverview.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
