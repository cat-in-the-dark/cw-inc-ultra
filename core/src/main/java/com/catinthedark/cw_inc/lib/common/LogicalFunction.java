package com.catinthedark.cw_inc.lib.common;

/**
 * Created by over on 03.11.14.
 */
@FunctionalInterface
public interface LogicalFunction {
    void apply(float delay) throws InterruptedException;
}
