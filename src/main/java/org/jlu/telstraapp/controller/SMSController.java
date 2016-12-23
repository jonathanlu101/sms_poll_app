package org.jlu.telstraapp.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jlu.telstraapp.authenticator.Authenticator;
import org.jlu.telstraapp.sms.SMSMessage;
import org.jlu.telstraapp.sms.SMSResponse;
import org.jlu.telstraapp.sms.api.SMSApiClient;
import org.jlu.telstraapp.sms.api.callback.SMSCallBackRequest;
import org.jlu.telstraapp.sms.dao.SMSMessageDao;
import org.jlu.telstraapp.sms.dao.SMSResponseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController

public class SMSController {
	
	@Autowired
	private SMSMessageDao smsMessageDao;
	@Autowired
	private SMSResponseDao smsResponseDao;
	@Autowired
	private SMSApiClient smsApiClient;

	@RequestMapping(value="/callback/smsresponse", method=RequestMethod.POST, consumes = "application/json")
	public void smsCallBack(@RequestBody SMSCallBackRequest smsCallBackRequest) {
		smsResponseDao.save(smsCallBackRequest);
	}
	
}