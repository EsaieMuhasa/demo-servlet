/**
 * 
 */
package com.esaie.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.esaie.entity.Event;

/**
 * @author esaie
 *
 */
public class EventRepository {
	
	private final ArrayList<RepositotyListener> listeners = new ArrayList<>();
	
	private static EventRepository instance;

	/**
	 * 
	 */
	private EventRepository() {
		super();
		loadDriver();
	}
	
	/**
	 * recuperation de l'instance du repos
	 * @return
	 */
	public static EventRepository getInstance () {
		if(instance == null)
			instance = new EventRepository();
		return instance;
	}

	/**
	 * persistance d'un event
	 * @param event
	 */
	public void create (Event event) {
		try (
		 		Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO Event (distance, recordDate) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
			) {

			statement.setDouble(1, event.getDistance());
			statement.setObject(2, event.getRecordDate());
			int status = statement.executeUpdate();
			if(status == 0)
				throw new RuntimeException("Aucun enregistrement faite");
			
			ResultSet result = statement.getGeneratedKeys();
			if(result.next())
				event.setId(result.getInt(1));
			
			result.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		for (RepositotyListener ls : listeners) 
			ls.onCreate(event);
	}

	/**
	 * executer une operation de mis en jours
	 * @param event
	 */
	public void update(Event event) {
		try (
		 		Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("UPDATE Event  SET distance = ?, lastUpdate = ? WHERE id = ?")
			) {

			statement.setDouble(1, event.getDistance());
			statement.setObject(2, event.getLastUpdate());
			statement.setInt(3, event.getId());
			
			int status = statement.executeUpdate();
			if(status == 0)
				throw new RuntimeException("Aucunne mise en jour effectué [ID: "+event.getId()+"]");
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		for (RepositotyListener ls : listeners) 
			ls.onUpadate(event);
	}
	
	/**
	 * supression d'une ou plusieur occurence dans la BDD
	 * @param keys
	 */
	public void delete (int...keys) {
		if (keys.length == 0)
			throw new RuntimeException("donnee aumoin une clee en parametre de la methode delete(int... keys)");
		
		String sql = "DELETE FROM Event WHERE id IN (";//1, 20, 5, 2, 3, 4, 5, 6)
		for (int i = 0; i < keys.length; i++)
			sql += keys[i]+",";
		sql = sql.substring(0, sql.length()-1)+")";

		try (
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
		) {
			int status  = statement.executeUpdate(sql);
			if(status == 0)
				throw new RuntimeException("Aucune suppression faite");
		} catch (SQLException e){
			throw new RuntimeException(e.getMessage(), e);
		}
		
		for (RepositotyListener ls : listeners) 
			ls.onDelete(keys);
	}

	/**
	 * renvoie le nombre d'opperation dans la table
	 * @return
	 */
	public int countAll () {
		try (
			Connection connection = getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery("SELECT COUNT(*) AS nombre FROM Event");
		) {
			if(result.next())
				return result.getInt("nombre");
		} catch (SQLException e){
			throw new RuntimeException(e.getMessage(), e);
		}
		return 0;
	}

	/**
	 * renvoie tout les elements de ladite table dans la base de donnee
	 * @return
	 */
	public Event [] findAll () {
		try (
			Connection connection = getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery("SELECT * FROM Event");
		) {
			return readAll(result);
		} catch (SQLException e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * selection d'un part des donnees de la table Event dans la BDD
	 * @param limit
	 * @param offset
	 * @return
	 */
	public Event [] findAll (int limit, int offset) {
		try (
			Connection connection = getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement.executeQuery("SELECT * FROM Event LIMIT "+limit+" OFFSET "+offset);
		) {
			return readAll(result);
		} catch (SQLException e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * lecture des donnees revoyer par une requette de selection
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	protected Event [] readAll (ResultSet result) throws SQLException{
		int count = result.last()? result.getRow() : 0;
		if(count != 0) {
			result.beforeFirst();
			Event [] data= new Event[count];
			while (result.next()) 
				data[result.getRow() - 1] = new Event(result.getInt("id"), result.getTimestamp("recordDate"), result.getDouble("distance"), result.getTimestamp("lastUpdate"));
			return data;
		}
		throw new RuntimeException("Aucun resultat");		
	}

	/**
	 * chargement du pilote JDBC
	 */
	protected void loadDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * recuperation d'une connection vers le SGBD
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		final String url = "jdbc:mysql://localhost:3306/arduino_db1", user = "root", pass = "";
		return DriverManager.getConnection(url, user, pass);
	}
	
	/**
	 * abonnement d'un ecouteur des changements de l'etat des donnees du repositoty
	 * @param listener
	 */
	public void addRepositoryListener (RepositotyListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	/**
	 * desabonnement d'un ecouteur des changements de l'etat des donnees du repository
	 * @param listener
	 */
	public void removeRepositoryListener (RepositotyListener listener) {
		listeners.remove(listener);
	}

}
