package edu.tcu.cs.hogwarts_artifacts_online.Wizard;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.ArtifactRepository;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.utils.IdWorker;
import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;

@Service
public class WizardService {

	private final WizardRepository wizardRepository;
	private final IdWorker idWorker;
	private final ArtifactRepository artifactRepository;

	public WizardService(WizardRepository wizardRepository, IdWorker idWorker, ArtifactRepository artifactRepository) {
		super();
		this.wizardRepository = wizardRepository;
		this.idWorker = idWorker;
		this.artifactRepository = artifactRepository;
	}

	public List<Wizard> findAll() {
		List<Wizard> wizards = wizardRepository.findAll();
		return wizards;
	}

	public Wizard save(Wizard newWizard) {
		newWizard.setId((int) idWorker.nextId());
		return wizardRepository.save(newWizard);
	}

	public Wizard findById(Integer wizardId) {
		Wizard foundWizard = this.wizardRepository.findById(wizardId)
				.orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

		return foundWizard;
	}

	public Wizard updateWizard(Integer wizardId, Wizard wizard) {
		Wizard updatedWizard = this.wizardRepository.findById(wizardId).map(foundWizard -> {
			foundWizard.setName(wizard.getName());
			return this.wizardRepository.save(foundWizard);
		}).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

		return updatedWizard;
	}

	public void deleteWizardById(int wizardId) {
		Wizard foundWizard = this.wizardRepository.findById(wizardId)
				.orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

		// Before the deletion we will unassign this wizard artifact
		foundWizard.removeAllArtifacts();
		this.wizardRepository.deleteById(wizardId);
	}

	public void assignArtifactToWizard(String artifactID, Integer wizardId) {

		Wizard foundWizard = this.wizardRepository.findById(wizardId)
				.orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
		Artifact foundArtifact = this.artifactRepository.findById(artifactID)
				.orElseThrow(() -> new ObjectNotFoundException("artifact", artifactID));

		foundWizard.getArtifacts().add(foundArtifact);
		foundArtifact.setOwner(foundWizard);
		wizardRepository.save(foundWizard);
		artifactRepository.save(foundArtifact);
	}
	
	public void assignArtifact(Integer wizardId,String artifactId)
	{
		Artifact foundArtifact=this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
		Wizard foundWizard=this.wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
		
		//Artifact assignment
		//We need to see if artifact is already owned by some owner
		if(foundArtifact.getOwner()!=null)
		{
			foundArtifact.getOwner().removeArtifact(foundArtifact);
		}
		foundWizard.addArtifact(foundArtifact);
		this.artifactRepository.save(foundArtifact);
		this.wizardRepository.save(foundWizard);
		
		
	}

}
