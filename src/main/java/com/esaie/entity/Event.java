package com.esaie.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat ("dd/MM/yyyy 'à' hh:mm:ss");
	
	private int id;
	private Date recordDate;
	private double distance;
	private Date lastUpdate;

	public Event() {
		super();
	}

	public Event(int id, Date recordDate, double distance) {
		super();
		this.id = id;
		this.recordDate = recordDate;
		this.distance = distance;
	}

	public Event(int id, Date recordDate, double distance, Date lastUpdate) {
		super();
		this.id = id;
		this.recordDate = recordDate;
		this.distance = distance;
		this.lastUpdate = lastUpdate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getRecordDate() {
		return recordDate;
	}
	
	public String getFormatRecordDate () {
		if(recordDate != null)
			return DATE_FORMAT.format(recordDate);
		return "";
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
