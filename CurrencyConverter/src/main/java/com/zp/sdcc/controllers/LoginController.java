package com.zp.sdcc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import static com.zp.sdcc.common.CurrencyConverterConstants.LOGIN_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.LOGIN_RESPONSE_MAPPING;

/**
 * @author AKC
 * Controller to handle get requests to /login.
 * Post requests to /login provided by Spring Security
 */
@Controller
public class LoginController {

	@GetMapping(value = LOGIN_REQUEST_MAPPING)
	public String loginMapping() {
		return LOGIN_RESPONSE_MAPPING;
	}

}
