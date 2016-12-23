package org.jlu.telstraapp.poll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jlu.telstraapp.contact.Contact;
import org.jlu.telstraapp.sms.SMSMessage;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="poll_message")
public class PollMessage extends SMSMessage {
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="poll_id")
	private Poll poll;
	@OneToOne
	@JoinColumn(name="chosen_option")
	private PollOption chosenOption;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	
	public PollMessage(){
		
	}
	
	public PollMessage(String firstName, String lastName,String mobileNumber,Poll poll,String userName){
		this.firstName = firstName;
		this.lastName = lastName;
		super.mobileNumber = mobileNumber;
		this.poll = poll;
		this.message = poll.getQuestion();
		this.userName = userName;
	}
	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Poll getPoll() {
		return poll;
	}
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	public PollOption getChosenOption() {
		return chosenOption;
	}
	public void setChosenOption(PollOption chosenOption) {
		this.chosenOption = chosenOption;
	}

}
