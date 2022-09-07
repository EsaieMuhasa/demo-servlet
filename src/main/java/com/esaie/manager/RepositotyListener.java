/**
 * 
 */
package com.esaie.manager;

import com.esaie.entity.Event;

/**
 * @author Esaie MUHASA
 * interface d'ecoute des changements d'etat des donnees dans le repository
 */
public interface RepositotyListener {
	/**
	 * evenement d'insersion d'un Event dans la BDD
	 * @param event
	 */
	void onCreate(Event event);
	
	/**
	 * lors de la mis en jour d'un evenement
	 * @param event
	 */
	void onUpadate(Event event);
	
	/**
	 * lors de la suppression de(s) Event(s)
	 * @param keys
	 */
	void onDelete (int...keys);
}
