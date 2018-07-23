package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.entity.Account;
import com.tpoi.neolynk.technicalTest.entity.User;
import com.tpoi.neolynk.technicalTest.exception.IdNotFound;
import com.tpoi.neolynk.technicalTest.exception.IllegalBalanceException;
import com.tpoi.neolynk.technicalTest.exception.InvalidOperation;
import com.tpoi.neolynk.technicalTest.exception.RepositoryException;
import com.tpoi.neolynk.technicalTest.repository.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BankService
{
    private final Repository<Account> accountRepository;
    private final Repository<User> userRepository;

    public BankService(Repository<Account> accountRepository, Repository<User> userRepository)
    {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
        Account account = accountRepository.findById(id).orElseThrow(() -> new IdNotFound("Account with uuid " + id + " doesn't exist"));
        rule.withdrawAuthorized(account.getBalance(), withdrawnAmount);
        account.setBalance(account.getBalance() - withdrawnAmount);
        accountRepository.save(Optional.of(account));
        return account.getBalance();
    }

    /**
     * Add money to account
     *
     * @param id        - id of the account
     * @param addAmount - amount to add
     * @param rule      - rules to verify during add
     * @return the new amount of the account
     * @throws IllegalBalanceException
     */
    long addAndReportBalance(UUID id, long addAmount, AccountRule rule) throws IllegalBalanceException, IdNotFound,
                                                                               InvalidOperation, RepositoryException
    {
        Account account = accountRepository.findById(id).orElseThrow(() -> new IdNotFound("Account with uuid " + id + " doesn't exist"));
        rule.addAuthorized(account.getBalance(), addAmount);
        account.setBalance(account.getBalance() + addAmount);
        accountRepository.save(Optional.of(account));
        return account.getBalance();
    }


    /**
     * Create a bank account
     *
     * @param account - the account to create
     * @throws RepositoryException
     */
    public void createAccount(Optional<Account> account) throws RepositoryException
    {
        accountRepository.save(account);
    }

    /**
     * Create an user
     *
     * @param user - the user to create
     * @throws RepositoryException
     */
    public void createUser(Optional<User> user) throws RepositoryException
    {
        userRepository.save(user);
    }

    /**
     * Update an user
     *
     * @param user - the user to update
     * @throws RepositoryException
     */
    public void updateUser(Optional<User> user) throws RepositoryException
    {
        userRepository.save(user);
    }

    /**
     * Get a user by uuid
     *
     * @param uuid - uuid of the account
     * @return the account in the database
     * @throws RepositoryException
     */
    public Optional<User> getUser(UUID uuid) throws RepositoryException
    {

        return userRepository.findById(uuid);
    }

    /**
     * get the accounts with the uuid of the user
     *
     * @param uuid - id of the account
     * @return Set of all the bank account for a user
     * @throws RepositoryException
     */
    public Set<Account> getAccounts(UUID uuid) throws RepositoryException
    {
        return getUser(uuid).map(User::getAccounts).orElse(new HashSet<>());
    }

    /**
     * link an account to an user
     *
     * @param uuid    - id of the user
     * @param account - account to link
     * @throws RepositoryException
     */
    public void linkAccountToUser(UUID uuid, Optional<Account> account) throws RepositoryException
    {
        Optional<User> user = getUser(uuid);
        account.ifPresent(
                user.map(User::getAccounts)
                        .orElseGet(Collections::emptySet)::add);
        updateUser(user);
    }
}
