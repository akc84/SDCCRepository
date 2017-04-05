package com.zp.sdcc.test.services;

import static com.zp.sdcc.common.CurrencyConverterConstants.EUR;
import static com.zp.sdcc.common.CurrencyConverterConstants.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import com.zp.sdcc.exceptions.ConversionRateNotFoundException;
import com.zp.sdcc.exceptions.ExternalServiceNotRespondingException;
import com.zp.sdcc.services.ExternalCurrencyServiceAdapter;
import com.zp.sdcc.services.FixerIOServiceAdapterImpl;

@RunWith(SpringRunner.class)
@RestClientTest({ FixerIOServiceAdapterImpl.class })
public class FixerIOAdapterTest {

	private static final Logger logger = LoggerFactory.getLogger(FixerIOAdapterTest.class);

	private static final String RESPONSE_JSON_WITHOUT_RATE = "{\"base\":\"EUR\",\"date\":\"2009-12-31\",\"rates\":{}}";

	private static final String RESPONSE_JSON_WITH_RATE = "{\"base\":\"EUR\",\"date\":\"2009-12-31\",\"rates\":{\"USD\":1.4406}}";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private MockRestServiceServer server;

	@Autowired
	private ExternalCurrencyServiceAdapter externalServiceAdapter;

	@Test
	public void getCurrencyExchangeRate_ServerRespondsWithRate_ExchangeRateReturned()
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException {
		// Arrange
		String[] params = new String[] { "2010-01-01", EUR, USD };
		this.server
				.expect(requestTo("http://api.fixer.io/" + params[0] + "?base=" + params[1] + "&symbols=" + params[2]))
				.andRespond(withSuccess().body(RESPONSE_JSON_WITH_RATE));

		// Act
		BigDecimal exchangeRate = externalServiceAdapter.getCurrencyExchangeRate(params);

		// Assert
		assertThat(exchangeRate).isEqualTo(new BigDecimal("1.4406"));

	}

	@Test
	public void getCurrencyExchangeRate_ServerRespondsWithoutRate_ConversionRateNotFoundExceptionThrown()
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException {
		// Arrange
		String[] params = new String[] { "2010-01-01", EUR, USD };
		this.server
				.expect(requestTo("http://api.fixer.io/" + params[0] + "?base=" + params[1] + "&symbols=" + params[2]))
				.andRespond(withSuccess().body(RESPONSE_JSON_WITHOUT_RATE));

		this.thrown.expect(ConversionRateNotFoundException.class);

		// Act
		externalServiceAdapter.getCurrencyExchangeRate(params);

		// Assert
		// exception thrown
	}

	@Test
	public void getCurrencyExchangeRate_ExceptionThrownDuringExternalServiceCall_ExternalServiceNotRespondingExceptionThrown()
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException {
		// Arrange
		String[] params = new String[] { "2010-01-01", EUR, USD };
		this.server
				.expect(requestTo("http://api.fixer.io/" + params[0] + "?base=" + params[1] + "&symbols=" + params[2]))
				.andRespond(MockRestResponseCreators.withServerError());
		this.thrown.expect(ExternalServiceNotRespondingException.class);
		logger.error("PLEASE IGNORE MOCKED REMOTE SERVER EXCEPTION BELOW:");

		// Act
		externalServiceAdapter.getCurrencyExchangeRate(params);

		// Assert
		// exception thrown
	}

	@Test
	public void getCurrencyExchangeRate_DateLessThanMinSupportedDate_ConversionRateNotFoundExceptionThrown()
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException {
		// Arrange
		String[] params = new String[] { "1999-12-31", EUR, USD };
		this.server
				.expect(requestTo("http://api.fixer.io/" + params[0] + "?base=" + params[1] + "&symbols=" + params[2]))
				.andRespond(MockRestResponseCreators.withServerError());
		this.thrown.expect(ConversionRateNotFoundException.class);

		// Act
		externalServiceAdapter.getCurrencyExchangeRate(params);

		// Assert
		// exception thrown
	}

	@Test
	public void getCurrencyExchangeRate_InvalidDateFormat_ConversionRateNotFoundExceptionThrown()
			throws ConversionRateNotFoundException, ExternalServiceNotRespondingException {
		// Arrange
		String[] params = new String[] { "2010/01/01", EUR, USD };
		this.server
				.expect(requestTo("http://api.fixer.io/" + params[0] + "?base=" + params[1] + "&symbols=" + params[2]))
				.andRespond(MockRestResponseCreators.withServerError());
		this.thrown.expect(ConversionRateNotFoundException.class);

		// Act
		externalServiceAdapter.getCurrencyExchangeRate(params);

		// Assert
		// exception thrown
	}

}
