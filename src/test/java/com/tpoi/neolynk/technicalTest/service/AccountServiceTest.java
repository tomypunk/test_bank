package com.tpoi.neolynk.technicalTest.service;

import com.tpoi.neolynk.technicalTest.repository.Repository;
import org.junit.Test;

public class AccountServiceTest
{
    @Test
    public void withdrawAndReportBalance_positive_value()
    {
        AccountService accountService = new AccountService(new Repository<>());

    }


}
