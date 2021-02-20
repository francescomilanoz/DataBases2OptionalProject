package it.polimi.db2.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

@WebServlet("/CreateProduct")
@MultipartConfig
public class CreateProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.marketing.services/ProductService")
	private ProductService mService;
	
	@EJB(name = "it.polimi.db2.marketing.services/QuestionService")
	private QuestionService qService;
	
	private TemplateEngine templateEngine;

	public CreateProduct() {
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
		String title = null;
		byte[] image = null;
		Date date = null;
		boolean isBadRequest;
		try {
			title = StringEscapeUtils.escapeJava(request.getParameter("productName"));

			System.out.println("Titolo fatto" + title);

			Part imgFile = request.getPart("productPhoto");
			System.out.println("Foto fatto");
			InputStream imgContent = imgFile.getInputStream();
			System.out.println("Foto2 fatto");
			image = it.polimi.db2.utils.ImageUtils.readImage(imgContent);
			System.out.println("Foto3 fatto");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = (Date) sdf.parse(request.getParameter("productDate"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Date fatto" + date);

			isBadRequest = title.isEmpty() || image.length == 0 || date.toString().isEmpty();
		} catch (NumberFormatException | NullPointerException e) {
			isBadRequest = true;
			e.printStackTrace();
		}

		String path1;
		if (isBadRequest) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}

		boolean dateUnique = mService.checkDateUniqueness(date);
		
		Date currentDate = new java.util.Date();

		if (date.before(currentDate) && (date.getDay() != currentDate.getDay() || date.getMonth() != currentDate.getMonth() || date.getYear() != currentDate.getYear())) {
			
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

			ctx.setVariable("productCreationError", "It's not possible to create a product for a past date. ");

			path1 = "/CreateProduct.html";
			templateEngine.process(path1, ctx, response.getWriter());

		} else if (!dateUnique) {

			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

			ctx.setVariable("productCreationError", "There is already a product for that date. ");

			path1 = "/CreateProduct.html";
			templateEngine.process(path1, ctx, response.getWriter());
		}

		else {
			// Create product and statistical questions in DB
			try {
				
				//Reset the session values 
				
				HttpSession session = request.getSession(true);
				
				int numQuestions = 0;
				if(session.getAttribute("numQuestions") != null) {
					numQuestions = (int) session.getAttribute("numQuestions");
				}
				for(int i = 0; i < numQuestions; i++) {
					session.removeAttribute("question" + i);
				}
				
				session.removeAttribute("productDate");
				session.removeAttribute("numQuestions");

				mService.createProduct(title, image, date);
				
				qService.createQuestion("Insert your age: ", QuestionType.STATISTICAL, mService.loadProductByDate(date));
				qService.createQuestion("Insert your sex: ", QuestionType.STATISTICAL, mService.loadProductByDate(date));
				qService.createQuestion("Insert your expertise level: ", QuestionType.STATISTICAL, mService.loadProductByDate(date));
				
				session.setAttribute("productDate", date);

			} catch (Exception e) {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create the product");
				return;
			}

			// return the user to the right view
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/CreateQuestionnaire";
			
			response.sendRedirect(path);

		}

	}

	public void destroy() {
	}

}
