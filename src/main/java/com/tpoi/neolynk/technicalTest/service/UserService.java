package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.entity.Account;
import com.tpoi.neolynk.technicalTest.entity.User;
import com.tpoi.neolynk.technicalTest.exception.RepositoryException;
import com.tpoi.neolynk.technicalTest.repository.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserService
{
    private final Repository<User> userRepository;

    public UserService()
    {
        userRepository = new Repository<>();
    }

    public void createUser(Optional<User> user) throws RepositoryException
    {
        userRepository.save(user);
    }

    public void updateUser(Optional<User> user) throws RepositoryException
    {
        userRepository.save(user);
    }

    public Optional<User> getUser(UUID uuid) throws RepositoryException
    {
        return userRepository.findById(uuid);
    }

    public Set<Account> getAccounts(UUID uuid) throws RepositoryException
    {
        return getUser(uuid).map(User::getAccounts).orElse(new HashSet<>());
    }

    public void linkAccountToUser(UUID uuid, Optional<Account> account) throws RepositoryException
    {
        Optional<User> user = getUser(uuid);


        Stream.concat(user.map(User::getAccounts).filter(accounts -> !accounts.isEmpty()).orElse(new HashSet<>()).stream(), Stream.of(account)).collect(Collectors.toSet());

        System.out.println(user.map(User::getAccounts));
        updateUser(user);
    }
}
