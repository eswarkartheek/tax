package com.kartheek.tax.model;

import java.util.Calendar;

public class TaxCalculationResponse extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;
	private double tax;

	public TaxCalculationResponse(Calendar time, String message) {
		super(time, message);
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the tax
	 */
	public double getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(double tax) {
		this.tax = tax;
	}

}
