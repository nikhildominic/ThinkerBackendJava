package com.thinker.app.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "thinkers")
public class Users {

	@Id
	private String email;
	private String password;
	private String fullname;
	private int identifier=1;
}
