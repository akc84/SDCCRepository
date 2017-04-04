package com.zp.sdcc.test.services;

import static com.zp.sdcc.common.CurrencyConverterConstants.PASSWORD;
import static com.zp.sdcc.test.TestConstants.TEST_USER_NAME;
import static com.zp.sdcc.test.TestConstants.TEST_USER_NAME2;
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
		User user = createUser(TEST_USER_NAME2, PASSWORD);
		when(userRepository.findOne(TEST_USER_NAME2)).thenReturn(user);
		
		//Act
		UserDetails result = userDetailsService.loadUserByUsername(TEST_USER_NAME2);
		
		//Assert
		assertThat(result.getPassword()).isEqualTo(PASSWORD);		
		
	}

	@Test
	
	public void loadUserByUsername_NewUsername_ThrowsUsernameNotFoundException(){

		//Arrange
		when(userRepository.findOne(TEST_USER_NAME)).thenReturn(null);
		thrown.expect(UsernameNotFoundException.class);
		
		//Act
		userDetailsService.loadUserByUsername(TEST_USER_NAME);
		
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
