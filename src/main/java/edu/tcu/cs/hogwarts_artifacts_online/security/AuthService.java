package edu.tcu.cs.hogwarts_artifacts_online.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.tcu.cs.hogwarts_artifacts_online.user.MyUserPrincipal;
import edu.tcu.cs.hogwarts_artifacts_online.user.User;
import edu.tcu.cs.hogwarts_artifacts_online.user.DTO.UserDto;
import edu.tcu.cs.hogwarts_artifacts_online.user.converter.UserToUserDtoConverter;

@Service
public class AuthService {
	
	private final JWTProvider jwtProvider;
	
	private final UserToUserDtoConverter userToUserDtoConverter;
	

	public AuthService(JWTProvider jwtProvider) {
		super();
		this.jwtProvider = jwtProvider;
		this.userToUserDtoConverter = new UserToUserDtoConverter();
	}

	public Map<String,Object> createLoginInfo(Authentication authentication) {
		//Create User info object
		MyUserPrincipal principal=(MyUserPrincipal) authentication.getPrincipal();
		User user=principal.getUser();
		UserDto userDto=this.userToUserDtoConverter.convert(user);
		//Create a JWT
		String token=this.jwtProvider.createToken(authentication);
		Map<String,Object> loginResultMap=new HashMap<>();
		loginResultMap.put("userInfo",userDto);
		loginResultMap.put("token", token);
		return loginResultMap;
		
	}
	

}
