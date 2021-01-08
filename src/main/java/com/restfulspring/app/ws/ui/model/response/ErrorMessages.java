package com.restfulspring.app.ws.ui.model.response;

public enum ErrorMessages {
	
	MISSING_REQUIRED_FIELD("missing required field"),
	RECORD_ALREADY_EXISTS("record already exists"),
	INTERNAL_SERVER_ERROR("internal server error"),
	NO_RECORD_FOUND("record with provided id is not found"),
	AUTHENTICATION_FAILED("authentication failed"),
	COULD_NOT_UPDATE_RECORD("could not update record"),
	COULD_NOT_DELETE_RECORD("could not delete record"),
	EMAIL_ADDRESS_NOT_VERIFIED("email address not verified");
	
	
	
	
	
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private String errorMessage;
	
	ErrorMessages(String errorMessage){
		this.errorMessage = errorMessage;
	}

}
