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

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.marketing.services.LoginRecordService;
import it.polimi.db2.marketing.services.UserService;
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.exceptions.CredentialsException;
import javax.persistence.NonUniqueResultException;

@WebServlet("/UserYetLogged")
public class UserYetLogged extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.marketing.services/UserService")
	private UserService usrService;
	
	@EJB(name = "it.polimi.db2.marketing.services/LoginRecordService")
	private LoginRecordService lService;

	public UserYetLogged() {
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
		String path;
		if((User) session.getAttribute("user") != null) {
			
			User loggedUser = (User) session.getAttribute("user");
			
			if(loggedUser.getType().equals("User") && loggedUser.isActive()) {
				
			lService.createLoginRecord(new Date(), loggedUser);
			
			path = getServletContext().getContextPath() + "/Home";
			response.sendRedirect(path);
			
			} else if(loggedUser.getType().equals("User") && !loggedUser.isActive()) {
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMsg", "Sorry, your account has been banned.");
				path = "/index.html";
				templateEngine.process(path, ctx, response.getWriter());
			} else if(loggedUser.getType().equals("Admin")) {
				path = getServletContext().getContextPath() + "/AdminHome";
				response.sendRedirect(path);
			}
		}

	}

	public void destroy() {
	}
}