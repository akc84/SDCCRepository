package com.zp.sdcc.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zp.sdcc.dao.UserRepository;
import com.zp.sdcc.entities.User;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;

	PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User registerUser(User userToRegister) {
		encodePassword(userToRegister);
		linkUserAndAddress(userToRegister);
		userToRegister = userRepository.save(userToRegister);
		return userToRegister;
	}

	@Override
	public String findLoggedInUsername() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails instanceof UserDetails) {
			return ((UserDetails) userDetails).getUsername();
		}
		return null;
	}

	@Override
	public boolean isUsernameAvailable(String username) {
		return !(userRepository.exists(username));
	}

	private void encodePassword(User userToRegister) {
		userToRegister.setPassword(passwordEncoder.encode(userToRegister.getPassword()));
	}

	private void linkUserAndAddress(User userToRegister) {
		userToRegister.getAddress().setUser(userToRegister);
	}
}
