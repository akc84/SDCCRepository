package com.zp.sdcc.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zp.sdcc.dao.UserRepository;
import com.zp.sdcc.entities.User;
import com.zp.sdcc.services.UserDetailsServiceImpl;


public class UserDetailsServiceTest {


	private UserDetailsService userDetailsService;

	private UserRepository userRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();	

    @Before
    public void setUp(){
    	userRepository = mock(UserRepository.class);
    	userDetailsService = new UserDetailsServiceImpl(userRepository);
    }
    
	@Test
	public void loadUserByUsername_ExistingUsername_ReturnsUserDetails(){

		//Arrange
		User user = createUser("jane", "password");
		when(userRepository.findOne("jane")).thenReturn(user);
		
		//Act
		UserDetails result = userDetailsService.loadUserByUsername("jane");
		
		//Assert
		assertThat(result.getPassword()).isEqualTo("password");		
		
	}

	@Test
	
	public void loadUserByUsername_NewUsername_ThrowsUsernameNotFoundException(){

		//Arrange
		when(userRepository.findOne("john")).thenReturn(null);
		thrown.expect(UsernameNotFoundException.class);
		
		//Act
		userDetailsService.loadUserByUsername("john");
		
		//Assert
		//the thrown exception
		
	}
	
	private User createUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return user;
	}

	

}
