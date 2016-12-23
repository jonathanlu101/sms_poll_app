package org.jlu.telstraapp.sms.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jlu.telstraapp.sms.SMSMessage;
import org.jlu.telstraapp.sms.SMSResponse;
import org.jlu.telstraapp.sms.api.callback.SMSCallBackRequest;
import org.jlu.telstraapp.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class SMSResponseDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	@Transactional
	public void save(SMSResponse response){
		Session session = sessionFactory.getCurrentSession();
		session.save(response);
	}
	
	@Transactional
	public void save(List<SMSResponse> smsResponses){
		Session session = sessionFactory.getCurrentSession();
		for (SMSResponse response : smsResponses){
			session.save(response);
		}
	}
	
	@Transactional
	public void save(SMSCallBackRequest callBackRequest){
		Session session = sessionFactory.getCurrentSession();
		SMSMessage smsMessage = (SMSMessage) session
				.createQuery("from SMSMessage s where s.messageId=?")
				.setParameter(0, callBackRequest.getMessageId())
				.getResultList()
				.get(0);
		session.save(new SMSResponse(
				smsMessage,smsMessage.getMobileNumber()
				,callBackRequest.getAcknowledgedTimestamp()
				,callBackRequest.getContent()));
	}

}