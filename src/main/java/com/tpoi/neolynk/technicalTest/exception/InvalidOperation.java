package com.tpoi.neolynk.technicalTest.exception;

public class InvalidOperation extends Exception
{
    public InvalidOperation(long amount)
    {
        super("The amount " + amount + " is not allowed");
    }
}
