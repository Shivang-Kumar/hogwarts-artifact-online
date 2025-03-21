package edu.tcu.cs.hogwarts_artifacts_online.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import edu.tcu.cs.hogwarts_artifacts_online.user.User;
import edu.tcu.cs.hogwarts_artifacts_online.user.DTO.UserDto;


@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

	@Override
	public User convert(UserDto source) {
		User newUser=new User();
		newUser.setUsername(source.username());
		newUser.setRoles(source.roles());
		newUser.setEnabled(source.enabled());
		newUser.setPassword(source.password());
		return newUser;
	}
	

}
