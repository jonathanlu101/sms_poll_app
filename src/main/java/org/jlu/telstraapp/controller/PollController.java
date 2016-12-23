package org.jlu.telstraapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.jlu.telstraapp.authenticator.Authenticator;
import org.jlu.telstraapp.authenticator.dao.AuthenticatorDao;
import org.jlu.telstraapp.contact.Contact;
import org.jlu.telstraapp.contact.ContactList;
import org.jlu.telstraapp.contact.dao.ContactListDao;
import org.jlu.telstraapp.contact.exception.UnauthorisedToAccessContactListException;
import org.jlu.telstraapp.contact.exception.UnauthorisedToAccessPollException;
import org.jlu.telstraapp.poll.Poll;
import org.jlu.telstraapp.poll.PollMessage;
import org.jlu.telstraapp.poll.PollMessageService;
import org.jlu.telstraapp.poll.PollOption;
import org.jlu.telstraapp.poll.dao.PollDao;
import org.jlu.telstraapp.sms.api.SMSApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/poll")
public class PollController {
	
	@Autowired
	private PollDao pollDao;
	@Autowired
	private ContactListDao contactListDao;
	@Autowired
	private AuthenticatorDao authenticatorDao;
	@Autowired
	private SMSApiClient smsApiClient;
	@Autowired
	private PollMessageService pollMessageService;


	public PollController(){
	}
	
	private String getUserName(){
		return SecurityContextHolder
	    		.getContext()
	    		.getAuthentication()
	    		.getName();
	}
	
	@RequestMapping(value="/test",method= RequestMethod.GET)
	@ResponseBody
	public void test() throws Exception{
		String userName = getUserName();
		Poll poll = new Poll();
		PollOption option = new PollOption();
		option.getAlternateValues().add("Yes");
		option.getAlternateValues().add("No");
		poll.getOptions().add(option);
		System.out.println(poll.getOptions().get(0).getAlternateValues().size());
		pollDao.save(poll);
	}
	
	@RequestMapping(method= RequestMethod.POST)
	@ResponseBody
	public Serializable sendPoll(@RequestBody Poll poll) throws Exception{
		String userName = getUserName();
		for (ContactList contactList: poll.getContactLists()){
			ContactList dbContactList = contactListDao.get(contactList.getContactListId());
			if (!(userName).equals(dbContactList.getUserName())){
				throw new UnauthorisedToAccessContactListException();
			}
		}
		Serializable pollId = pollMessageService.sendPoll(poll);
		return pollId;
	}
	
	@RequestMapping(method= RequestMethod.GET)
	@ResponseBody
	public List<Poll> getAllPolls() throws Exception{
		String userName = SecurityContextHolder
	    		.getContext()
	    		.getAuthentication()
	    		.getName();
		List<Poll> listOfPolls = pollDao.getAll(userName);
		return listOfPolls;
	}
	
	@RequestMapping(value = "/{pollId}", method= RequestMethod.GET)
	@ResponseBody
	public Poll getPoll(@PathVariable("pollId") String pollId,
						@RequestParam(value = "updateResponses", required = false, defaultValue = "false") Boolean updateResponses) throws Exception{
		Poll poll = pollDao.get(new Integer(pollId));
		if (!poll.getUserName().equals(getUserName())){
			throw new UnauthorisedToAccessPollException();
		}
		if (updateResponses){
			pollMessageService.updatePollResponses(poll);
		}
		return poll;
	}
	
	@RequestMapping(value="/{pollId}",method= RequestMethod.DELETE)
	@ResponseBody
	public void deletePoll(@PathVariable String pollId) throws NumberFormatException, UnauthorisedToAccessPollException{
		Poll poll = pollDao.get(Integer.parseInt(pollId));
		String userName = getUserName();
		if (poll.getUserName().equals(userName)){
			pollDao.delete(poll);
		}else{
			throw new UnauthorisedToAccessPollException();
		}
	}
	
	public void setPollDao(PollDao pollDao) {
		this.pollDao = pollDao;
	}
	
	public void setContactListDao(ContactListDao contactlistDao){
		this.contactListDao = contactListDao;
	}
	
	public AuthenticatorDao getAuthenticatorDao() {
		return authenticatorDao;
	}

	public void setAuthenticatorDao(AuthenticatorDao authenticatorDao) {
		this.authenticatorDao = authenticatorDao;
	}

	public SMSApiClient getSmsApiClient() {
		return smsApiClient;
	}

	public void setSmsApiClient(SMSApiClient smsApiClient) {
		this.smsApiClient = smsApiClient;
	}

	public PollDao getPollDao() {
		return pollDao;
	}

	public ContactListDao getContactListDao() {
		return contactListDao;
	}
}
