package com.ajiranet.connections.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Response {

	boolean patternMatched;
	boolean isError;
	String message;
	String errorMessage;

}
