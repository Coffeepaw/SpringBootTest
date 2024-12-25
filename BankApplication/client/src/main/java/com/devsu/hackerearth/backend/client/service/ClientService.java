package com.devsu.hackerearth.backend.client.service;

import java.util.List;

import com.devsu.hackerearth.backend.client.exception.NotFoundException;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;

public interface ClientService {

	public List<ClientDto> getAll();
	public ClientDto getById(Long id) throws NotFoundException;
	public ClientDto create(ClientDto clientDto);
	public ClientDto update(ClientDto clientDto) throws NotFoundException;
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) throws NotFoundException;
	public void deleteById(Long id) throws NotFoundException;
}
