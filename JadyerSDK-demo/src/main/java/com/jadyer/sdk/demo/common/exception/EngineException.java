package com.jadyer.sdk.demo.common.exception;

public class EngineException extends RuntimeException {
	private static final long serialVersionUID = 601366631919634564L;
	private int code;
	private String message;
	
	public EngineException(int code, String message){
		super(message);
		this.code = code;
		this.message = message;
	}
	
	public EngineException(int code, String message, Throwable cause){
		super(message, cause);
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}