package com.devsu.hackerearth.backend.account.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.exception.BadRequestException;
import com.devsu.hackerearth.backend.account.exception.NotFoundException;
import com.devsu.hackerearth.backend.account.exception.PreconditionException;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.ClientStatement;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
            AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public List<TransactionDto> getAll() {
        return transactionRepository.findAll().stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) throws NotFoundException {
        return transactionRepository.findById(id)
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto) throws Exception {
        AccountDto accountDto = accountService.getById(transactionDto.getAccountId());
        if (!accountDto.isActive()) {
            throw new PreconditionException("La cuenta no se encuentra activa");
        }

        double currentBalance = accountDto.getInitialAmount();

        TransactionDto lastTransaction = getLastByAccountId(transactionDto.getAccountId());
        if (lastTransaction != null) {
            currentBalance = lastTransaction.getBalance();
        }

        if (transactionDto.getAmount() < 0 && currentBalance <= transactionDto.getAmount()) {
            throw new PreconditionException("Saldo no disponible");
        }
        double balance = currentBalance + transactionDto.getAmount();
        transactionDto.setBalance(balance);

        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDto.class);
    }

    @Override
    public List<ClientStatement> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) throws Exception {

        if (dateTransactionStart == null || dateTransactionEnd == null
                || dateTransactionEnd.before(dateTransactionStart)) {
            throw new BadRequestException("Fechas invalidas");
        }
        List<ClientStatement> clientStatements = new ArrayList<>();

        List<AccountDto> accountDto = accountService.getByClientId(clientId);
        for (AccountDto singleAccount : accountDto) {
            ClientStatement clientStatement = new ClientStatement();
            List<BankStatementDto> bankStatements = new ArrayList<>();

            clientStatement.setClientId(singleAccount.getClientId());
            clientStatement.setInitialAmount(singleAccount.getInitialAmount());
            clientStatement.setNumber(singleAccount.getNumber());
            clientStatement.setType(singleAccount.getType());
            clientStatement.setActive(singleAccount.isActive());

            TransactionDto lastTransaction = getLastByAccountId(singleAccount.getId());
            clientStatement.setCurrentBalance(lastTransaction == null ? 0 : lastTransaction.getBalance());

            List<TransactionDto> listTransaction = transactionRepository
                    .findByAccountIdAndDateBetween(singleAccount.getId(),
                            dateTransactionStart, dateTransactionEnd)
                    .stream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                    .collect(Collectors.toList());
            listTransaction.stream()
                    .forEach(transaction -> {
                        BankStatementDto bankStatement = new BankStatementDto(
                                transaction.getDate(),
                                singleAccount.getClientId().toString(),
                                singleAccount.getNumber(),
                                singleAccount.getType(),
                                singleAccount.getInitialAmount(),
                                singleAccount.isActive(),
                                transaction.getType(),
                                transaction.getAmount(),
                                transaction.getBalance());
                        bankStatements.add(bankStatement);
                    });
            clientStatement.setBankStatements(bankStatements);
        }

        return clientStatements;
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        return transactionRepository.findTopByAccountIdOrderByDateDesc(accountId)
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .orElse(null);
    }

}
