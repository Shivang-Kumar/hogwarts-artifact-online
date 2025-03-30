package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto;

import java.util.List;

public class ChatRequest {
	
	private List<Content> contents;
	

	public ChatRequest(List<Content> contents) {
		super();
		this.contents = contents;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

}
