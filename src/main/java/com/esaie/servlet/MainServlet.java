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
import com.esaie.serial.ArduinoManager;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet({ "/index"})
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private EventRepository eventRepository;
	private ArduinoManager arduinoManager;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException{
		eventRepository = EventRepository.getInstance();
		arduinoManager = ArduinoManager.getInstance();
	}
	
	@Override
	public void destroy() {
		//arduinoManager.dispose();
		super.destroy();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		int $page  = 0;
		try {
			$page = ((int)Double.parseDouble(page)) * 20;
		} catch (NumberFormatException | NullPointerException e) {
			response.sendRedirect("?page=0");
			return;
		}
		
		int count = eventRepository.countAll();
		if($page >= count || $page < 0) {
			response.sendRedirect("?page=0");
			return;
		}
		
//		String ports[] = arduinoManager.getPorts();
		String ports [] = {};

		Event [] events = eventRepository.findAll(20, $page);
		request.setAttribute("events", events);
		request.setAttribute("ports", ports);
		request.setAttribute("startIndex", $page);
		getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String port = request.getParameter("port");
		System.out.println(">>> "+port);
		System.out.println(request.getRequestURL().toString());
		
		if(port == null || port.equals("none")) {
			arduinoManager.disposeCurrentPort();
		} else {
			arduinoManager.open(port);
		}
		response.sendRedirect("?page=0");
	}

}
