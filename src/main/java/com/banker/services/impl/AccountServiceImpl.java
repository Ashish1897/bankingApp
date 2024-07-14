package com.banker.services.impl;

import com.banker.dto.AccountDto;
import com.banker.entity.Account;
import com.banker.mapper.AccountMapper;
import com.banker.repository.AccountRepository;
import com.banker.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account Does Not Exists"));
        return AccountMapper.mapToAccountDto(account);
    }


    @Override
    public AccountDto deposit(Long id, Double amount) {
        //first check if account exist or not
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account Does Not Exists"));

        double balance = account.getBalance() + amount;
        account.setBalance(balance);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, Double amount) {
        //first check if account exist or not
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account Does Not Exists"));

        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient Bank Balance");
        }
        double balance = account.getBalance() - amount;
        account.setBalance(balance);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return  accounts.stream().map( account -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());
    }

    public String deleteAccount(Long id){
        //first check if account exist or not
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account Does Not Exists"));
        accountRepository.delete(account);
        return "Account Deleted";
    }
}
