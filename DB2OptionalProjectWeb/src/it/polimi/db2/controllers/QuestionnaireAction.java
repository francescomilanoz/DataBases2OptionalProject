package it.polimi.db2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
import it.polimi.db2.marketing.entities.User;
import it.polimi.db2.marketing.services.AnswerService;
import it.polimi.db2.marketing.services.LeaderboardService;
import it.polimi.db2.marketing.services.OffensiveWordService;
import it.polimi.db2.marketing.services.QuestionService;
import it.polimi.db2.marketing.services.UserService;
import it.polimi.db2.marketing.utils.QuestionType;

@WebServlet("/QuestionnaireAction")
public class QuestionnaireAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.marketing.services/LeaderboardService")
	private LeaderboardService lService;
	
	@EJB(name = "it.polimi.db2.marketing.services/AnswerService")
	private AnswerService aService;
	
	@EJB(name = "it.polimi.db2.marketing.services/QuestionService")
	private QuestionService qService;
	
	@EJB(name = "it.polimi.db2.marketing.services/OffensiveWordService")
	private OffensiveWordService oService;
	
	@EJB(name = "it.polimi.db2.marketing.services/UserService")
	private UserService uService;

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

		if (request.getParameter("decision") == null)
			System.out.println("era null");
		else
			System.out.println(request.getParameter("decision"));

		if (request.getParameter("decision") != null) {
			
			Product productOfTheDay = (Product) session.getAttribute("productOfTheDay");
			User loggedUser = (User) session.getAttribute("user");

			if (request.getParameter("decision").equals("Previous")) {

				List<String> answers = new ArrayList<String>();

				answers.add((String) request.getParameter("age"));
				answers.add((String) request.getParameter("gender"));
				answers.add((String) request.getParameter("expertiseLevel"));

				session.setAttribute("statisticalAnswers", answers);

				// return the user to the right view
				String ctxpath = getServletContext().getContextPath();
				String path = ctxpath + "/QuestionnaireMarketing";

				response.sendRedirect(path);

			}

			if (request.getParameter("decision").equals("Submit")) {
				
				
				
				List<String> answers = new ArrayList<String>();
				answers = (List<String>) session.getAttribute("marketingQuestions");
				
				boolean offensiveWordFound = false;
				
				for(String a: answers) {
					a = a.toLowerCase();
					
					a = a.replaceAll("[^a-zA-Z0-9]", " ");
					a = a.replaceAll("\\s+", " ");
					
					System.out.println("stringa elaborata: " + a);
					
					StringTokenizer st = new StringTokenizer(a);
				     while (st.hasMoreTokens()) {
				         //System.out.println(st.nextToken());
				         if(oService.isAnOffensiveWord(st.nextToken())) {
				        	 offensiveWordFound = true;
				        	 break;
				         }
				     }
				     if(offensiveWordFound)
				    	 break;
				}
				
				if(!offensiveWordFound) {
					List<Question> questionsMarketing = new ArrayList<>();
					questionsMarketing = qService.loadQuestionsByProductAndType(productOfTheDay, QuestionType.MARKETING);
					
					for(int i = 0; i < answers.size(); i++) {
						aService.createAnswer(answers.get(i), loggedUser, questionsMarketing.get(i), productOfTheDay);
					}
					
					List<Question> questionsStatistics = new ArrayList<>();
					questionsStatistics = qService.loadQuestionsByProductAndType(productOfTheDay, QuestionType.STATISTICAL);
					
					if(request.getParameter("age") != null) {
						String age = request.getParameter("age");
						if(age.length() > 0) {
							aService.createAnswer(age, loggedUser, questionsStatistics.get(0), productOfTheDay);
						}
					}
					
					if(request.getParameter("gender") != null) {
						String gender = request.getParameter("gender");
						if(gender.length() > 0) {
							aService.createAnswer(gender, loggedUser, questionsStatistics.get(1), productOfTheDay);
						}
					}
					
					if(request.getParameter("expertiseLevel") != null) {
						String expertiseLevel = request.getParameter("expertiseLevel");
						if(expertiseLevel.length() > 0) {
							aService.createAnswer(expertiseLevel, loggedUser, questionsStatistics.get(2), productOfTheDay);
						}
					}
					
					//Load the new points obtained by the user. 
					int dailyPoints = 0;
					if(lService.loadLeaderboardByUserAndProduct(loggedUser, productOfTheDay) != null)
						dailyPoints = lService.loadLeaderboardByUserAndProduct(loggedUser, productOfTheDay).getDaily_points();
					
					if(dailyPoints == 0) {
						lService.createLeaderboard(new Date(), 0, loggedUser, productOfTheDay);
					}
								
					
					String path2 = "Greetings.html";
					ServletContext servletContext = getServletContext();
					final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
					ctx.setVariable("dailyPoints", dailyPoints);
					templateEngine.process(path2, ctx, response.getWriter());
				}
				
				else { //An offensive word is found!
					
					uService.blockUser(loggedUser);
					
					// return the user to the index page
					String ctxpath = getServletContext().getContextPath();
					String path = ctxpath + "/Logout";

					response.sendRedirect(path);
					
				}
				
				

			}

			if (request.getParameter("decision").equals("Cancel")) {
				
				//I save in the DB the information about the cancellation of the form
				lService.createLeaderboard(new Date(), -1, loggedUser, productOfTheDay);
				
				
				// return the user to the right view
				String ctxpath = getServletContext().getContextPath();
				String path = ctxpath + "/Home";

				response.sendRedirect(path);

			}

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
