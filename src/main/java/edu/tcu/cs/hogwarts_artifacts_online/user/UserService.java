package edu.tcu.cs.hogwarts_artifacts_online.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	
	
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	public List<User> findAllUser(){
		List<User> allUsers=this.userRepository.findAll();
		return allUsers;
	}


	public User addUser(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
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


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	return this.userRepository.findByUsername(username)
				.map(user -> new MyUserPrincipal(user))
				.orElseThrow(() -> new UsernameNotFoundException("username"+username+" is not found."));
	}

}
