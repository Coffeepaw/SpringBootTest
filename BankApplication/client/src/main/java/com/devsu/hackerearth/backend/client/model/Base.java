package com.devsu.hackerearth.backend.client.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public class Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
