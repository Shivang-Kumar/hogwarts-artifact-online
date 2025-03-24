package edu.tcu.cs.hogwarts_artifacts_online.user;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;


public class MyUserPrincipal  implements UserDetails{

	private User user;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MyUserPrincipal(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//Converting user role from space delimited strings to a list of simple granted authorities.
		//Before conversion we need to add "ROLE_" prefix to each role name
		return Arrays.stream(StringUtils.tokenizeToStringArray(this.user.getRoles(), " "))
				.map(role -> new SimpleGrantedAuthority("ROLE_"+role))
				.toList();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.user.getUsername();
	}

	
    @Override
    public boolean isAccountNonExpired() {
        // You could use a field in your User object to check if the account is expired
        // For simplicity, we'll return true (indicating the account is not expired)
        return true; // Replace with actual logic if account expiry is implemented
    }

    @Override
    public boolean isAccountNonLocked() {
        // Use the 'isLocked' field or any other field in your User object
        return true; // Return true if the account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // If you track password expiration, you can check it here
        return true; // Return true if credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        // You can check if the user is enabled from a field in your User object
        return this.user.isEnabled(); // Replace with actual logic for checking if the account is enabled
    }

}
