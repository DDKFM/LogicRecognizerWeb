package de.mschaedlich.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.mschaedlich.logic.recognizer.logic.Logic;
import de.mschaedlich.logic.recognizer.logic.LogicParser;

/**
 * Servlet implementation class LogicServlet
 */
@WebServlet("/logic")
public class LogicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("method") != null) {
			switch(request.getParameter("method").toLowerCase()) {
			case "getatomic":
				if(request.getParameter("input") != null) {
					String input = request.getParameter("input");
					LogicParser parser = new LogicParser(input);
					String result = "[";
					List<String> atomicVars = parser.getAtomicVars();
					for(String var : atomicVars) {
						if(atomicVars.indexOf(var) < atomicVars.size() - 1)
							result += "\"" + var + "\", ";
						else
							result += "\"" + var + "\"";
					}
					result += "]";
					response.setContentType("application/json");
					response.getWriter().append(result);
				}
				break;
			case "calculate":
				if(request.getParameter("input") != null) {
					String input = request.getParameter("input");
					LogicParser parser = new LogicParser(input);
					List<String> atomicVars = parser.getAtomicVars();
					Map<String, Boolean> assignments = new HashMap<String, Boolean>();
					for(String atomic : atomicVars) {
						if(request.getParameter(atomic) != null) {
							String value = request.getParameter(atomic).trim().toLowerCase();
							
							boolean booleanValue = value.startsWith("true") || value.startsWith("on") || value.startsWith("0");
							assignments.put(atomic, booleanValue);
						} else {
							assignments.put(atomic, false);
						}
					}
					Logic logic = parser.parse();
					if(logic != null) {
						boolean resultValue = logic.getResult(assignments);
						response.setContentType("application/json");
						response.getWriter().write(resultValue ? "true" : "false");
					}
				}
				break;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
