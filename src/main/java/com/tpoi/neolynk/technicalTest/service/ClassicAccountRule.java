package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.exception.IllegalBalanceException;
import com.tpoi.neolynk.technicalTest.exception.InvalidOperation;

public class ClassicAccountRule implements AccountRule
{
    private static final long MINIMUM_BALANCE = -1000L;
    private static final long MAXIMUM_BALANCE = 10000000L;


    @Override
    public void withdrawAuthorized(long initialAccountAmount, long withdrawAmount)
            throws IllegalBalanceException, InvalidOperation
    {
        checkAmountIsPositive(withdrawAmount);
        if((initialAccountAmount - withdrawAmount) < ClassicAccountRule.MINIMUM_BALANCE)
        {
            throw new IllegalBalanceException();
        }
    }

    @Override
    public void addAuthorized(long initialAccountAmount, long addAmount)
            throws IllegalBalanceException, InvalidOperation
    {
        checkAmountIsPositive(addAmount);
        if((initialAccountAmount + addAmount) > ClassicAccountRule.MAXIMUM_BALANCE)
        {
            throw new IllegalBalanceException();
        }
    }

    /**
     * Check if the amount is positive, we can not have negative value
     *
     * @param amount - amount to check
     * @throws InvalidOperation if amount is negative
     */
    private void checkAmountIsPositive(long amount) throws InvalidOperation
    {
        if(amount < 0)
        {
            throw new InvalidOperation(amount);
        }
    }
}
