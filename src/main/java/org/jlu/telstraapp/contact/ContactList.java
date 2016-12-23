package org.jlu.telstraapp.contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.jlu.telstraapp.poll.Poll;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="contact_list")
public class ContactList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="contact_list_id")
	private int contactListId;
	@Column (name="name")
	private String name;
	@Column (name="description")
	private String description;
	@Column (name="user_name")
	private String userName;
	@OneToMany(mappedBy="contactList",fetch = FetchType.EAGER)
	private List<Contact> contacts = new ArrayList<Contact>();
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public int getContactListId() {
		return contactListId;
	}
	public void setContactListId(int contactListId) {
		this.contactListId = contactListId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
