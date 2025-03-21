package edu.tcu.cs.hogwarts_artifacts_online.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;


public interface UserRepository extends JpaRepository<User, Integer>{

}
