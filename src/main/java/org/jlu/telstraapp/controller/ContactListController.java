package org.jlu.telstraapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.jlu.telstraapp.contact.Contact;
import org.jlu.telstraapp.contact.ContactList;
import org.jlu.telstraapp.contact.dao.ContactListDao;
import org.jlu.telstraapp.contact.exception.UnauthorisedToAccessContactListException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/contactlist")
public class ContactListController {
	
	@Autowired
	private ContactListDao contactListDao;
	
	public ContactListController(){
		
	}
	
	@RequestMapping(value="/test",method= RequestMethod.GET)
	@ResponseBody
	public void test() throws UnauthorisedToAccessContactListException{
		ContactList contactList = contactListDao.get(2);
		System.out.println(contactList.getContacts().size());
	}

	@RequestMapping(method= RequestMethod.GET)
	@ResponseBody
	public List<ContactList> getAllContactList(){
		String userName = SecurityContextHolder
	    		.getContext()
	    		.getAuthentication()
	    		.getName();
		List<ContactList> listOfContactlists = contactListDao.getAll(userName);
		return listOfContactlists;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Serializable createContactList(@RequestBody ContactList contactList){
		String userName = SecurityContextHolder
	    		.getContext()
	    		.getAuthentication()
	    		.getName();
		contactList.setUserName(userName);
		return contactListDao.save(contactList);
	}
	
	@RequestMapping(value="/{contactListId}",method= RequestMethod.GET)
	@ResponseBody
	public ContactList viewContactList(@PathVariable String contactListId) throws NumberFormatException, UnauthorisedToAccessContactListException{
		ContactList contactList = contactListDao.get(Integer.parseInt(contactListId));
		String userName = SecurityContextHolder
	    		.getContext()
	    		.getAuthentication()
	    		.getName();
		if (contactList.getUserName().equals(userName)){
			return contactList;
		}else{
			throw new UnauthorisedToAccessContactListException();
		}
	}
	
	@RequestMapping(value="/{contactListId}",method= RequestMethod.PUT)
	@ResponseBody
	public ContactList editContactList(@PathVariable String contactListId,@RequestBody ContactList newContactList) throws NumberFormatException, UnauthorisedToAccessContactListException{
		ContactList oldContactList = contactListDao.get(newContactList.getContactListId());
		String userName = SecurityContextHolder
	    		.getContext()
	    		.getAuthentication()
	    		.getName();
		if (oldContactList.getUserName().equals(userName)){
			contactListDao.update(newContactList);
			return contactListDao.get(newContactList.getContactListId());
		}else{
			throw new UnauthorisedToAccessContactListException();
		}
	}
	
	@RequestMapping(value="/{contactListId}",method= RequestMethod.DELETE)
	@ResponseBody
	public void deleteContactList(@PathVariable String contactListId) throws NumberFormatException, UnauthorisedToAccessContactListException{
		ContactList contactList = contactListDao.get(Integer.parseInt(contactListId));
		String userName = SecurityContextHolder
	    		.getContext()
	    		.getAuthentication()
	    		.getName();
		if (contactList.getUserName().equals(userName)){
			contactListDao.delete(contactList);
		}else{
			throw new UnauthorisedToAccessContactListException();
		}
	}
	
	public void setContactListDao(ContactListDao contactListDao) {
		this.contactListDao = contactListDao;
	}
}
