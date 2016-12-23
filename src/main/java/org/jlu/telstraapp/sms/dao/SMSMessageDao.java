package org.jlu.telstraapp.sms.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jlu.telstraapp.contact.ContactList;
import org.jlu.telstraapp.sms.SMSMessage;
import org.jlu.telstraapp.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class SMSMessageDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	@Transactional
	public void save(SMSMessage message){
		Session session = sessionFactory.getCurrentSession();
		session.save(message);
	}
	
	@Transactional
	public void save(Object obj){
		Session session = sessionFactory.getCurrentSession();
		session.save(obj);
	}
	
}