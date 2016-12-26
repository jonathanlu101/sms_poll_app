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
	private static final String SEND_SMS_URL = "https://api.telstra.com/v1/sms/messages";
	private static final String SMS_STATUS_URL = "https://api.telstra.com/v1/sms/messages/*";
	private static final String SMS_RESPONSE_URL = "https://api.telstra.com/v1/sms/messages/*/response";
	
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
        	HttpResponse httpResponse = httpClient.execute(get);
        	
        	String httpResponseString = EntityUtils.toString(httpResponse.getEntity());
        	UpdateMessageStatusResponse response = objectMapper.readValue(httpResponseString,UpdateMessageStatusResponse.class);
        	
        	msg.setSentTimestamp(response.getSentTimestamp());
        	msg.setReceivedTimestamp(response.getReceivedTimestamp());
        	msg.setStatus(response.getStatus());
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
	
}
