package com.devsu.hackerearth.backend.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.client.exception.NotFoundException;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	@Autowired
	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public ResponseEntity<List<ClientDto>> getAll() {
		return new ResponseEntity<>(clientService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClientDto> get(@PathVariable Long id) throws NotFoundException {
		ClientDto client = clientService.getById(id);
		return client != null ? new ResponseEntity<>(client, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto) {
		ClientDto createdClient = clientService.create(clientDto);
		return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<ClientDto> update(@PathVariable Long id, @RequestBody ClientDto clientDto)
			throws NotFoundException {
		ClientDto updatedClient = clientService.update(clientDto);
		return new ResponseEntity<>(updatedClient, HttpStatus.OK);
	}

	@PatchMapping
	public ResponseEntity<ClientDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialClientDto partialClientDto) throws NotFoundException {
		ClientDto updatedClient = clientService.partialUpdate(id, partialClientDto);
		return new ResponseEntity<>(updatedClient, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException {
		clientService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
