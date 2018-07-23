package com.tpoi.neolynk.technicalTest.exception;

public class IllegalBalanceException extends Exception
{
    public IllegalBalanceException()
    {
        super("Withdraw not allowed");
    }
}
