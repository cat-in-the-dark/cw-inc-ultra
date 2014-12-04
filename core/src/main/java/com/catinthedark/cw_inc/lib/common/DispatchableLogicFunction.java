package com.catinthedark.cw_inc.lib.common;

/**
 * Created by over on 03.11.14.
 */
@FunctionalInterface
public interface DispatchableLogicFunction<T> {
    public void dispatch(T emitted) throws InterruptedException;
}
