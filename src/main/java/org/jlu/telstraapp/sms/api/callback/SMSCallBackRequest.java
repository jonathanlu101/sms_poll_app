package org.jlu.telstraapp.sms.api.callback;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SMSCallBackRequest {

	private String messageId;
	private String status;
	private String acknowledgedTimestamp;
	private String content;
	
	
	public SMSCallBackRequest(String messageId, String status, String acknowledgedTimestamp, String content) {
		this.messageId = messageId;
		this.status = status;
		this.acknowledgedTimestamp = acknowledgedTimestamp;
		this.content = content;
	}
	
	public SMSCallBackRequest() {
	}
	
	@JsonGetter("messageId")
	public String getMessageId() {
		return messageId;
	}
	
	@JsonSetter("messageId")
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	@JsonGetter("status")
	public String getStatus() {
		return status;
	}
	
	@JsonSetter("status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonGetter("acknowledgedTimestamp")
	public String getAcknowledgedTimestamp() {
		return acknowledgedTimestamp;
	}
	
	@JsonSetter("acknowledgedTimestamp")
	public void setAcknowledgedTimestamp(String acknowledgedTimestamp) {
		this.acknowledgedTimestamp = acknowledgedTimestamp;
	}
	
	@JsonGetter("content")
	public String getContent() {
		return content;
	}
	
	@JsonSetter("content")
	public void setContent(String content) {
		this.content = content;
	}

}

