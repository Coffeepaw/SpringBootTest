package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.exception.NotFoundException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDto> getAll() {
        return accountRepository.findAll().stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) throws NotFoundException {
        return accountRepository.findById(id)
                .map(account -> modelMapper.map(account, AccountDto.class))
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountDto.class);
    }

    @Override
    public AccountDto update(AccountDto accountDto) throws NotFoundException {
        Account account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new NotFoundException("Account not found"));
        AccountDto editableAccount = modelMapper.map(account, AccountDto.class);
        editableAccount.setActive(accountDto.isActive());
        editableAccount.setClientId(accountDto.getClientId());
        editableAccount.setInitialAmount(accountDto.getInitialAmount());
        editableAccount.setNumber(accountDto.getNumber());
        editableAccount.setType(accountDto.getType());

        Account updatedAccount = accountRepository.save(modelMapper.map(editableAccount, Account.class));
        return modelMapper.map(updatedAccount, AccountDto.class);
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) throws NotFoundException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        AccountDto editableAccount = modelMapper.map(account, AccountDto.class);
        editableAccount.setActive(partialAccountDto.isActive());

        Account updatedAccount = accountRepository.save(modelMapper.map(editableAccount, Account.class));
        return modelMapper.map(updatedAccount, AccountDto.class);
    }

    @Override
    public void deleteById(Long id) throws NotFoundException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        accountRepository.delete(account);
    }

    @Override
    public List<AccountDto> getByClientId(Long clientId) throws Exception {
        return accountRepository.findByClientId(clientId).stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
    }

}
