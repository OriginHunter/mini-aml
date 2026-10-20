package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.util.List;

public interface Rule<T> {
    List<Transaction> findHits(T target);

    boolean hit(T target);

    String name();
}
