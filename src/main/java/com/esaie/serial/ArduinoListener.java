/**
 * 
 */
package com.esaie.serial;

import java.util.Map;

/**
 * @author Esaie Muhasa
 *
 */
public interface ArduinoListener {
	
	void onOpen (String portName);
	void onClose (String portName);
	void onError (Exception e);
	
	void onRead (String message);
	void onRead (Map<String, String> message);
	
	void onWrite (String message);
	
	void onConnectedPortChange(String [] ports);
}
