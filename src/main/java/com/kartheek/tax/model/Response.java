package com.kartheek.tax.model;

import java.io.Serializable;
import java.util.Calendar;

public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar time;
	private String message;

	public Response(Calendar time, String message) {
		this.time = time;
		this.message = message;
	}

	/**
	 * @return the time
	 */
	public Calendar getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Calendar time) {
		this.time = time;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
