package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.exception.IllegalBalanceException;

public class ClassicAccountRule implements AccountRule
{
    private static final long MINIMUM_BALANCE = -1000L;
    private static final long MAXIMUM_BALANCE = 10000000L;


    @Override
    public void withdrawAuthorized(long initialAccountAmount, long withdrawAmount) throws IllegalBalanceException
    {
        if((initialAccountAmount - withdrawAmount) < ClassicAccountRule.MINIMUM_BALANCE)
        {
            throw new IllegalBalanceException();
        }
    }

    @Override
    public void addAuthorized(long initialAccountAmount, long addAmount) throws IllegalBalanceException
    {
        if((initialAccountAmount + addAmount) > ClassicAccountRule.MAXIMUM_BALANCE)
        {
            throw new IllegalBalanceException();
        }
    }
}
