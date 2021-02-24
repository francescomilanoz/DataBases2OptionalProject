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

import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.services.UserService;

@WebServlet("/GoToProfilePage")
public class GoToProfilePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.marketing.services/UserService")
	private UserService uService;

	public GoToProfilePage() {
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
		

		User oldUser = (User) session.getAttribute("user");
		User loggedUser = uService.getUserByUsername(oldUser.getUsername());
		session.setAttribute("user", loggedUser);
		
		String path = "Profile.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		ctx.setVariable("UserUsername", loggedUser.getUsername());
		ctx.setVariable("UserEmail", loggedUser.getEmail());
		ctx.setVariable("UserPoints", loggedUser.getPoints());

		boolean showPassword;

		if (session.getAttribute("showHidePassword") != null)
			showPassword = (boolean) session.getAttribute("showHidePassword");
		else
			showPassword = false;
		
		if(request.getParameter("ShowHide") != null) {
			showPassword = !showPassword;
			session.setAttribute("showHidePassword", showPassword);
		}
		
		if(showPassword) {
			ctx.setVariable("UserPassword", loggedUser.getPassword());
		} else {
			int passwordLength = loggedUser.getPassword().length();
			String passwordHided = "";
			for(int i = 0; i < passwordLength; i++)
				passwordHided += "*";
			
			ctx.setVariable("UserPassword", passwordHided);
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
