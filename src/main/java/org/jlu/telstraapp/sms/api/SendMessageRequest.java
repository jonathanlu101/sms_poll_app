package org.jlu.telstraapp.sms.api;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SendMessageRequest {
	
	private String to;
	private String body;
	
	public SendMessageRequest (String mobile, String message) {
		to = mobile;
		body = message;
	}
	
    @JsonGetter("body")
    public String getBody ( ) { 
        return this.body;
    }
    
    @JsonSetter("body")
    public void setBody (String value) { 
        this.body = value;
    }
 
    @JsonGetter("to")
    public String getTo ( ) { 
        return this.to;
    }
    
    @JsonSetter("to")
    public void setTo (String value) { 
        this.to = value;
    }
}
