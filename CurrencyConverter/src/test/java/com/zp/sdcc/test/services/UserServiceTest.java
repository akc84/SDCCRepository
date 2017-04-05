package com.zp.sdcc.test.services;

import static com.zp.sdcc.common.CurrencyConverterConstants.PASSWORD;
import static com.zp.sdcc.test.TestConstants.TEST_USER_NAME;
import static com.zp.sdcc.test.TestConstants.TEST_USER_NAME2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zp.sdcc.dao.UserRepository;
import com.zp.sdcc.entities.Address;
import com.zp.sdcc.entities.User;
import com.zp.sdcc.services.UserService;
import com.zp.sdcc.services.UserServiceImpl;

public class UserServiceTest {

	private UserService userService;

	private UserRepository userRepository;

	PasswordEncoder passwordEncoder;

	@Before
	public void setUp() {
		userRepository = mock(UserRepository.class);
		userService = new UserServiceImpl(userRepository, new BCryptPasswordEncoder());
	}

	@Test
	public void registerUser_ValidUser_ReturnsRegisteredUser() {
		// Arrange
		User user = new User();
		user.setUsername(TEST_USER_NAME);
		user.setPassword(PASSWORD);
		Address address = new Address();
		user.setAddress(address);

		when(userRepository.save(any(User.class)))
				.thenAnswer((InvocationOnMock invocation) -> (User) invocation.getArguments()[0]);

		// Act
		User returnedUser = userService.registerUser(user);

		// Assert
		assertThat(TEST_USER_NAME).isEqualTo(returnedUser.getUsername());
	}

	@Test
	public void isUsernameAvailable_ExistingUsername_ReturnsFalse() {
		// Arrange
		when(userRepository.exists(TEST_USER_NAME2)).thenReturn(Boolean.TRUE);

		// Act
		boolean result = userService.isUsernameAvailable(TEST_USER_NAME2);

		// Assert
		assertThat(result).isFalse();

	}

	@Test
	public void isUsernameAvailable_NewUsername_ReturnsTrue() {
		// Arrange
		when(userRepository.exists(TEST_USER_NAME)).thenReturn(Boolean.FALSE);

		// Act
		boolean result = userService.isUsernameAvailable(TEST_USER_NAME);

		// Assert
		assertThat(result).isTrue();

	}

}
