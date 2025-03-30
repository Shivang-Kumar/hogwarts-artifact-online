package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {
	
	private List<Candidate> candidates;

	public ChatResponse(List<Candidate> candidates) {
		super();
		this.candidates = candidates;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}

	@Override
	public String toString() {
		return "ChatResponse [candidates=" + candidates + "]";
	}
	

}
