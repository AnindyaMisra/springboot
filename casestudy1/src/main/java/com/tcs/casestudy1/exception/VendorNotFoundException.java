package com.tcs.casestudy1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This vendor is not found in the system")
public class VendorNotFoundException extends Exception {
	private static final long serialVersionUID = 100L;
}