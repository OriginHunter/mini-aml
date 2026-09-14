package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.util.List;

public interface Rule {
    boolean hit(List<Transaction> transactions);

    String name();
}
