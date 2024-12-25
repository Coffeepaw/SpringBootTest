package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.exception.NotFoundException;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import org.modelmapper.*;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private final ClientRepository clientRepository;

	@Autowired
	private ModelMapper modelMapper;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public List<ClientDto> getAll() {
		return clientRepository.findAll().stream()
				.map(client -> modelMapper.map(client, ClientDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) throws NotFoundException {
		return clientRepository.findById(id)
				.map(client -> modelMapper.map(client, ClientDto.class))
				.orElseThrow(() -> new NotFoundException("Client not found"));
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		Client client = modelMapper.map(clientDto, Client.class);
		Client savedClient = clientRepository.save(client);
		return modelMapper.map(savedClient, ClientDto.class);
	}

	@Override
	public ClientDto update(ClientDto clientDto) throws NotFoundException {
		Client client = clientRepository.findById(clientDto.getId())
				.orElseThrow(() -> new NotFoundException("Client not found"));
		ClientDto editableClient = modelMapper.map(client, ClientDto.class);
		editableClient.setActive(clientDto.isActive());
		editableClient.setAddress(clientDto.getAddress());
		editableClient.setAge(clientDto.getAge());
		editableClient.setDni(clientDto.getDni());
		editableClient.setGender(clientDto.getGender());
		editableClient.setName(clientDto.getName());
		editableClient.setPassword(clientDto.getPassword());
		editableClient.setPhone(clientDto.getPhone());

		Client updatedClient = clientRepository.save(modelMapper.map(editableClient, Client.class));
		return modelMapper.map(updatedClient, ClientDto.class);
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) throws NotFoundException {
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Client not found"));
		ClientDto editableClient = modelMapper.map(client, ClientDto.class);
		editableClient.setActive(partialClientDto.isActive());

		Client updatedClient = clientRepository.save(modelMapper.map(editableClient, Client.class));
		return modelMapper.map(updatedClient, ClientDto.class);
	}

	@Override
	public void deleteById(Long id) throws NotFoundException {
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Client not found"));
		clientRepository.delete(client);
	}
}
