package org.jlu.telstraapp.poll;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="poll_option")
public class PollOption {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="poll_option_id")
	private int pollOptionId;
	@Column(name="value")
	private String value;
	@ElementCollection(fetch=FetchType.EAGER)
	@Column(name="alternate_values")
	@CollectionTable(name="poll_alternate_values", joinColumns=@JoinColumn(name="poll_option_id"))
	@Fetch(FetchMode.SUBSELECT)
	@Cascade(CascadeType.DELETE)
	private List<String> alternateValues = new ArrayList();
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="poll_id")
	private Poll poll;
	
	public PollOption() {

	}
	
	public int getPollOptionId() {
		return pollOptionId;
	}
	public void setPollOptionId(int pollOptionId) {
		this.pollOptionId = pollOptionId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public List<String> getAlternateValues() {
		return alternateValues;
	}

	public void setAlternateValues(List<String> alternateValues) {
		this.alternateValues = alternateValues;
	}

	public Poll getPoll() {
		return poll;
	}
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	
}
