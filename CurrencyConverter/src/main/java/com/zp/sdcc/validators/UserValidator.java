package com.zp.sdcc.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.zp.sdcc.common.CurrencyConverterConstants.*;
import com.zp.sdcc.entities.User;
import com.zp.sdcc.services.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User userForValidation = ((User) target);
		if (!userService.isUsernameAvailable(userForValidation.getUsername()))
			errors.rejectValue(USERNAME, "username.unavailable");
		if (!userForValidation.getPassword().equals(userForValidation.getConfirmPassword()))
			errors.rejectValue(PASSWORD, "passwords.mismatch");
	}

}
