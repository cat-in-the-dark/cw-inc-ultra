package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 03.11.14.
 */
@FunctionalInterface
public interface LogicalFunction {
    void doLogic(long globalTime, long delay) throws InterruptedException;
}
