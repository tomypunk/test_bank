package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.exception.IllegalBalanceException;
import org.junit.Test;

public class ClassicAccountTest
{
    private final AccountRule accountRule = new ClassicAccountRule();

    @Test
    public void authorized_withdraw() throws Exception
    {
        accountRule.withdrawAuthorized(1000L, 1000L);
    }

    @Test(expected = IllegalBalanceException.class)
    public void unauthorized_withdraw() throws Exception
    {
        accountRule.withdrawAuthorized(0L, 1001L);
    }

    @Test(expected = IllegalBalanceException.class)
    public void unauthorized_add() throws Exception
    {
        accountRule.addAuthorized(6799L, 1000000000L);
    }

    @Test
    public void authorized_add() throws Exception
    {
        accountRule.addAuthorized(1000L, 1000L);
    }
}
