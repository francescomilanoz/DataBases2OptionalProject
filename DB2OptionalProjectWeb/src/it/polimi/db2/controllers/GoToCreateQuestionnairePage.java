package it.polimi.db2.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CreateQuestionnaire
 */
@WebServlet("/CreateQuestionnaire")
public class GoToCreateQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoToCreateQuestionnairePage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Create your questionnaire: ";
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
		
		System.out.println("URL: " + url);

		int numQuestions;
		if (session.getAttribute("numQuestions") != null) {
			numQuestions = (int) session.getAttribute("numQuestions");
		} else {
			numQuestions = 0;
		    System.out.println("ELSE CASE");
		}

		String questionText = request.getParameter("questionText");
		System.out.println(questionText + "prev num: " + numQuestions);

		List<String> questionTexts = new ArrayList<String>();

		for (int i = 0; i < numQuestions; i++) {
			
				questionTexts.add((String) session.getAttribute("question" + i));
		}

		String selectedOperation = request.getParameter("operation");

		if (selectedOperation != null) {

			if (selectedOperation.equals("Add")) {
				numQuestions++;
				session.setAttribute("numQuestions", numQuestions);
				questionTexts.add(questionText);
				session.setAttribute("question" + (numQuestions - 1), request.getParameter("questionText"));
			} else if (selectedOperation.equals("Remove")) {
				int numberToRemove = Integer.parseInt(request.getParameter("numberToRemove"));
				session.removeAttribute("question" + numberToRemove);
				session.setAttribute("question" + numberToRemove, "null");
				questionTexts.set(numberToRemove, "null");
				
				
			}
		}

		for (int i = 0; i < numQuestions; i++) {
			
			if(!questionTexts.get(i).equals("null")) {
				out.println(questionTexts.get(i) + "   ");

				out.println("<form action=\"" + request.getRequestURI() + "\" method=GET>");
				out.println("<input type=\"submit\" name=\"operation\" value=\"Remove\">");
				out.println("<input type=\"hidden\" name=\"numberToRemove\" value=\""+i+"\">");
				out.println("</form>");
				
			}

		}

		//Form to add a question
		out.println("<form action=\"" + request.getRequestURI() + "\" method=GET>");
		out.print("<input type=\"text\" name=\"questionText\"");
		out.println(" >");
		out.println("<input type=\"submit\" name=\"operation\" value=\"Add\">");
		out.println("</form>");
		
		//Form to save the questionnaire
		out.println("<form action=\"" + "/DB2OptionalProjectWeb/ProductCreated" + "\" method=GET>");
		out.println("<fieldset>");
		out.println("<input type=\"submit\" name=\"Save\" value=\"Save\">");
		out.println("</fieldset>");
		out.println("</form>");
		
		//Form to cancel the questionnaire
		out.println("<form action=\"" + "/DB2OptionalProjectWeb/ProductCreated" + "\" method=GET>");
		out.println("<fieldset>");
		out.println("<input type=\"submit\" name=\"Save\" value=\"Skip\">");
		out.println("</fieldset>");
		out.println("</form>");

		//End of html file
		out.println("</body>");
		out.println("</html>");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}