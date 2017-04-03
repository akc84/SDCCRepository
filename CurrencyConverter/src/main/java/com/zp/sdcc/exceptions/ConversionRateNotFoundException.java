package com.zp.sdcc.exceptions;


public class ConversionRateNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8862177040148024727L;
	
	public ConversionRateNotFoundException(String[] params)
	{
		super(String.format("Exchange rate unavailable from %s to %s as on %s", params[1], params[2], params[0]));
	}

	public String toString()
	{
		return this.getMessage();
	}	
	
}
