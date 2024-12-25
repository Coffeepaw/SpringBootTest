package com.devsu.hackerearth.backend.account.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.exception.NotFoundException;
import com.devsu.hackerearth.backend.account.model.dto.ClientStatement;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping
	public ResponseEntity<List<TransactionDto>> getAll() {
		return new ResponseEntity<>(transactionService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TransactionDto> get(@PathVariable Long id) throws NotFoundException {
		TransactionDto transaction = transactionService.getById(id);
		return transaction != null ? new ResponseEntity<>(transaction, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<TransactionDto> create(@RequestBody TransactionDto transactionDto) throws Exception {
		TransactionDto createdTransaction = transactionService.create(transactionDto);
		return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
	}

	@GetMapping("/clients/{clientId}/report")
	public ResponseEntity<List<ClientStatement>> report(@PathVariable Long clientId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionStart,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionEnd) throws Exception {
		return new ResponseEntity<>(
				transactionService.getAllByAccountClientIdAndDateBetween(clientId, dateTransactionStart,
						dateTransactionEnd),
				HttpStatus.OK);
	}
}
