package it.polimi.db2.controllers;

import java.io.IOException;

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

import it.polimi.db2.marketing.entities.Leaderboard;
import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.services.LeaderboardService;

@WebServlet("/Questionnaire")
public class GoToQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.marketing.services/LeaderboardService")
	private LeaderboardService lService;

	public GoToQuestionnairePage() {
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

		HttpSession session = request.getSession();

		Leaderboard leaderboard = lService.loadLeaderboardByUserAndProduct((User) session.getAttribute("user"),
				(Product) session.getAttribute("productOfTheDay"));
		Product product = (Product) session.getAttribute("productOfTheDay");
		
		if (leaderboard == null && product != null) {
			// return the user to the right view
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/QuestionnaireMarketing";
			response.sendRedirect(path);
		}
		else {
			String path = "QuestionnaireAlreadyFilled.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			if (product == null)
				ctx.setVariable("questionnaireResponse", "Sorry, there is no questionnaire for the moment!");
			else if(leaderboard.getDaily_points() == -1)
				ctx.setVariable("questionnaireResponse", "Sorry, you can't submit the questionnare after having deleted it! Return tomorrow.");
			else
				ctx.setVariable("questionnaireResponse", "You have already filled the questionnaire of today! Return tomorrow.");
			templateEngine.process(path, ctx, response.getWriter());
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
