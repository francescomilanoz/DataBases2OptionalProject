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

import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.Question;
import it.polimi.db2.marketing.services.QuestionService;
import it.polimi.db2.marketing.utils.QuestionType;

@WebServlet("/QuestionnaireStatistical")
public class GoToQuestionnaireStatisticalPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.marketing.services/QuestionService")
	private QuestionService qService;

	public GoToQuestionnaireStatisticalPage() {
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
		
		int numberOfAnswers;
		
		if(request.getParameter("NumberOfAnswers") != null)
			numberOfAnswers = Integer.parseInt(request.getParameter("NumberOfAnswers"));
		else
			numberOfAnswers = 0;
		
		List<String> answers = new ArrayList<String>();
		
		for(int i = 0; i < numberOfAnswers; i++) {
			answers.add((String) request.getParameter("answer"+i));
		}
	
		session.setAttribute("marketingQuestions", answers);
		
		
		List<Question> statisticalQuestions = new ArrayList<Question>();
		statisticalQuestions = qService.loadQuestionsByProductAndType((Product) session.getAttribute("productOfTheDay"), QuestionType.STATISTICAL);
		

		String path = "QuestionnaireStatistical.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("age", statisticalQuestions.get(0).getQuestion_text());
		ctx.setVariable("gender", statisticalQuestions.get(1).getQuestion_text());
		ctx.setVariable("expertiseLevel", statisticalQuestions.get(2).getQuestion_text());
		
		String ageCompiled;
		String ageValue = "";
		if(session.getAttribute("statisticalAnswers") == null) 
			ageCompiled = "false";
		else {
			
			ageCompiled = "true";
			List<String> statisticalQuestionsPreCompiled = (List<String>) session.getAttribute("statisticalAnswers");
			
			if(statisticalQuestionsPreCompiled.size() > 0)
				ageValue = statisticalQuestionsPreCompiled.get(0);
			
		}
		
		System.out.println(ageValue);
		
		ctx.setVariable("ageCompiled", ageCompiled);
		ctx.setVariable("ageValue", ageValue);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
