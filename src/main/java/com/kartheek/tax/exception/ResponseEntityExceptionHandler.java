package com.kartheek.tax.exception;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.kartheek.tax.model.Constants;
import com.kartheek.tax.model.Response;

@ControllerAdvice
public class ResponseEntityExceptionHandler extends
		org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler implements Constants {

	private static final Logger logger = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
		logger.error("handleAllExceptions: {}", exception);
		Response response = new Response(Calendar.getInstance(), "Internal Server Error, please try after sometime");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		logger.info("missingServletRequestParameterException called.");
		String missingParam = "'" + ex.getParameterName() + "'";
		logger.error("missingServletRequestParameterException missingParam: {}", missingParam);
		Response response = new Response(Calendar.getInstance(), missingParam + " is missing, please try again with valid value");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}
