package com.miniaml;

import java.math.BigDecimal;
import java.util.Date;

import com.miniaml.model.Account;
import com.miniaml.model.Customer;

public class Main {
    public static void main(String[] args) {
        Date date = new Date();
        long time = date.getTime();
        System.out.println(date);
        System.out.println(time);
        date.setTime(time + 86400000);
        System.out.println(date);
    }
}
