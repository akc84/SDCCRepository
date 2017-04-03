package com.zp.sdcc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import static com.zp.sdcc.common.CurrencyConverterConstants.LOGIN_REQUEST_MAPPING;
import static com.zp.sdcc.common.CurrencyConverterConstants.LOGIN_RESPONSE_MAPPING;

@Controller
public class LoginController {
	
	@GetMapping(value = LOGIN_REQUEST_MAPPING)
	public String loginMapping()
	{
		return LOGIN_RESPONSE_MAPPING;
	}		

}
