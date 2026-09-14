package com.miniaml.rule;

import com.miniaml.model.Transaction;

public interface Rule {
    boolean hit(Transaction transaction);
    String name();
}
