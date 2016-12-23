package org.jlu.telstraapp.poll.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jlu.telstraapp.contact.Contact;
import org.jlu.telstraapp.contact.ContactList;
import org.jlu.telstraapp.contact.exception.UnauthorisedToAccessContactListException;
import org.jlu.telstraapp.poll.Poll;
import org.jlu.telstraapp.poll.PollMessage;
import org.jlu.telstraapp.poll.PollOption;
import org.jlu.telstraapp.sms.SMSMessage;
import org.jlu.telstraapp.sms.SMSResponse;
import org.jlu.telstraapp.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

public class PollDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	@Transactional
	public Poll get(Integer pollId){
		Session session = sessionFactory.getCurrentSession();
		return session.get(Poll.class, pollId);
	}
	
	@Transactional
	public List<Poll> getAll(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Poll where userName = :username");
		query.setParameter("username", userName);
		return query.getResultList();
	}
    
    @Transactional
	public Serializable save(Poll poll){
		Session session = sessionFactory.getCurrentSession();
		Serializable id = session.save(poll);
		for (PollMessage pollMessage: poll.getMessages()){
			pollMessage.setPoll(poll);
			session.save(pollMessage);
			for (SMSResponse response: pollMessage.getSmsResponses()){
				response.setSmsMessage(pollMessage);
				session.save(response);
			}
		}
		for (PollOption pollOption: poll.getOptions()){
			pollOption.setPoll(poll);
			session.save(pollOption);
		}
		return id;
	}
	
    
	@Transactional
	public void update(Poll poll){
		Session session = sessionFactory.getCurrentSession();
		for (PollMessage pollMessage: poll.getMessages()){
			pollMessage.setPoll(poll);
			session.saveOrUpdate(pollMessage);
			for (SMSResponse response: pollMessage.getSmsResponses()){
				response.setSmsMessage(pollMessage);
				session.saveOrUpdate(response);
			}
		}
		for (PollOption pollOption: poll.getOptions()){
			pollOption.setPoll(poll);
			session.saveOrUpdate(pollOption);
		}
		session.update(poll);
	}
	
	@Transactional
	public void delete(Poll poll) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(poll);
	}
}
