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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.tcu.cs.hogwarts_artifacts_online.Wizard.Wizard;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.utils.IdWorker;
import net.bytebuddy.NamingStrategy.Suffixing.BaseNameResolver.ForGivenType;

@ExtendWith(MockitoExtension.class)
public class ArtifactServiceTest {

	@Mock
	ArtifactRepository artifactRepository;

	@Mock
	IdWorker idWorker;

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

		given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

		// When.

		Throwable thrown = catchThrowable(() -> {
			Artifact returnedArtifact = artifactService.findById("13445324535632");
		});

		// Then.
		assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class)
				.hasMessage("Could not find artifact with id 13445324535632  :(");
		verify(artifactRepository, times(1)).findById("13445324535632");

	}

	@Test
	void testFindAllSuccess() {
		// Given
		given(artifactRepository.findAll()).willReturn(this.artifacts);

		// When
		List<Artifact> actualArtifacts = artifactService.findAll();

		// Then
		assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
		verify(artifactRepository, times(1)).findAll();
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
		update.setId("13445324535632");
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
		assertThrows(ArtifactNotFoundException.class,()->{
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
		assertThrows(ArtifactNotFoundException.class, () -> {
			artifactService.delete("13445324535632");
		});
		//then
		verify(artifactRepository,times(1)).findById("13445324535632");
		
	}
	
	
}
