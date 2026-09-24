package com.miniaml.rule;

public interface Rule<T> {
    boolean hit(T target);

    String name();
}
