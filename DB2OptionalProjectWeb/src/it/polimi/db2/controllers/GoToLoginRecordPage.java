package it.polimi.db2.controllers;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import it.polimi.db2.marketing.entities.LoginRecord;
import it.polimi.db2.marketing.services.LoginRecordService;

@WebServlet("/LoginRecordPage")
public class GoToLoginRecordPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.marketing.services/LoginRecordService")
	private LoginRecordService lrService;

	public GoToLoginRecordPage() {
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

		List<LoginRecord> loginRecordOfTheDay = new ArrayList<>();

		if (request.getParameter("date") != null) {
			Date date;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date"));
				loginRecordOfTheDay = lrService.loadLoginRecordOfTheDay(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 

		// Redirect to the login record page
		String path = "LoginRecordPage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		// System.out.println(loginRecordOfTheDay.get(0).getUser().getUsername());

		if (request.getParameter("Search") != null) {
			if (loginRecordOfTheDay.size() == 0)
				ctx.setVariable("noLoginRecord", "No one has logged in that day!");
			else {
				ctx.setVariable("loginRecords", loginRecordOfTheDay);
			}
		} else {
			ctx.setVariable("noLoginRecord", "Select a date to retrieve the list of log-ins of that date. ");
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
