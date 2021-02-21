package it.polimi.db2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

@WebServlet("/QuestionnaireAction")
public class QuestionnaireAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	public QuestionnaireAction() {
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
		
		if(request.getParameter("decision") == null)
			System.out.println("era null");
		else
			System.out.println(request.getParameter("decision"));
		
		if(request.getParameter("decision") != null) {
			
			if(request.getParameter("decision").equals("Previous")) {
				
				System.out.println("entrato correttamente");
				
				List<String> answers = new ArrayList<String>();
			
			    answers.add((String) request.getParameter("age"));
			    answers.add((String) request.getParameter("gender"));
			    answers.add((String) request.getParameter("expertiseLevel"));
			    
				System.out.println("entrato correttamente 2");
			
				session.setAttribute("statisticalAnswers", answers);
				
				// return the user to the right view
				String ctxpath = getServletContext().getContextPath();
				String path = ctxpath + "/QuestionnaireMarketing";
				
				System.out.println("entrato correttamente 3");
				
				response.sendRedirect(path);
	
			}
		}
		
		
			/*
			String path2 = "Profile.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process(path2, ctx, response.getWriter());
			*/
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
