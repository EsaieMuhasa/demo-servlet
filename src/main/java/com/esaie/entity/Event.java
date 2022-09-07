package com.esaie.entity;

import java.util.Date;

public class Event {
	
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
