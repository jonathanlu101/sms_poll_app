package org.jlu.telstraapp.authenticator;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name="authenticator")
public class Authenticator {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="authenticator_id")
	private Integer Id;
	@Transient
	private static final String authenticationURL = "https://api.telstra.com/v1/oauth/token";
	@Column(name="consumer_key")
	private String consumerKey;
	@Column(name="consumer_secret")
	private String consumerSecret;
	@Column(name="authentication_key")
	private String authenticationKey;
	@Column(name="user_name")
	private String userName;
	@Column(name="expires_in")
	private Timestamp expiresIn;
	
	public Authenticator(){
		
	}
	
	private void updateAuthenticationKey() throws Exception{
		HttpClient client = HttpClientBuilder.create().build();
    	HttpPost post = new HttpPost(authenticationURL);
    	String payload = "client_id=" + consumerKey + "&client_secret=" + consumerSecret + "&grant_type=client_credentials&scope=SMS";
    	StringEntity entity = new StringEntity(payload);
    	entity.setContentType("application/x-www-form-urlencoded");
    	post.setEntity(entity);
    	HttpResponse response = client.execute(post);
    	
    	String output = EntityUtils.toString(response.getEntity());
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode mainNode = mapper.readTree(output);
    	authenticationKey = "Bearer " + mainNode.get("access_token").asText();
    	Integer expireMiliseconds = new Integer(mainNode.get("expires_in").asText()) * 1000;
    	expiresIn = new Timestamp(System.currentTimeMillis() + expireMiliseconds);
	}
	
    public String getAuthenticationKey() throws Exception{
    	if (expiresIn == null || expiresIn.getTime() < System.currentTimeMillis()){
    		updateAuthenticationKey();
    	}
    	return authenticationKey; 
    }
    
	public String getAuthenticationURL() {
		return authenticationURL;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public Timestamp getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Timestamp expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setAuthenticationKey(String authenticationKey) {
		this.authenticationKey = authenticationKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
