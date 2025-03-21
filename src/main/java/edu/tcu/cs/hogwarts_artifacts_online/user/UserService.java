package edu.tcu.cs.hogwarts_artifacts_online.user;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;

@Service
public class UserService {
	
	UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	
	public List<User> findAllUser(){
		List<User> allUsers=this.userRepository.findAll();
		return allUsers;
	}


	public User addUser(User user) {
		
		return this.userRepository.save(user);
	}
	
	public User findUserById(Integer id)
	{
		User foundUser=this.userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("user", id));
		return foundUser;
	}


	public User updateUser(Integer userId,User user) {
	return this.userRepository.findById(userId).map(foundUser -> {
		foundUser.setUsername(user.getUsername());
		foundUser.setEnabled(user.isEnabled());
		foundUser.setRoles(user.getRoles());
		return this.userRepository.save(foundUser);
	}).orElseThrow(() ->  new ObjectNotFoundException("user", userId));
	}


	public void deleteUserById(Integer userId) {
		
		User user=this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
		this.userRepository.deleteById(userId);
	}

}
