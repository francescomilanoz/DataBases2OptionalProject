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

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import it.polimi.db2.marketing.entities.Leaderboard;
import it.polimi.db2.marketing.entities.Product;
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.services.LeaderboardService;
import it.polimi.db2.marketing.services.ProductService;

@WebServlet("/LoadPastQuestionnaire")
public class LoadStatistics extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.marketing.services/ProductService")
	private ProductService pService;

	@EJB(name = "it.polimi.db2.marketing.services/LeaderboardService")
	private LeaderboardService lService;
	
	public LoadStatistics() {
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

				String questionnaire = "Statistics:";
				
				// Redirect to the Home page and add missions to the parameters
				String path = "Inspection.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				
				//Product lists
				List<Product> products = new ArrayList<>();
				products = pService.getProductsList();
				
				List<User> usersC = new ArrayList<>();
				Product productSelected = null;
				if(request.getParameter("productId") !=null) {
					productSelected = pService.loadProductById(Integer.parseInt(request.getParameter("productId")));
					usersC = lService.loadUsersDeleteProduct(Integer.parseInt(request.getParameter("productId")));
				}
				
				List<Leaderboard> leaderboardS = new ArrayList<>();
				if(request.getParameter("productId") !=null)
					leaderboardS = lService.loadUsersSubmitProduct(Integer.parseInt(request.getParameter("productId")));
				
				if (products.size() != 0) {
					ctx.setVariable("productSelected", productSelected);
					ctx.setVariable("products", products);
					ctx.setVariable("questionnaire", questionnaire);
					ctx.setVariable("submitted", "List of users who submitted the questionnaire");
					ctx.setVariable("cancelled", "List of users who cancelled the questionnaire");
					ctx.setVariable("answers", "Questionnaire answers of each user");
					ctx.setVariable("usersC", usersC);
					ctx.setVariable("usersS", leaderboardS);
				} else {
					ctx.setVariable("products", products);
					ctx.setVariable("noProduct", "No product inserted yet.");
				}
				
				//System.out.println(products.get(0).getName());
						
				templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
