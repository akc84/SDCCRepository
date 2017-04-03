package com.zp.sdcc.services;

import com.zp.sdcc.entities.User;

public interface UserService {

	public User registerUser(User userToRegister);	
	
	public String findLoggedInUsername();
	
	public boolean isUsernameAvailable(String username);
}
