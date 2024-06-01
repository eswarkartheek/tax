package com.kartheek.tax;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.kartheek.tax.model.Constants;
import com.kartheek.tax.model.Employee;
import com.kartheek.tax.model.TaxCalculationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class CalculationController implements Constants {

	private static final Logger logger = LoggerFactory.getLogger(CalculationController.class);

	@Autowired
	private CalculationService calculationService;

	@PostMapping(value = "/employee", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "API endpoint to store employee details", description = "API endpoint to store employee details")
	@RequestBody(content = { @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE) })
	@Parameters(value = {
			@Parameter(in = ParameterIn.QUERY, name = EMPLOYEE_ID, description = "ID of the Employee", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = EMPLOYEE_ID, description = "Employee ID", value = "HYD001") }),
			@Parameter(in = ParameterIn.QUERY, name = FIRST_NAME, description = "First Name of the Employee", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = FIRST_NAME, description = "First name", value = "Eswar") }),
			@Parameter(in = ParameterIn.QUERY, name = LAST_NAME, description = "Last Name of the Employee", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = LAST_NAME, description = "Last Name", value = "Kartheek") }),
			@Parameter(in = ParameterIn.QUERY, name = EMAIL, description = "Email of the Employee", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = EMAIL, description = "Email Id", value = "eswarkartheek90@gmail.com") }),
			@Parameter(in = ParameterIn.QUERY, name = PHONE_NUMBERS, description = "Phone Numbers of Employee in comma separated", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = PHONE_NUMBERS, description = "Phone Numbers", value = "123456789,987654321") }),
			@Parameter(in = ParameterIn.QUERY, name = DOJ, description = "Joining Date of the Employee", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = DOJ, description = "Joining Date", value = "01-01-2023") }),
			@Parameter(in = ParameterIn.QUERY, name = SALARY, description = "Salary of the Employee", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = SALARY, description = "Salary", value = "90000") }), })
	public ResponseEntity<JsonNode> saveEmployee(@RequestParam(value = EMPLOYEE_ID) String employeeId,
			@RequestParam(value = FIRST_NAME) String firstName, @RequestParam(value = LAST_NAME) String lastName,
			@RequestParam(value = EMAIL) String email, @RequestParam(value = PHONE_NUMBERS) String phoneNumber,
			@RequestParam(value = DOJ) String doj, @RequestParam(value = SALARY) Integer salary) {

		Calendar dojCalemdar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault());
		try {
			dojCalemdar.setTime(sdf.parse(doj));
		} catch (ParseException e) {
			logger.error("Invalid Date");
		}
		Employee employee = new Employee(employeeId, firstName, lastName, email,
				Arrays.stream(phoneNumber.split(",")).toList(), dojCalemdar, salary);

		calculationService.saveEmployee(employee);
		return null;

	}

	@GetMapping(value = "/employee", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "API endpoint to return employees tax deduction for the current financial year(April to March).", description = "API endpoint to return employees tax deduction for the current financial year(April to March).")
	@Parameters(value = {
			@Parameter(in = ParameterIn.QUERY, name = EMPLOYEE_ID, description = "ID of the Employee", style = ParameterStyle.FORM, schema = @Schema(type = "string", format = "string"), examples = {
					@ExampleObject(name = EMPLOYEE_ID, description = "Employee ID", value = "HYD001") }) })
	public ResponseEntity<TaxCalculationResponse> calculateTax(@RequestParam(value = EMPLOYEE_ID) String employeeId) {
		TaxCalculationResponse taxCalculationResponse = calculationService.calculateTax(employeeId);
		return new ResponseEntity<TaxCalculationResponse>(taxCalculationResponse, HttpStatus.OK);

	}

}
