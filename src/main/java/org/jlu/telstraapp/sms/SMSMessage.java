package org.jlu.telstraapp.sms;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hibernate.annotations.OrderBy;
import org.jlu.telstraapp.sms.api.SendMessageRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name="sms_message")
@Inheritance(strategy=InheritanceType.JOINED)
public class SMSMessage {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="sms_message_id")
	protected int smsMessageId;
	@Column (name="user_name")
	protected String userName; 
	@Column(name="status")
	protected String status = "NOT_STARTED";
	@Column(name="mobile_number")
	protected String mobileNumber;
	@Column(name="message")
	protected String message;
	@Column(name="message_id")
	protected String messageId;
	@Column(name="sent_timestamp")
	protected String sentTimestamp;
	@Column(name="received_timestamp")
	protected String receivedTimestamp;
	@OneToMany(mappedBy="smsMessage",fetch = FetchType.EAGER)
	@OrderBy(clause="acknowledged_timestamp desc")
	protected List<SMSResponse> smsResponses = new ArrayList();

	public SMSMessage() {
		
	}
	
	public SMSMessage(String userName,String mobileNumber, String message) {
		this.userName = userName;
		this.mobileNumber = mobileNumber;
    	this.message = message;
	}
	
	public int getSmsMessageId() {
		return smsMessageId;
	}

	public void setSmsMessageId(int smsMessageId) {
		this.smsMessageId = smsMessageId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSentTimestamp() {
		return sentTimestamp;
	}

	public void setSentTimestamp(String sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	public String getReceivedTimestamp() {
		return receivedTimestamp;
	}

	public void setReceivedTimestamp(String receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
	}

	public String getStatus() {
		return status;
	}

	public String getMessageId() {
		return messageId;
	}

    public void setMessageId(String value){
    	this.messageId = value;
    }
    
    public void setStatus(String value){
    	this.status = value;
    }
    
	public List<SMSResponse> getSmsResponses() {
		return smsResponses;
	}

	public void setSmsResponses(List<SMSResponse> smsResponses) {
		this.smsResponses = smsResponses;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
