package org.jlu.telstraapp.sms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@Table(name = "sms_response")
public class SMSResponse {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="sms_response_id")
	private int smsResponseId;
	@Column(name="origin")
	private String from;
	@Column(name="acknowledged_timestamp")
	private String acknowledgedTimestamp;
	@Column(name="content")
	private String content;
	@ManyToOne
	@JoinColumn(name="sms_message_id")
	@JsonIgnore
	private SMSMessage smsMessage;
	
	public SMSResponse(){
		
	}
	
	public SMSResponse(SMSMessage smsMessage, String from, String acknowledgedTimestamp,
			String content) {
		this.smsResponseId = smsResponseId;
		this.smsMessage = smsMessage;
		this.from = from;
		this.acknowledgedTimestamp = acknowledgedTimestamp;
		this.content = content;
	}
	
    @JsonGetter("from")
    public String getFrom ( ) { 
        return this.from;
    }
    
    @JsonSetter("from")
    public void setFrom (String value) { 
        this.from = value;
    }
 
    @JsonGetter("acknowledgedTimestamp")
    public String getAcknowledgedTimestamp ( ) { 
        return this.acknowledgedTimestamp;
    }
    
    @JsonSetter("acknowledgedTimestamp")
    public void setToAcknowledgedTimestamp (String value) { 
        this.acknowledgedTimestamp = value;
    }
    
    @JsonGetter("content")
    public String getContent ( ) { 
        return this.content;
    }
    
    @JsonSetter("content")
    public void setContent (String value) { 
        this.content = value;
    }
    
	public SMSMessage getSmsMessage() {
		return smsMessage;
	}

	public void setSmsMessage(SMSMessage smsMessage) {
		this.smsMessage = smsMessage;
	}
	
	public int getSmsResponseId() {
		return smsResponseId;
	}

	public void setSmsResponseId(int sms_response_id) {
		this.smsResponseId = sms_response_id;
	}
}
