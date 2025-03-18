package edu.tcu.cs.hogwarts_artifacts_online.Wizard.dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDto(
		Integer id,
		@NotEmpty(message="name is required")
		String name,
		Integer numberOfArtifact) {

}
