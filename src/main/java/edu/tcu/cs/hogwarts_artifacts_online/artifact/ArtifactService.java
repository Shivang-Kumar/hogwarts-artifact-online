package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.utils.IdWorker;
import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArtifactService {

	private final ArtifactRepository artifactRepository;

	private final IdWorker idWorker;

	public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
		super();
		this.artifactRepository = artifactRepository;
		this.idWorker = idWorker;
	}

	@Observed(name="artifact" , contextualName = "findByIdService")
	public Artifact findById(String artifactId) {
		return this.artifactRepository.findById(artifactId)
				.orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));

	}

	public List<Artifact> findAll() {
		return this.artifactRepository.findAll();
	}

	public Artifact save(Artifact newArtifact) {

		newArtifact.setId(idWorker.nextId() + "");
		return this.artifactRepository.save(newArtifact);

	}

	public Artifact update(String artifactId, Artifact update) {

		return this.artifactRepository.findById(artifactId).map(oldArtifact -> {
			oldArtifact.setName(update.getName());
			oldArtifact.setDescription(update.getDescription());
			oldArtifact.setImageUrl(update.getImageUrl());
			return this.artifactRepository.save(oldArtifact);

		}).orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));

	}

	public void delete(String artifactId)
	{
	   Artifact artifact=  this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));
	   this.artifactRepository.deleteById(artifactId);
	     
	}
}
