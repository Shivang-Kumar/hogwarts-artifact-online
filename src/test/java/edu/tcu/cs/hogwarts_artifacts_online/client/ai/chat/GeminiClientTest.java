package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat;



import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.Candidate;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatRequest;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatResponse;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.Content;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.Part;


@ActiveProfiles("dev")
@RestClientTest(GeminiClient.class)
public class GeminiClientTest {
	
	private String url;
     
	@Autowired
	private MockRestServiceServer mockServer;
	
	@Autowired
	private GeminiClient geminiClient;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	@BeforeEach
	void setUp(@Value("${ai.geminiai.api-key}")String apiKey){
		
		
		this.url="https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"+"?key=" + apiKey;;
				
	}
	
	
	@Test
	void testGenerateSuccess() throws Exception {
		//Given
		
		ChatRequest chatRequest=new ChatRequest(List.of(new Content(List.of(new Part("What is elder wand")))));
		ChatResponse chatResponse=new ChatResponse(List.of(new Candidate((new Content(List.of(new Part("The Elder Wand is a powerful, legendary wand in the Harry Potter series, known for being the most powerful wand ever created.")))))));
		this.mockServer.expect(requestTo(this.url))
		.andExpect(method(HttpMethod.POST))
		.andExpect(content().json(this.objectMapper.writeValueAsString(chatRequest)))
		.andRespond(withSuccess(this.objectMapper.writeValueAsString(chatResponse),MediaType.APPLICATION_JSON));
		
		//When
		ChatResponse generatedChatResponse=this.geminiClient.generate(chatRequest);
		//Then
		this.mockServer.verify();
		assertThat(generatedChatResponse.getCandidates().get(0).getContent().getParts().get(0).getText()).isEqualTo("The Elder Wand is a powerful, legendary wand in the Harry Potter series, known for being the most powerful wand ever created.");
		
	}
	
	@Test
	void testGenerateUnauthorizedRequest() throws Exception {
		//Given
		
		ChatRequest chatRequest=new ChatRequest(List.of(new Content(List.of(new Part("What is elder wand")))));
		ChatResponse chatResponse=new ChatResponse(List.of(new Candidate((new Content(List.of(new Part("The Elder Wand is a powerful, legendary wand in the Harry Potter series, known for being the most powerful wand ever created.")))))));
		this.mockServer.expect(requestTo(this.url))
		.andExpect(method(HttpMethod.POST))
		.andRespond(withUnauthorizedRequest());
		
		//When
		Throwable thrown= catchThrowable(() -> this.geminiClient.generate(chatRequest));
		//Then
		this.mockServer.verify();
		assertThat(thrown).isInstanceOf(Exception.class);
		
	}
	
	
	
}
