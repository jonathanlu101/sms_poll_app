package org.jlu.telstraapp.sms.api;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class UpdateMessageStatusResponse {
	
	private String to;
	private String receivedTimestamp;
	private String sentTimestamp;
	private String status;
	
	
	public UpdateMessageStatusResponse (String mobile, String message) {
		to = mobile;
	}

	@JsonGetter("to")
	public String getTo() {
		return to;
	}

	@JsonSetter("to")
	public void setTo(String to) {
		this.to = to;
	}

	@JsonGetter("receivedTimestamp")
	public String getReceivedTimestamp() {
		return receivedTimestamp;
	}

	@JsonSetter("receivedTimestamp")
	public void setReceivedTimestamp(String receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
	}

	@JsonGetter("sentTimestamp")
	public String getSentTimestamp() {
		return sentTimestamp;
	}

	@JsonSetter("sentTimestamp")
	public void setSentTimestamp(String sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	@JsonGetter("status")
	public String getStatus() {
		return status;
	}

	@JsonSetter("status")
	public void setStatus(String status) {
		this.status = status;
	}
	
    
}