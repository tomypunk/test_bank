package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.entity.AbstractEntity;
import com.tpoi.neolynk.technicalTest.entity.Account;
import com.tpoi.neolynk.technicalTest.entity.Address;
import com.tpoi.neolynk.technicalTest.entity.User;
import com.tpoi.neolynk.technicalTest.exception.IdNotFound;
import com.tpoi.neolynk.technicalTest.exception.IllegalBalanceException;
import com.tpoi.neolynk.technicalTest.exception.InvalidOperation;
import com.tpoi.neolynk.technicalTest.exception.RepositoryException;
import com.tpoi.neolynk.technicalTest.repository.Repository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BankServiceTest
{

    private User testUser;
    private Account account;
    private Optional<User> user;
    private BankService bankService;

    @Before
    public void init() throws ParseException, RepositoryException
    {
        Address address = Address.builder()
                .city("Paris")
                .country("France")
                .postalCode("75001")
                .street("1 rue de Rivoli")
                .build();

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1985");
        testUser = User.builder()
                .firstName("Michel")
                .lastName("Durand")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000000")
                .build();
        testUser.setId(UUID.randomUUID());
        user = Optional.of(testUser);

        bankService = new BankService(new Repository<>(), new Repository<>());


        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();
        account = Account.builder().balance(0).startDate(today).endDate(nextYear).build();
        account.setId(UUID.randomUUID());
    }


    @Test
    public void withdrawAndReportBalance_positive_value() throws RepositoryException, IdNotFound,
                                                                 IllegalBalanceException, InvalidOperation
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);
        bankService.linkAccountToUser(user.get().getId(), Optional.of(account));

        long balance = bankService.withdrawAndReportBalance(account.getId(), 100L, new ClassicAccountRule());
        assertEquals(-100L, balance);
    }

    @Test(expected = InvalidOperation.class)
    public void withdrawAndReportBalance_negative_value() throws RepositoryException, IdNotFound,
                                                                 IllegalBalanceException, InvalidOperation
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);
        bankService.linkAccountToUser(testUser.getId(), Optional.of(account));
        bankService.withdrawAndReportBalance(account.getId(), -100L, new ClassicAccountRule());
    }

    @Test(expected = IllegalBalanceException.class)
    public void withdrawAndReportBalance_too_much_value() throws RepositoryException, IdNotFound,
                                                                 IllegalBalanceException, InvalidOperation
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);
        bankService.linkAccountToUser(testUser.getId(), Optional.of(account));
        bankService.withdrawAndReportBalance(account.getId(), 190000L, new ClassicAccountRule());
    }

    @Test
    public void create_and_get_user() throws RepositoryException
    {
        bankService.createUser(user);
        Optional<User> resultUser = bankService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertEquals(user, resultUser);
    }


    @Test
    public void create_and_update_user() throws Exception
    {
        bankService.createUser(user);
        Optional<User> optionalResultUser = bankService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertTrue(optionalResultUser.isPresent());
        User resultUser = optionalResultUser.get();
        resultUser.setLastName("TOTO");
        bankService.updateUser(Optional.of(resultUser));
        Optional<User> optionalResultUser2 = bankService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertEquals(Optional.of(resultUser), optionalResultUser2);
        Assert.assertEquals("TOTO", optionalResultUser2.map(User::getLastName).orElse(null));
    }

    @Test
    public void empty_accounts() throws Exception
    {
        bankService.createUser(user);
        Optional<User> optionalResultUser = bankService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertTrue(optionalResultUser.isPresent());
        User resultUser = optionalResultUser.get();
        Assert.assertTrue(resultUser.getAccounts().isEmpty());
    }


    @Test
    public void create_user_and_add_account() throws Exception
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);

        bankService.linkAccountToUser(user.map(AbstractEntity::getId).orElse(null), Optional.of(account));
        Optional<User> optionalResultUser = bankService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertTrue(optionalResultUser.isPresent());
        User resultUser = optionalResultUser.get();
        Assert.assertFalse(resultUser.getAccounts().isEmpty());
    }


    @Test
    public void create_user_get_account() throws Exception
    {
        bankService.createUser(user);
        Optional<User> result = bankService.getUser(testUser.getId());
        assertEquals(user, result);
    }

    @Test
    public void create_user_add_account_and_get_accounts() throws Exception
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);
        bankService.linkAccountToUser(user.map(AbstractEntity::getId).orElse(null), Optional.of(account));
        assertEquals(1, bankService.getAccounts(testUser.getId()).size());
    }


    @Test
    public void addAndReportBalance_valid_amount() throws Exception
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);
        bankService.linkAccountToUser(user.get().getId(), Optional.of(account));
        long balance = bankService.addAndReportBalance(account.getId(), 1000L, new ClassicAccountRule());
        assertEquals(1000L, balance);
    }

    @Test(expected = InvalidOperation.class)
    public void addAndReportBalance_invalid_amount() throws Exception
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);
        bankService.linkAccountToUser(user.get().getId(), Optional.of(account));
        bankService.addAndReportBalance(account.getId(), -1005L, new ClassicAccountRule());
    }


    @Test(expected = IllegalBalanceException.class)
    public void addAndReportBalance_too_much_amount() throws Exception
    {
        bankService.createAccount(Optional.of(account));
        bankService.createUser(user);
        bankService.linkAccountToUser(user.get().getId(), Optional.of(account));
        bankService.addAndReportBalance(account.getId(), 100006000L, new ClassicAccountRule());
    }

}
