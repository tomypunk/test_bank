package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.exception.IllegalBalanceException;
import com.tpoi.neolynk.technicalTest.exception.InvalidOperation;

public interface AccountRule
{
    /**
     * Check if you are authorized to withdraw the amount
     *
     * @param initialAccountAmount - initial amount on the account
     * @param withdrawAmount       - withdraw value to remove
     * @throws IllegalBalanceException throws if the withdraw is not authorized
     */
    void withdrawAuthorized(long initialAccountAmount, long withdrawAmount)
            throws IllegalBalanceException, InvalidOperation;

    /**
     * check if you are authorized to add the amount
     *
     * @param initialAccountAmount - initial amount on the account
     * @param addAmount            - amount to add
     * @throws IllegalBalanceException throws if you want to add too much
     */
    void addAuthorized(long initialAccountAmount, long addAmount) throws IllegalBalanceException, InvalidOperation;

}
