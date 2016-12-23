package org.jlu.telstraapp.contact.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jlu.telstraapp.contact.Contact;
import org.jlu.telstraapp.contact.ContactList;
import org.jlu.telstraapp.contact.exception.UnauthorisedToAccessContactListException;
import org.jlu.telstraapp.sms.SMSMessage;
import org.jlu.telstraapp.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

public class ContactListDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	@Transactional
	public Serializable save(ContactList contactList){
		Session session = sessionFactory.getCurrentSession();
		for (Contact contact : contactList.getContacts()){
			contact.setContactList(contactList);
		}
		Serializable id = session.save(contactList);
		return id;
	}
	
	@Transactional
	public List<ContactList> getAll(String userName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ContactList where userName = :username");
		query.setParameter("username", userName);
		return query.getResultList();
	}
	
	@Transactional
	public ContactList get(int contactListId){
		Session session = sessionFactory.getCurrentSession();
		return session.get(ContactList.class, contactListId);
	}
	
	@Transactional
	public void delete(ContactList contactList) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(contactList);
	}
	
	@Transactional
	public void update(ContactList contactList){
		Session session = sessionFactory.getCurrentSession();
		Query deleteContactsQuery = session.createQuery("delete Contact where contactList = :contactList");
		deleteContactsQuery.setParameter("contactList", contactList);
		deleteContactsQuery.executeUpdate();
		for (Contact contact : contactList.getContacts()){
			contact.setContactList(contactList);
			session.save(contact);
		}
		session.saveOrUpdate(contactList);
	}
}
