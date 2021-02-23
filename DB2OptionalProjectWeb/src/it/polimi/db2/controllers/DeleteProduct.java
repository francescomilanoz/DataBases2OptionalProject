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

import com.ctc.wstx.sr.InputElementStack;

import it.polimi.db2.marketing.services.ProductService;
import it.polimi.db2.marketing.services.QuestionService;
import it.polimi.db2.marketing.utils.QuestionType;

@WebServlet("/DeleteProduct")
@MultipartConfig
public class DeleteProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.marketing.services/ProductService")
	private ProductService pService;
	
	private TemplateEngine templateEngine;

	public DeleteProduct() {
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
		
		int productId = Integer.parseInt(request.getParameter("productId"));
		System.out.println("ProductId: " + productId);
		try {
			pService.deleteProduct(productId);
			System.out.println("Prodotto Eliminato");

		} catch (NumberFormatException | NullPointerException e) {
			e.printStackTrace();
		}

			// return the user to the right view
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/DeleteProductPage";
			
			response.sendRedirect(path);

	}


	public void destroy() {
	}

}
