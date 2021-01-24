package com.thinker.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetIdentifierResponse {

	private String email;
	private String fullname;
	private int identifier;
	
	
}
