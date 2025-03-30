package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Candidate {
	
	private Content  content;

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Candidate(Content content) {
		super();
		this.content = content;
	}



	

}
