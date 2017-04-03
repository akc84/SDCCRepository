package com.zp.sdcc.it.cucumber.steps;

import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.zp.sdcc.CurrencyConverterApplication;



@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CurrencyConverterApplication.class, loader = SpringBootContextLoader.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractStepDef {
	
	@LocalServerPort
	int port;
	
}