package com.devsu.hackerearth.backend.account.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatement extends AccountDto {
    private double currentBalance;
    private List<BankStatementDto> bankStatements;
}