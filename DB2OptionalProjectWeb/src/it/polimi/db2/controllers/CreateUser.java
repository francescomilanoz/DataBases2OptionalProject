package it.polimi.db2.controllers;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.marketing.services.UserService;

@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.marketing.services/UserService")
	private UserService mService;
	private TemplateEngine templateEngine;

	public CreateUser() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Get and parse all parameters from request
		String username = null;
		String email = null;
		String password = null;
		boolean isBadRequest;
		try {
			username = StringEscapeUtils.escapeJava(request.getParameter("username"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			password = StringEscapeUtils.escapeJava(request.getParameter("password"));

			System.out.println("Username: " + username);
			
			isBadRequest = username.isEmpty() || email.isEmpty() || password.isEmpty();
		} catch (NumberFormatException | NullPointerException e) {
			isBadRequest = true;
			e.printStackTrace();
		}

		String path1;
		if (isBadRequest) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}

		boolean usernameUnique = mService.checkUsernameUniqueness(username);
		boolean emailUnique = mService.checkEmailUniqueness(email);

		if (!usernameUnique || !emailUnique) {
			
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
			if(!usernameUnique && !emailUnique)
				ctx.setVariable("signUpError", "Username and E-mail already in use");
			else if(!usernameUnique)
				ctx.setVariable("signUpError", "Username already in use");
			else if(!emailUnique)
				ctx.setVariable("signUpError", "E-mail already in use");
			path1 = "/Signup.html";
			templateEngine.process(path1, ctx, response.getWriter());
		}

		else {
			// Create user in DB
			try {

				mService.createUser(username, email, password);

			} catch (Exception e) {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create the user");
				return;
			}
			
			
			// return the user to the right view 
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/Index";
			response.sendRedirect(path);
			
		}

	}

	public void destroy() {
	}

}
