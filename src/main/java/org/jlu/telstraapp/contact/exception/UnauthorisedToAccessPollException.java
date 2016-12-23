package org.jlu.telstraapp.contact.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED,reason="You are not authorized to access this poll")
public class UnauthorisedToAccessPollException extends Exception{
	private static final long serialVersionUID = 100L;
}
