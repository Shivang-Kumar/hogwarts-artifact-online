package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.tcu.cs.hogwarts_artifacts_online.Wizard.Wizard;
import edu.tcu.cs.hogwarts_artifacts_online.Wizard.dto.WizardDto;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.DTO.ArtifactDto;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.utils.IdWorker;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.ChatClient;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.Candidate;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatRequest;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.ChatResponse;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.Content;
import edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto.Part;
import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;
import net.bytebuddy.NamingStrategy.Suffixing.BaseNameResolver.ForGivenType;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value="dev")
public class ArtifactServiceTest {

	@Mock
	ArtifactRepository artifactRepository;

	@Mock
	IdWorker idWorker;

	
	
	@Mock
	ChatClient chatClient;
	
	@Mock
	ObjectMapper objectMapper;
	
	@InjectMocks
	ArtifactService artifactService;
	

	List<Artifact> artifacts;

	@BeforeEach
	void setUp() {

		Artifact a1 = new Artifact();
		a1.setId("23445324535633");
		a1.setName("Time-Turner");
		a1.setDescription(
				"A Time-Turner is a device used for time travel, enabling the wearer to travel back in time for a short duration.");
		a1.setImageUrl("ImageUrlTimeTurner");

		Artifact a2 = new Artifact();
		a2.setId("33445324535634");
		a2.setName("Elder Wand");
		a2.setDescription(
				"The Elder Wand is one of the Deathly Hallows, known to be the most powerful wand ever created.");
		a2.setImageUrl("ImageUrlElderWand");

		this.artifacts = new ArrayList<>();
		this.artifacts.add(a1);
		this.artifacts.add(a2);

	}

	@Test
	void testFindByIdSuccess() {
		// Given. Arrange Inputs and targets. Define the behaviour of mock object
		// artifactRepository.

		Artifact a = new Artifact();
		a.setId("13445324535632");
		a.setName("Invisibility Cloak");
		a.setDescription("An invisibility cloack is used to make the wearer invisible");
		a.setImageUrl("ImageUrl");

		Wizard w = new Wizard();
		w.setId(2);
		w.setName("Harry Potter");

		a.setOwner(w);

		given(artifactRepository.findById("13445324535632")).willReturn(Optional.of(a));

		// When. Act on the target behavior . When steps should cover the method to be
		// tested.

		Artifact returnedArtifact = artifactService.findById("13445324535632");

		// Then. Assert expected outcomes.

		assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
		assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
		assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
		assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
		verify(artifactRepository, times(1)).findById("13445324535632");

	}

