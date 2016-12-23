package org.jlu.telstraapp.poll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.jlu.telstraapp.contact.Contact;
import org.jlu.telstraapp.contact.ContactList;
import org.jlu.telstraapp.contact.exception.UnauthorisedToAccessContactListException;


@Entity
@Table(name="poll")
public class Poll {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="poll_id")
	private int pollId;
	@Column(name="name")
	private String name;
	@Column(name="description")
	private String description;
	@Column(name="user_name")
	private String userName;
	@Column(name="question")
	private String question;
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy="poll",fetch = FetchType.EAGER)
	@Cascade(CascadeType.DELETE)
	private List<PollMessage> messages = new ArrayList();
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy="poll",fetch = FetchType.EAGER)
	@Cascade(CascadeType.DELETE)
	private List<PollOption> options = new ArrayList();
	@Fetch(value = FetchMode.SUBSELECT)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "poll_contact_list", joinColumns = {
			@JoinColumn(name = "poll_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "contact_list_id",
					nullable = false, updatable = false) })
	private Set<ContactList> contactLists = new HashSet<ContactList>();
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Set<ContactList> getContactLists() {
		return contactLists;
	}
	public void setContactLists(Set<ContactList> contactLists) {
		this.contactLists = contactLists;
	}
	public List<PollOption> getOptions() {
		return options;
	}
	public void setOptions(List<PollOption> options) {
		this.options = options;
	}
	public int getPollId() {
		return pollId;
	}
	public void setPollId(int pollId) {
		this.pollId = pollId;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PollMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<PollMessage> messages) {
		this.messages = messages;
	}
	
	public void createMessages(Set<ContactList> contactLists){
		Map<String,Contact>mobilesMap = new HashMap();
		for (ContactList contactList : contactLists){
			for (Contact contact: contactList.getContacts()){
				mobilesMap.put(contact.getMobile(),contact);
			}
		}
		for (Map.Entry<String,Contact> entry: mobilesMap.entrySet()){
			messages.add(new PollMessage(entry.getValue().getFirstName()
										,entry.getValue().getLastName()
										,entry.getValue().getMobile()
										,this
										,userName));
		}
	}
}
