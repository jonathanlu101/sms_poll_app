package org.jlu.telstraapp.sms.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jlu.telstraapp.sms.SMSMessage;
import org.jlu.telstraapp.sms.SMSResponse;
import org.jlu.telstraapp.sms.dao.SMSMessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SMSApiClient {
	
	private ObjectMapper objectMapper;
	private HttpClient httpClient = HttpClientBuilder.create().build();
	private String SEND_SMS_URL;
	private String SMS_STATUS_URL;
	private String SMS_RESPONSE_URL;
	
	public SMSApiClient(){
		
	}
	
	public void sendMessage(String authenticationKey,SMSMessage msg) throws Exception{
		
    	SendMessageRequest sendRequest = new SendMessageRequest(msg.getMobileNumber(),msg.getMessage());
    	String sendRequestString = objectMapper.writeValueAsString(sendRequest);
    	HttpPost post = new HttpPost(SEND_SMS_URL);
    	post.setHeader("Authorization", authenticationKey);
    	post.setHeader("Content-Type", "application/x-www-form-urlencoded");
    	post.setEntity(new StringEntity(sendRequestString));
    	HttpResponse response = httpClient.execute(post);
    	if (response.getStatusLine().getStatusCode() == 200 ||
    		response.getStatusLine().getStatusCode() == 202){
    		String responseString = EntityUtils.toString(response.getEntity());
        	System.out.println(responseString);
        	JsonNode mainNode = objectMapper.readTree(responseString);
        	msg.setMessageId(mainNode.get("messageId").asText());
        	msg.setStatus("STARTED");
    	}
    	else{
    		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
    	}
    }
	
	public void updateSMSStatus(String authenticationKey,SMSMessage msg) throws Exception{
    	if (!msg.getStatus().equals("NOT_STARTED")){
    		
    		String getURL = SMS_STATUS_URL.replace("*",msg.getMessageId());
        	HttpGet get = new HttpGet(getURL);
        	get.setHeader("Authorization", authenticationKey);
        	HttpResponse response = httpClient.execute(get);
        	
        	String output = EntityUtils.toString(response.getEntity());
        	JsonNode mainNode = objectMapper.readTree(output);
        	
        	msg.setSentTimestamp(mainNode.get("sentTimestamp").asText());
        	msg.setReceivedTimestamp(mainNode.get("receivedTimestamp").asText());
        	msg.setStatus(mainNode.get("status").asText());
    	}
    }
    
    public void updateSMSResponse(String authenticationKey,SMSMessage msg) throws Exception{
    	if (!msg.getStatus().equals("READ")){
    		updateSMSStatus(authenticationKey,msg);
    	}
    	if (msg.getStatus().equals("READ")){
    		
    		String getURL = SMS_RESPONSE_URL.replace("*", msg.getMessageId());
        	HttpGet get = new HttpGet(getURL);
        	get.setHeader("Authorization", authenticationKey);
        	HttpResponse response = httpClient.execute(get);
        	
        	String output = EntityUtils.toString(response.getEntity());
        	List<SMSResponse> responses = objectMapper.readValue(output,new TypeReference<List<SMSResponse>>() {});
        	for (SMSResponse item : responses){
        		item.setSmsMessage(msg);
        	}
        	msg.setSmsResponses(responses);
    	}
    }
    
    public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public String getSEND_SMS_URL() {
		return SEND_SMS_URL;
	}

	public void setSEND_SMS_URL(String sEND_SMS_URL) {
		SEND_SMS_URL = sEND_SMS_URL;
	}

	public String getSMS_STATUS_URL() {
		return SMS_STATUS_URL;
	}

	public void setSMS_STATUS_URL(String sMS_STATUS_URL) {
		SMS_STATUS_URL = sMS_STATUS_URL;
	}

	public String getSMS_RESPONSE_URL() {
		return SMS_RESPONSE_URL;
	}

	public void setSMS_RESPONSE_URL(String sMS_RESPONSE_URL) {
		SMS_RESPONSE_URL = sMS_RESPONSE_URL;
	}
}