package com.zp.sdcc.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import static com.zp.sdcc.common.CurrencyConverterUtil.*;

public class CurrencyConversionVO {

	@NotBlank
	@Size(max = 3, min = 3)
	private String sourceCurrency;
	
	@NotNull
	@DecimalMin("0.0")
	@Digits(integer=11,fraction=2)
	private BigDecimal amountToConvert;
	
	@NotBlank
	@Size(max = 3, min = 3)
	private String targetCurrency;
	

	private BigDecimal convertedAmount;
    
	@Past
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date conversionDate; 

	
	public CurrencyConversionVO() {
		super();
	}

	public CurrencyConversionVO(String fromCurrency, BigDecimal fromAmount, String toCurrency,
								Date conversionDate) {
		super();
		this.sourceCurrency = fromCurrency;
		this.amountToConvert = fromAmount;
		this.targetCurrency = toCurrency;
		this.conversionDate = conversionDate;
	}

	public String getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(String setSourceCurrency) {
		this.sourceCurrency = setSourceCurrency;
	}

	public BigDecimal getAmountToConvert() {
		return amountToConvert;
	}

	public void setAmountToConvert(BigDecimal amountToConvert) {
		this.amountToConvert = amountToConvert;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public BigDecimal getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(BigDecimal convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

	public Date getConversionDate() {
		conversionDate = Optional.ofNullable(conversionDate).orElse(new Date());
		return conversionDate;
	}

	public void setConversionDate(Date conversionDate) {
		this.conversionDate = conversionDate;
	}

	public String getAuditString() {
		StringBuilder auditString = new StringBuilder("Query:::").append(getFormattedAmount(getAmountToConvert()))
										.append(" ").append(getSourceCurrency()).append("=>")
										.append(getTargetCurrency()).append(" as on ")
										.append(getFormattedDate(getConversionDate()))
										.append("    Result:::").append(getFormattedAmount(getConvertedAmount()))
										.append(" ").append(getTargetCurrency());
		return auditString.toString();		
	}	
}
