package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.entity.Account;
import com.tpoi.neolynk.technicalTest.exception.IdNotFound;
import com.tpoi.neolynk.technicalTest.exception.IllegalBalanceException;
import com.tpoi.neolynk.technicalTest.exception.InvalidOperation;
import com.tpoi.neolynk.technicalTest.exception.RepositoryException;
import com.tpoi.neolynk.technicalTest.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public class AccountService
{
    private final Repository<Account> accountRepository;

    public AccountService()
    {
        accountRepository = new Repository<>();
    }


    /**
     * Withdraw money from account
     *
     * @param id              - id of the account
     * @param withdrawnAmount - amount to withdraw
     * @param rule            - rules to verify during withdraw
     * @return the amount of the account
     * @throws IllegalBalanceException if withdraw is not authorized
     * @throws IdNotFound              if we cannot found the account
     * @throws Repository              if we have problems with da
     */
    public long withdrawAndReportBalance(UUID id, long withdrawnAmount, AccountRule rule)
            throws IllegalBalanceException, IdNotFound, RepositoryException, InvalidOperation
    {
        Account account = accountRepository.findById(id).orElseThrow(() -> new IdNotFound("User doesn't with uuid " + id + " exist"));
        rule.withdrawAuthorized(account.getBalance(), withdrawnAmount);

        //TODO : tester le fait de ne pas insérer du négatif
        account.setBalance(account.getBalance() - withdrawnAmount);
        accountRepository.save(Optional.of(account));
        return account.getBalance();
    }

    /**
     * Add money to account
     *
     * @param account   - account we want to modify
     * @param addAmount - amount to add
     * @param rule      - rules to verify during add
     * @return the new amount of the account
     * @throws IllegalBalanceException
     */
    long addAndReportBalance(Account account, long addAmount, AccountRule rule) throws IllegalBalanceException
    {
        return 1L;
    }


    public void createAccount(Optional<Account> account) throws RepositoryException
    {
        accountRepository.save(account);
    }
}
