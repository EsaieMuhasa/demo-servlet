/**
 * 
 */
package com.esaie.serial;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gnu.io.NRSerialPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @author Esaie Muhasa
 *
 */
public class ArduinoManager implements Runnable, SerialPortEventListener {
	
	private final ArrayList<String> ports = new ArrayList<>();
	private NRSerialPort port;
	private String portName;
	private ArrayList<ArduinoListener> listeners = new ArrayList<>();
	
	private Thread thread;
	private boolean running = true;
	
	private static ArduinoManager manager;

	/**
	 * 
	 */
	private ArduinoManager() {
		super();
		thread = new Thread(this);
		thread.start();
	}
	
	
	/**
	 * utilitaire d'instanciation d com.esaie.manager des ports serie
	 * @return
	 */
	public static ArduinoManager getInstance () {
		if(manager == null)
			manager = new ArduinoManager();
		
		return manager;
	}
	
	
	/**
	 * arret total du scanning des ports
	 */
	public void dispose() {
		disposeCurrentPort();
		running = false;
	}
	
	/**
	 * reloancement du scnning des ports
	 */
	public void reload() {
		if (thread != null && thread.getState() != State.TERMINATED)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * renvoie la liste des ports disponible
	 * @return
	 */
	public String[] getPorts () {
		return ports.toArray(new String[ports.size()]);
	}
	
	/**
	 * ouverture du port dont le nom symbolique est en parametre
	 * @param portName
	 */ 
	public synchronized void open (String portName) {
		if(portName == null)
			return;
		
		if(hasOpendPort()){
			if(this.portName.equals(portName))
				return;
			disposeCurrentPort();
		}
		
		if(!ports.contains(portName))
			throw new RuntimeException("Port inconnito dans le model des ports: -> "+portName);
		
		try {
			
			port = new NRSerialPort(portName, 9600, SerialPort.PARITY_NONE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1);
			this.portName = portName;
			port.connect();
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);

			for(ArduinoListener ls : listeners)
				ls.onOpen(portName);
		} catch (Exception e) {
			for(ArduinoListener ls : listeners)
				ls.onError(e);
		}
	}
	
	/**
	 * renvoie le nom du port actuelement occupee
	 * @return
	 */
	public String getOpendPortName () {
		if(port != null)
			return portName;
		
		return null;
	}
	
	/**
	 * liberation du port encours d'utilisation
	 */
	public synchronized void disposeCurrentPort () {
		if(!hasOpendPort())
			return;
		String name = portName;
		port.removeEventListener();
		port.disconnect();
		port = null;
		portName = null;
		
		for(ArduinoListener ls : listeners)
			ls.onClose(name);
		
	}
	
	/**
	 * y-a-il un port ouvert???
	 * @return
	 */
	public boolean hasOpendPort () {
		return port != null && portName != null;
	}
	
	/**
	 * ecriture d'un message text sur le port actuelement ouvert
	 * @param message
	 * @throws IOException
	 */
	public synchronized void write (String message) throws IOException {
		if (!hasOpendPort())
			throw new IOException("Aucun port n'est ouvert");
	}
	
	/**
	 * abonnement d'un nouveau listener
	 * @param listener
	 */
	public void addArduinoListener(ArduinoListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	/**
	 * desabonnement d'un listener
	 * @param listener
	 */
	public void removeArduinoListener (ArduinoListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() != SerialPortEvent.DATA_AVAILABLE)
	         return;
		
		try {
			String message = "";
			byte [] buffer = new byte [1024];
			
			InputStream in = port.getInputStream();
			int ln = 0;
			while ((ln = in.read(buffer)) !=  0)
				message += new String(buffer, 0, ln);
			
			/**
			 * deux cas sont possible
			 * -message respecte le format clee -> valuer
			 * -message banal au format not prise en charge
			 * 
			 * ==
			 * format du message
			 * Ex: key1:value1;key2:value2;key_n:valuen
			 */
			
			if(message.contains(":") && !message.startsWith(":")) {//pour un message formater
				Map<String, String> values = new HashMap<>();
				String [] fragments = message.split(";");//separation de fragement (a chaque fois qu'on croise un ; (point virgule)
				for (String fragment : fragments) {//separation des clees aux valeurs
					String [] v = fragment.split(":");
					if(v == null || v.length == 0)//dans cas où le text ne respecte pas le format key:value on l'ignore
						continue;
					
					values.put(v[0], v.length >= 2? v[1] : "");
				}
				
				for(ArduinoListener ls : listeners)
					ls.onRead(values);
			} else {				
				for(ArduinoListener ls : listeners)
					ls.onRead(message);
			}
			
			
		} catch (Exception e) {
			for(ArduinoListener ls : listeners)
				ls.onError(e);
		}
	}

	
	@Override
	public void run() {
		ports.clear();
		while (running) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}

			ArrayList<String> currentPorts = new ArrayList<>();
			
			Set<String> pNames = NRSerialPort.getAvailableSerialPorts();
			for (String p : pNames) {
				currentPorts.add(p);
			}

			if (ports.equals(currentPorts))
				continue;
			
			if (hasOpendPort() && !currentPorts.contains(portName)) 
				disposeCurrentPort();
				
			ports.clear();
			ports.addAll(currentPorts);
			
			String [] emit = new String[ports.size()];
			emit = ports.toArray(emit);
			for(ArduinoListener ls : listeners)
				ls.onConnectedPortChange(emit);
		}
		
		ports.clear();
		for(ArduinoListener ls : listeners)
			ls.onConnectedPortChange(new String[0]);
	}

}
