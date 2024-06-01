package com.kartheek.tax;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.kartheek.tax.model.Employee;
import com.kartheek.tax.model.TaxCalculationModel;
import com.kartheek.tax.model.TaxCalculationResponse;
import com.kartheek.tax.utils.TaxUtils;

@Service
public class CalculationService {

	private Map<String, Employee> employees = new HashMap<String, Employee>();

	public JsonNode saveEmployee(Employee employee) {
		employees.put(employee.getEmployeeId(), employee);
		return null;
	}

	public TaxCalculationResponse calculateTax(String employeeId) {
		Employee employee = employees.get(employeeId);
		TaxCalculationResponse response = new TaxCalculationResponse(Calendar.getInstance(),
				"Tax Calculated Successfully");
		response.setEmployee(employee);
		response.setTax(calculateTax(calculateCurrentFySalary(employee.getDoj(), employee.getSalary())));
		return response;
	}

	private Double calculateCurrentFySalary(Calendar joinedDate, Double salary) {
		Calendar currentDate = Calendar.getInstance();
		Calendar currentFyAprDate = new Calendar.Builder().setDate(currentDate.get(Calendar.YEAR), Calendar.APRIL, 1)
				.build();
		Double totalSalary = 0d;
		if (joinedDate.get(Calendar.YEAR) < currentDate.get(Calendar.YEAR)) {
			Integer noOfFullMonthsWorked = currentDate.get(Calendar.MONTH) - currentFyAprDate.get(Calendar.MONTH);
			Integer noOfCurrentMonthWorkingDays = currentDate.get(Calendar.DATE) - currentFyAprDate.get(Calendar.DATE);
			totalSalary = calculateFullWorkingMonthSalary(noOfFullMonthsWorked, salary)
					+ calculatePartialMonthSalary(noOfCurrentMonthWorkingDays, salary);
		} else {
			Integer noOfFullMonthsWorked = (currentDate.get(Calendar.MONTH) + 1) - currentFyAprDate.get(Calendar.MONTH);
			Integer noOfCurrentMonthWorkingDays = currentDate.get(Calendar.DATE) - currentFyAprDate.get(Calendar.DATE);
			Integer noOfJoinedMonthWorkingDays = 30 - joinedDate.get(Calendar.DATE);
			totalSalary = calculatePartialMonthSalary(noOfJoinedMonthWorkingDays, salary)
					+ calculateFullWorkingMonthSalary(noOfFullMonthsWorked, salary)
					+ calculatePartialMonthSalary(noOfCurrentMonthWorkingDays, salary);

		}
		return totalSalary;
	}

	private Double calculatePartialMonthSalary(Integer numberOfWorkingDays, Double salary) {
		Integer noOfDaysOfMonth = 30;
		return (salary / noOfDaysOfMonth) * numberOfWorkingDays;
	}

	private Double calculateFullWorkingMonthSalary(Integer numberOfFullWorkingMonths, Double salary) {
		return salary * numberOfFullWorkingMonths;
	}

	private Double calculateTax(Double salary) {

		if (salary < 250000) {
			return 0d;
		}

		final TaxCalculationModel calculationModel = new TaxCalculationModel(salary);
		Stream<Double> incomeSlabs = TaxUtils.taxSlabs.keySet().stream();
		incomeSlabs.forEach(taxSlab -> {
			if (salary > taxSlab) {
				calculationModel.setTax((calculationModel.getSalary() - taxSlab) * TaxUtils.taxSlabs.get(taxSlab));
				if (calculationModel.getSalary() < TaxUtils.ADDITIONAL_CESS_AMOUNT_LIMIT) {
					calculationModel.setSalary(taxSlab);
				}
			}
		});

		return calculationModel.getTax();
	}

}
