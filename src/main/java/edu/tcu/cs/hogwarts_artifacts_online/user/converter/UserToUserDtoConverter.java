package edu.tcu.cs.hogwarts_artifacts_online.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import edu.tcu.cs.hogwarts_artifacts_online.user.User;
import edu.tcu.cs.hogwarts_artifacts_online.user.DTO.UserDto;

@Component
public class UserToUserDtoConverter  implements Converter<User, UserDto>{

	@Override
	public UserDto convert(User source) {
		UserDto userDto=new UserDto(source.getId(),source.getUsername(),source.isEnabled(),source.getRoles(),source.getPassword());
		return userDto;
		
	}

}
