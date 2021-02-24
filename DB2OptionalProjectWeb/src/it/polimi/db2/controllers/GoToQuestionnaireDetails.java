package it.polimi.db2.controllers;

import java.io.IOException;
import java.util.ArrayList;
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

import it.polimi.db2.marketing.entities.Answer;
import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.services.AnswerService;
import it.polimi.db2.marketing.services.ProductService;
import it.polimi.db2.marketing.services.UserService;

@WebServlet("/QuestionnaireDetails")
public class GoToQuestionnaireDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.marketing.services/AnswerService")
	private AnswerService aService;
	@EJB(name = "it.polimi.db2.marketing.services/ProductService")
	private ProductService pService;
	@EJB(name = "it.polimi.db2.marketing.services/UserService")
	private UserService uService;

	

	public GoToQuestionnaireDetails() {
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
		
		int product_id = Integer.parseInt(request.getParameter("product_id"));
		Product product = null;
		if (request.getParameter("product_id") != null )
			product = pService.loadProductById(product_id);
		
		String username = request.getParameter("username");
		User user = null;
		if (request.getParameter("username") != null )
			user = uService.getUserByUsername(username);
		
			
		List<Answer> answers = new ArrayList<>();
		answers = aService.loadAnswerByProductAndUser(product, user);
		
		

		// Redirect to the Home page and add missions to the parameters
		String path = "QuestionnaireDetails.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("user", user);
		ctx.setVariable("product", product);
		ctx.setVariable("answers", answers);
		templateEngine.process(path, ctx, response.getWriter());
	}

	public void destroy() {
	}

}
