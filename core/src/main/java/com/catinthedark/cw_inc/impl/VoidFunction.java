package com.catinthedark.cw_inc.impl;

/**
 * Created by over on 04.12.14.
 */
@FunctionalInterface
public interface VoidFunction<T> {
    void apply(T val);
}
