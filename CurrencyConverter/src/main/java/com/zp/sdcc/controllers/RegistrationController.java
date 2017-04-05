package com.zp.sdcc.controllers;

import static com.zp.sdcc.common.CurrencyConverterConstants.COUNTRY_LIST;
import static com.zp.sdcc.common.CurrencyConverterConstants.FORM_REGISTRATION;
import static com.zp.sdcc.common.CurrencyConverterConstants.LOGIN_RESPONSE_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.MESSAGE;
import static com.zp.sdcc.common.CurrencyConverterConstants.REGISTER_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.REGISTER_RESPONSE_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.REGISTRATION_SUCCESS_MESSAGE;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.zp.sdcc.entities.User;
import com.zp.sdcc.services.UserService;
import com.zp.sdcc.validators.UserValidator;

/**
 * @author AKC
 * Controller to handle get and post requests to /register.
 */
@Controller
public class RegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	UserService registrationService;

	@Autowired
	UserValidator validator;

	@Value("#{'${country.list}'.split(',')}")
	private List<String> countryList;

	@GetMapping(value = REGISTER_REQUEST_MAPPING)
	public String registerMapping(ModelMap model) {
		return defaultHandler(model);
	}

	@PostMapping(value = REGISTER_REQUEST_MAPPING)
	public String handleRegistrationRequest(@ModelAttribute(FORM_REGISTRATION) @Valid User registrationForm,
											BindingResult bindingResult, ModelMap model) {
		
		validator.validate(registrationForm, bindingResult);

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().stream().forEach(p -> logger.error(p.toString()));
			return defaultHandler(model);
		}

		registrationService.registerUser(registrationForm);

		model.put(MESSAGE, REGISTRATION_SUCCESS_MESSAGE);
		return LOGIN_RESPONSE_MAPPING;
	}

	private String defaultHandler(ModelMap model) {
		model.addAttribute(COUNTRY_LIST, countryList);
		model.putIfAbsent(FORM_REGISTRATION, new User());
		return REGISTER_RESPONSE_MAPPING;
	}

}
