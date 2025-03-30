package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto;

import java.util.List;

public class Part {
	
	@Override
	public String toString() {
		return "Part [text=" + text + "]";
	}

	private String text;
	
	

	public Part(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	

}
