package com.devsu.hackerearth.backend.account.service;

import java.util.List;

import com.devsu.hackerearth.backend.account.exception.NotFoundException;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;

public interface AccountService {

	public List<AccountDto> getAll();

	public AccountDto getById(Long id) throws NotFoundException;

	public List<AccountDto> getByClientId(Long clientId) throws Exception;

	public AccountDto create(AccountDto accountDto);

	public AccountDto update(AccountDto accountDto) throws NotFoundException;

	public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) throws NotFoundException;

	public void deleteById(Long id) throws NotFoundException;
}
