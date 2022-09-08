/**
 * 
 */
package com.esaie.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonGenerator;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.esaie.serial.ArduinoListener;
import com.esaie.serial.ArduinoManager;

/**
 * @author Esaie Muhasa
 *
 */
@ServerEndpoint(value = "/ws")
public class WsEndpoint{
	
	private static final List<Session> SESSIONS = new ArrayList<>();
	
	private Session session;
	private ArduinoAdapter adapter = new ArduinoAdapter();
	private ArduinoManager manager; 


	public WsEndpoint() {
		manager = ArduinoManager.getInstance();
	}
	
	@OnMessage
	public void onMessage(Session session, String message) {
		System.out.println("message >> "+session.getId()+ " >> "+message);
	}
	
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		manager.addArduinoListener(adapter);
	}
	
	@OnClose
	public void onClose(Session session) {
		manager.removeArduinoListener(adapter);
		SESSIONS.remove(session);
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("WS error: ID"+ session.getId());
		throwable.printStackTrace();
	}
	
	private class ArduinoAdapter implements ArduinoListener {
		@Override
		public void onOpen(String portName) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onClose(String portName) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onError(Exception e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onRead(String message) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onRead(Map<String, String> message) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onWrite(String message) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onConnectedPortChange(String[] ports) {
			StringWriter out = new StringWriter();
			JsonArrayBuilder json = Json.createArrayBuilder();
			for (String port : ports) {
				json.add(port);
				System.out.println(port);
			}
			
			JsonGenerator gen = Json.createGenerator(out)
					.writeStartObject()
					.write("currentPort", manager.getOpendPortName() == null? "" : manager.getOpendPortName())
					.write("ports", json.build())
					.writeEnd();
			
			gen.close();

			try {
				session.getBasicRemote().sendText(out.toString());
			} catch (IOException e) {
				
			};
		}
		
	}
	

}
