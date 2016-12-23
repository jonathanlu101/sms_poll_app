package org.jlu.telstraapp.contact.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED,reason="You are not authorized to access this contact list")
public class UnauthorisedToAccessContactListException extends Exception {
	private static final long serialVersionUID = 100L;
}