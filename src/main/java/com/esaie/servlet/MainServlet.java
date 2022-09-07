package com.esaie.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esaie.entity.Event;
import com.esaie.manager.EventRepository;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet({ "/index"})
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private EventRepository eventRepository;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		eventRepository = EventRepository.getInstance();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		int $page  = 0;
		try {
			$page = ((int)Double.parseDouble(page)) * 20;
		} catch (NumberFormatException e) {
			response.sendRedirect("?page=0");
			return;
		}
		
		int count = eventRepository.countAll();
		if($page >= count || $page < 0) {
			response.sendRedirect("?page=0");
			return;
		}

		Event [] events = eventRepository.findAll(20, $page);
		request.setAttribute("events", events);
		request.setAttribute("startIndex", $page);
		getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
