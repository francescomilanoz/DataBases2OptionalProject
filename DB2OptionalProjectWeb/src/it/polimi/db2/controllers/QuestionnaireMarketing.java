package it.polimi.db2.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.marketing.services.ProductService;
import it.polimi.db2.marketing.services.QuestionService;
import it.polimi.db2.marketing.services.UserService;
import it.polimi.db2.marketing.utils.QuestionType;
import it.polimi.db2.marketing.entities.*;

@WebServlet("/QuestionnaireMarketing")
public class QuestionnaireMarketing extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.marketing.services/QuestionService")
	private QuestionService qService;
	
	@EJB(name = "it.polimi.db2.marketing.services/ProductService")
	private ProductService pService;
	
	private TemplateEngine templateEngine;

	public QuestionnaireMarketing() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Please fill this questionnaire: ";
		HttpSession session = request.getSession(true);
		
		out.println("<!DOCTYPE html>");
		out.println("<html xmlns:th=\"http://www.thymeleaf.org\">");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>" + title + "</title>");
		out.println("</head>");
		out.println("<body>");

		out.println("<h1>" + title + "</h1>");

		String url = request.getRequestURI().toString();
		
		List<Question> questions = new ArrayList<Question>();
		Product product = pService.loadProductOfTheDay();
		questions = qService.loadQuestionsByProductAndType(product, QuestionType.MARKETING);
		
		out.println("<form action=\"" + request.getRequestURI() + "\" method=POST>");
		
		for(int i = 0; i < questions.size(); i++) {
			out.print(questions.get(i).getQuestion_text() + "  ");
			out.println("<input type=\"text\" name=\"questionText\">");
		}
		out.println("<input type=\"submit\" name=\"Next\" value=\"Next\">");
		out.println("</form>");


		//End of html file
		out.println("</body>");
		out.println("</html>");

	}

	public void destroy() {
	}

}
