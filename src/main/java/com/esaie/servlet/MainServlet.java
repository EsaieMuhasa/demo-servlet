package com.esaie.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esaie.entity.Event;
import com.esaie.manager.EventRepository;
import com.esaie.serial.ArduinoListener;
import com.esaie.serial.ArduinoManager;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet({ "/index"})
public class MainServlet extends HttpServlet implements ArduinoListener {
	private static final long serialVersionUID = 1L;
	
	private EventRepository eventRepository;
//	private ArduinoManager arduinoManager;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException{
		eventRepository = EventRepository.getInstance();
//		arduinoManager = ArduinoManager.getInstance();
//		arduinoManager.addArduinoListener(this);
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
		
//		if(port == null || port.equals("none")) {
//			arduinoManager.disposeCurrentPort();
//		} else {
//			arduinoManager.open(port);
//		}
		
		response.sendRedirect("?page=0");
	}
	
	

	@Override
	public void onOpen(String portName) {
		System.out.println("open: "+portName);
	}

	@Override
	public void onClose(String portName) {
		System.out.println("close: "+portName);
	}

	@Override
	public void onError(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void onRead(String message) {
		System.out.println("read: "+message);
	}

	@Override
	public void onRead(Map<String, String> message) {
		try {			
			if(message.containsKey("distance")) {
				Event event = new Event();
				double distance = Double.parseDouble(message.get("distance"));
				event.setDistance(distance);
				event.setRecordDate(new Date());
				
				eventRepository.create(event);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onWrite(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectedPortChange(String[] ports) {
		for (String p : ports) {
			System.out.println(p);
		}
		System.out.println("\n\n");
	}

}
