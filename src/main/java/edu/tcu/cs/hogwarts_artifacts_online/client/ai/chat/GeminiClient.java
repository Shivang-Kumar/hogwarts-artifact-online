package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatRequest;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatResponse;

@Component
public class GeminiClient implements ChatClient {

	private final RestClient restClient;

	@Autowired
	ObjectMapper objectMapper;

	public GeminiClient(@Value("${ai.geminiai.endpoint}") String endpoint,
			@Value("${ai.geminiai.api-key}") String apiKey, RestClient.Builder restClientBuilder) {
		super();
		String fullEndpoint = endpoint + "?key=" + apiKey;
		
		this.restClient = restClientBuilder.baseUrl(fullEndpoint).build();
	}

	@Override
	public ChatResponse generate(ChatRequest chatRequest) throws Exception {

		String json = objectMapper.writeValueAsString(chatRequest);

		String responseBody = this.restClient.post().contentType(MediaType.APPLICATION_JSON).body(json).retrieve()
				.body(String.class); // Get response as raw String

		System.out.println("Raw API Response: " + responseBody); // Log it

		// Convert response manually
		return objectMapper.readValue(responseBody, ChatResponse.class);
	}

}
