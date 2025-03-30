package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
	
	@Override
	public String toString() {
		return "Content [parts=" + parts + "]";
	}

	private List<Part> parts;
	

	public Content(List<Part> parts) {
		super();
		this.parts = parts;
	}

	public List<Part> getParts() {
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}


}
