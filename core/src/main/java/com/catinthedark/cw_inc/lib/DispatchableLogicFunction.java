package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 03.11.14.
 */
@FunctionalInterface
public interface DispatchableLogicFunction<T> {
    public void dispatch(long globalTime, T emitted) throws InterruptedException;
}
