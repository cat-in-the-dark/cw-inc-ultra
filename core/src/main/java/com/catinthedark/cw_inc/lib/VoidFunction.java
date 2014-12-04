package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 04.12.14.
 */
@FunctionalInterface
public interface VoidFunction<T> {
    void apply(T val);
}
