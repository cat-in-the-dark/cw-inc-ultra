package com.catinthedark.cw_inc.lib.io;

import com.catinthedark.cw_inc.lib.common.DispatchableLogicFunction;
import com.catinthedark.cw_inc.lib.common.RunnableEx;

/**
 * Created by over on 14.11.14.
 */
public class DirectPort<T> implements Port<T> {
    private final DispatchableLogicFunction<T> fn;

    public DirectPort(DispatchableLogicFunction<T> fn) {
        this.fn = fn;
    }

    @Override
    public void write(T msg, Runnable onWrite) throws InterruptedException{
        fn.apply(msg);
        onWrite.run();
    }
}
