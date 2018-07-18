package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.entity.AbstractEntity;
import com.tpoi.neolynk.technicalTest.entity.Account;
import com.tpoi.neolynk.technicalTest.entity.Address;
import com.tpoi.neolynk.technicalTest.entity.User;
import com.tpoi.neolynk.technicalTest.exception.RepositoryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class UserServiceTest
{
    private User testUser;
    private Account account;
    private Optional<User> user;
    private UserService userService;
    private AccountService accountService;

    @Before
    public void init() throws ParseException
    {
        Address address = Address.builder()
                .city("Paris")
                .country("France")
                .postalCode("75001")
                .street("1 rue de Rivoli")
                .build();

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1985");
        User testUser = User.builder()
                .firstName("Michel")
                .lastName("Durand")
                .address(address)
                .birthDate(birthDate)
                .phoneNumber("0600000000")
                .build();
        testUser.setId(UUID.randomUUID());
        user = Optional.of(testUser);
        userService = new UserService();
        accountService = new AccountService();


        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();
        account = Account.builder().balance(0).startDate(today).endDate(nextYear).build();
        account.setId(UUID.randomUUID());

    }

    @Test
    public void create_and_get_user() throws RepositoryException
    {
        userService.createUser(user);
        Optional<User> resultUser = userService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertEquals(user, resultUser);
    }


    @Test
    public void create_and_update_user() throws Exception
    {
        userService.createUser(user);
        Optional<User> optionalResultUser = userService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertTrue(optionalResultUser.isPresent());
        User resultUser = optionalResultUser.get();
        resultUser.setLastName("TOTO");
        userService.updateUser(Optional.of(resultUser));
        Optional<User> optionalResultUser2 = userService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertEquals(Optional.of(resultUser), optionalResultUser2);
        Assert.assertEquals("TOTO", optionalResultUser2.map(User::getLastName).orElse(null));
    }

    @Test
    public void empty_accounts() throws Exception
    {
        userService.createUser(user);
        Optional<User> optionalResultUser = userService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertTrue(optionalResultUser.isPresent());
        User resultUser = optionalResultUser.get();
        Assert.assertTrue(resultUser.getAccounts().isEmpty());
    }


    @Test
    public void create_user_and_add_account() throws Exception
    {
        accountService.createAccount(Optional.of(account));
        userService.createUser(user);

        userService.linkAccountToUser(user.map(AbstractEntity::getId).orElse(null), Optional.of(account));
        Optional<User> optionalResultUser = userService.getUser(user.map(AbstractEntity::getId).orElse(null));
        Assert.assertTrue(optionalResultUser.isPresent());
        User resultUser = optionalResultUser.get();
        Assert.assertFalse(resultUser.getAccounts().isEmpty());


    }


}
