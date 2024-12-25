package com.devsu.hackerearth.backend.account.controller;

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

import com.devsu.hackerearth.backend.account.exception.NotFoundException;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll() {
		return new ResponseEntity<>(accountService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id) throws NotFoundException {
		AccountDto account = accountService.getById(id);
		return account != null ? new ResponseEntity<>(account, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto) {
		AccountDto createdAccount = accountService.create(accountDto);
		return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto)
			
			throws NotFoundException {
		AccountDto updatedAccount = accountService.update(accountDto);
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
	}

	@PatchMapping
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialAccountDto partialAccountDto) throws NotFoundException {
		AccountDto updatedAccount = accountService.partialUpdate(id, partialAccountDto);
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException {
		accountService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
