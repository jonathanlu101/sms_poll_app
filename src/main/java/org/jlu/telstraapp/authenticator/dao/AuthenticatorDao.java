package org.jlu.telstraapp.authenticator.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jlu.telstraapp.authenticator.Authenticator;
import org.jlu.telstraapp.contact.Contact;
import org.jlu.telstraapp.contact.ContactList;
import org.jlu.telstraapp.contact.exception.UnauthorisedToAccessContactListException;
import org.jlu.telstraapp.poll.Poll;
import org.jlu.telstraapp.poll.PollMessage;
import org.jlu.telstraapp.poll.PollOption;
import org.jlu.telstraapp.sms.SMSMessage;
import org.jlu.telstraapp.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

public class AuthenticatorDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	@Transactional
	public Serializable save(Authenticator authenticator){
		Session session = sessionFactory.getCurrentSession();
		Serializable id = session.save(authenticator);
		return id;
	}
	
	@Transactional
	public void update(Authenticator authenticator){
		Session session = sessionFactory.getCurrentSession();
		session.update(authenticator);
	}
	
	@Transactional
	public Authenticator get(String userName){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Authenticator where userName = :username");
		query.setParameter("username", userName);
		return (Authenticator) query.getResultList().get(0);
	}
}
