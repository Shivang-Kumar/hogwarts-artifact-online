package edu.tcu.cs.hogwarts_artifacts_online.wizard;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import edu.tcu.cs.hogwarts_artifacts_online.Wizard.Wizard;
import edu.tcu.cs.hogwarts_artifacts_online.Wizard.WizardService;
import edu.tcu.cs.hogwarts_artifacts_online.Wizard.dto.WizardDto;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.DTO.ArtifactDto;
import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;

@SpringBootTest
@AutoConfigureMockMvc
public class WizardControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	WizardService wizardService;

	@Autowired
	ObjectMapper objectMapper;
	

	@Value("${api.endpoint.base-url}")
	String baseUrl;

	List<Wizard> wizards;

	@BeforeEach
	void setUp() {

		List<Artifact> artifacts1 = new ArrayList<>();

		Artifact a1 = new Artifact();
		a1.setId("21232456489892566");
		a1.setName("Deluminator");
		a1.setDescription("A deluminator is a device invented by albus dumbeldore");
		a1.setImageUrl("ImageUrl");
		artifacts1.add(a1);

		Artifact a2 = new Artifact();
		a2.setId("31232456489892567");
		a2.setName("Invisibility Cloak");
		a2.setDescription("An invisibility cloak is a magical garment that makes the wearer invisible.");
		a2.setImageUrl("ImageUrl");
		artifacts1.add(a2);

		List<Artifact> artifacts2 = new ArrayList<>();

		Artifact a3 = new Artifact();
		a3.setId("41232456489892568");
		a3.setName("Elder Wand");
		a3.setDescription("The Elder Wand is the most powerful wand in existence.");
		a3.setImageUrl("ImageUrl");
		artifacts2.add(a3);

		Artifact a4 = new Artifact();
		a4.setId("51232456489892569");
		a4.setName("Resurrection Stone");
		a4.setDescription("The Resurrection Stone can bring back spirits from the dead.");
		a4.setImageUrl("ImageUrl");
		artifacts2.add(a4);

		Wizard w1 = new Wizard();
		w1.setId(1);
		w1.setName("Albus Dumbeldore");
		w1.setArtifacts(artifacts1);

		Wizard w2 = new Wizard();
		w2.setId(2);
		w2.setName("Harry Potter");
		w2.setArtifacts(artifacts2);

		wizards = new ArrayList<>();
		wizards.add(w1);
		wizards.add(w2);

	}

	@Test
	void testFindAllWizardSuccess() throws Exception {
		// given
		given(wizardService.findAll()).willReturn(this.wizards);
		// when and then
		this.mockMvc.perform(get(this.baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value("true")).andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpect(jsonPath("$.message").value("Find All Success")).andExpect(jsonPath("$.data[0].id").value(1))
				.andExpect(jsonPath("$.data[0].name").value("Albus Dumbeldore"))
				.andExpect(jsonPath("$.data[0].numberOfArtifact").value(2));
	}

	@Test
	void testAddWizardSuccess() throws Exception {

		WizardDto wizardDto = new WizardDto(null, "Harry Potter", null);

		String json = this.objectMapper.writeValueAsString(wizardDto);

		Wizard savedWizard = new Wizard();
		savedWizard.setId(6);
		savedWizard.setName("Harry Potter");

		// given
		given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);

		// when and then
		this.mockMvc
				.perform(post(this.baseUrl+"/wizards").contentType(MediaType.APPLICATION_JSON).content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(true)).andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpect(jsonPath("$.message").value("Add Success")).andExpect(jsonPath("$.data.id").value(6))
				.andExpect(jsonPath("$.data.name").value("Harry Potter"));

	}

	@Test
	void testFindWizardByIdSuccess() throws Exception {
		// Given
		given(wizardService.findById(1)).willReturn(this.wizards.get(0));
		// When and Then
		this.mockMvc.perform(get(this.baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(true)).andExpectAll(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpectAll(jsonPath("$.message").value("Find One Success")).andExpect(jsonPath("$.data.id").value(1))
				.andExpect(jsonPath("$.data.name").value("Albus Dumbeldore"));

	}

	@Test
	void testFindWizardByIdNotFound() throws Exception {
		// Given
		given(this.wizardService.findById(1)).willThrow(new ObjectNotFoundException("wizard",1));
		// When and Then
		this.mockMvc.perform(get(this.baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(false)).andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
				.andExpect(jsonPath("$.message").value("Could not find wizard with id 1  :("))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testWizardUpdateSuccess() throws Exception {
		WizardDto wizardDto = new WizardDto(null, "Goku Loki", null);

		String json = this.objectMapper.writeValueAsString(wizardDto);

		Wizard savedWizard = new Wizard();
		savedWizard.setId(6);
		savedWizard.setName("Harry Potter");

		given(this.wizardService.updateWizard(eq(1), Mockito.any(Wizard.class))).willReturn(savedWizard);

		// When and Then
		this.mockMvc
				.perform(put(this.baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json)
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(true)).andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpect(jsonPath("$.message").value("Update Success")).andExpect(jsonPath("$.data.id").value(6))
				.andExpect(jsonPath("$.data.name").value("Harry Potter"));
	}

	@Test
	void testWizardUpdateWithIdNotFound() throws Exception {

		WizardDto wizardDto = new WizardDto(null, "Goku Loki", null);

		String json = this.objectMapper.writeValueAsString(wizardDto);

		given(this.wizardService.updateWizard(eq(1), Mockito.any(Wizard.class)))
				.willThrow(new ObjectNotFoundException("wizard",1));

		// When and Then
		this.mockMvc
				.perform(put(this.baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json)
						.accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(false)).andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
				.andExpect(jsonPath("$.message").value("Could not find wizard with id 1  :("))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testDeleteWizardSuccess() throws Exception {
		// Given
		doNothing().when(this.wizardService).deleteWizardById(1);

		// when and then
		this.mockMvc.perform(delete(this.baseUrl+"/wizards/1").accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(true)).andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpect(jsonPath("$.message").value("Delete Success")).andExpect(jsonPath("$.data").isEmpty());

	}

	@Test
	void testDeleteWizardIdNotFound() throws Exception {
		// Given
		doThrow(new ObjectNotFoundException("wizard",1)).when(wizardService).deleteWizardById(1);

		// When and then
		this.mockMvc.perform(delete(this.baseUrl+"/wizards/1").accept(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(false)).andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
				.andExpect(jsonPath("$.message").value("Could not find wizard with id 1  :("))
				.andExpect(jsonPath("$.data").isEmpty());

	}

}