	@Test
	void testFindByIdNotFound() {
		// Given.

		given(this.artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

		// When.

		Throwable thrown = catchThrowable(() -> {
			Artifact returnedArtifact = artifactService.findById("13445324535632");
		});

		// Then.
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
				.hasMessage("Could not find artifact with id 13445324535632  :(");
		verify(this.artifactRepository, times(1)).findById("13445324535632");

	}

	@Test
	void testFindAllSuccess() {
		// Given
		given(this.artifactRepository.findAll()).willReturn(this.artifacts);

		// When
		List<Artifact> actualArtifacts = artifactService.findAll();

		// Then
		assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
		verify(this.artifactRepository, times(1)).findAll();
	}

	@Test
	void testSaveSuccess() {
		// given

		Artifact newArtifact = new Artifact();

		newArtifact.setName("Artifact 3");
		newArtifact.setDescription("Description");
		newArtifact.setImageUrl("Image URL.....");

		given(idWorker.nextId()).willReturn(123456L);
		given(artifactRepository.save(newArtifact)).willReturn(newArtifact);
		// when
		Artifact savedArtifact = artifactService.save(newArtifact);

		// then
		assertThat(savedArtifact.getId()).isEqualTo("123456");
		assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
		assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
		assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
		verify(artifactRepository, times(1)).save(newArtifact);

	}

	@Test
	void testUpdateSuccess() {
		// Given
		Artifact oldArtifact = new Artifact();
		oldArtifact.setId("13445324535632");
		oldArtifact.setName("Invisibility Cloak");
		oldArtifact.setDescription("An invisibility cloack is used to make the wearer invisible");
		oldArtifact.setImageUrl("ImageUrl");

		Artifact update = new Artifact();
		//update.setId("13445324535632");
		update.setName("Invisibility Cloak");
		update.setDescription("A new Description");
		update.setImageUrl("ImageUrl");

		given(artifactRepository.findById("13445324535632")).willReturn(Optional.of(oldArtifact));
		given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

		// When

		Artifact updatedArtifact = artifactService.update("13445324535632", update);

		// Then

		assertThat(updatedArtifact.getId()).isEqualTo("13445324535632");
		assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
		verify(artifactRepository, times(1)).findById("13445324535632");
		verify(artifactRepository, times(1)).save(oldArtifact);

	}
	
	@Test
	void testUpdateNotFound() {
		//Given
		Artifact update = new Artifact();
		update.setName("Invisibility Cloak");
		update.setDescription("A new Desription");
		update.setImageUrl("ImageUrl");
		
		given(artifactRepository.findById("13445324535632")).willReturn(Optional.empty());

		
		//When
		assertThrows(ObjectNotFoundException.class,()->{
			artifactService.update("13445324535632",update);
		});
		
		
		//Then
		
		verify(artifactRepository,times(1)).findById("13445324535632");
}
	
	
	@Test
	void testDeleteSuccess()
	{
		//given 
		
		Artifact artifact = new Artifact();
		artifact.setId("13445324535632");
		artifact.setName("Invisibility Cloak");
		artifact.setDescription("An invisibility cloack is used to make the wearer invisible");
		artifact.setImageUrl("ImageUrl");
		
		given(artifactRepository.findById("13445324535632")).willReturn(Optional.of(artifact));
		doNothing().when(artifactRepository).deleteById("13445324535632");
		
		
		//when
		artifactService.delete("13445324535632");
		//then
		verify(artifactRepository,times(1)).deleteById("13445324535632");
		
	}
	
	
	@Test
	void testDeleteNotFound()
	{
		//given 
		
		given(artifactRepository.findById("13445324535632")).willReturn(Optional.empty());
		
		//Above method will throw optional so belowe method is not required as it will not be executed
		//doNothing().when(artifactRepository).deleteById("13445324535632");
		
		
		//when
		assertThrows(ObjectNotFoundException.class, () -> {
			artifactService.delete("13445324535632");
		});
		//then
		verify(artifactRepository,times(1)).findById("13445324535632");
		
	}
	
	@Test
	void testSummarizeSuccess() throws Exception {
		//Given
		WizardDto wizardDto=new WizardDto(1, "Albus dumbeldore", 2);
		List<ArtifactDto> artifactDto=List.of(
				new ArtifactDto("123456","Deluminitor","Deluminitor is a good device",null,wizardDto),
				new ArtifactDto("123456789","Deluminitor2","Deluminitor is a good device 2",null,wizardDto)
				);
		
		
		ChatRequest chatRequest=new ChatRequest(List.of(new Content(List.of(new Part("What is elder wand")))));
		ChatResponse chatResponse=new ChatResponse(List.of(new Candidate((new Content(List.of(new Part("The Elder Wand is a powerful, legendary wand in the Harry Potter series, known for being the most powerful wand ever created.")))))));
		
		

		when(chatClient.generate(any(ChatRequest.class))).thenReturn(chatResponse);

		
		//When
		String summary=this.artifactService.summarize(artifactDto);
		//Then
		assertThat(summary).isEqualTo("The Elder Wand is a powerful, legendary wand in the Harry Potter series, known for being the most powerful wand ever created.");
		verify(this.chatClient,times(1)).generate(any(ChatRequest.class));
		}
	
	
}
