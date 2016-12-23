package org.jlu.telstraapp.poll;

import org.jlu.telstraapp.authenticator.dao.AuthenticatorDao;
import org.jlu.telstraapp.poll.dao.PollDao;
import org.jlu.telstraapp.sms.SMSResponse;
import org.jlu.telstraapp.sms.api.SMSApiClient;
import org.jlu.telstraapp.sms.dao.SMSMessageDao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jlu.telstraapp.authenticator.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PollMessageService {
	
	@Autowired
	private AuthenticatorDao authenticatorDao;
	@Autowired
	private SMSApiClient smsApiClient;
	@Autowired
	private PollDao pollDao;
	@Autowired
	private SMSMessageDao smsMessageDao;
	
	public PollMessageService(){}
	
	private String getAuthKey() throws Exception{
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Authenticator authenticator = authenticatorDao.get(userName);
		authenticatorDao.update(authenticator);
		return authenticator.getAuthenticationKey();
	}
	
	private void FormatOptionValues(PollOption option){
		option.setValue(option.getValue().trim());
		List<String> alternativeValues = option.getAlternateValues();
		for (int i = 0; i < alternativeValues.size(); i++) {
			alternativeValues.set(i, alternativeValues.get(i).trim());
		}
	}
	
	private void updateChosenOption(PollMessage pollMsg, List<PollOption> options){
		for (SMSResponse response : pollMsg.getSmsResponses()){
			String content = response.getContent().trim().toLowerCase();
			Iterator<PollOption> optionsIterator = options.iterator();
			Boolean match = false;
			while(optionsIterator.hasNext() && match == false){
				PollOption option = optionsIterator.next();
				if (content.equals(option.getValue().toLowerCase())){
					pollMsg.setChosenOption(option);
					return;
				} 
				for (String altvalue : option.getAlternateValues()){
					if (content.equals(altvalue.toLowerCase())){
						pollMsg.setChosenOption(option);
						return;
					}
				}
			}
		}
	}
	
	public Serializable sendPoll(Poll poll) throws Exception{
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		String authKey = getAuthKey();
		poll.setUserName(userName);
		poll.createMessages(poll.getContactLists());
		for (PollMessage pollMsg : poll.getMessages()){
			smsApiClient.sendMessage(authKey, pollMsg);
		}
		for (PollOption pollOption : poll.getOptions()){
			FormatOptionValues(pollOption);
		}
		return pollDao.save(poll);
	}
	
	public void updatePollResponses(Poll poll) throws Exception{
		String authKey = getAuthKey();
		for (PollMessage pollMsg : poll.getMessages()){
			smsApiClient.updateSMSResponse(authKey, pollMsg);
			updateChosenOption( pollMsg,poll.getOptions());
		}
		pollDao.update(poll);
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
	
}
