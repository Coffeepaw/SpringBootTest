package com.devsu.hackerearth.backend.account.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.devsu.hackerearth.backend.account.exception.NotFoundException;
import com.devsu.hackerearth.backend.account.model.dto.ClientStatement;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;

public interface TransactionService {

    public List<TransactionDto> getAll();

    public TransactionDto getById(Long id) throws NotFoundException;

    public TransactionDto create(TransactionDto transactionDto) throws Exception;

    public List<ClientStatement> getAllByAccountClientIdAndDateBetween(Long clientId,
            @Param("dateTransactionStart") Date dateTransactionStart,
            @Param("dateTransactionEnd") Date dateTransactionEnd) throws Exception;

    public TransactionDto getLastByAccountId(Long accountId);
}
