package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 14.11.14.
 */
public class DirectPort<T> implements Port<T> {
    private final DispatchableLogicFunction<T> fn;

    public DirectPort(DispatchableLogicFunction<T> fn) {
        this.fn = fn;
    }

    @Override
    public void write(T msg, RunnableEx onWrite) throws InterruptedException{
        fn.dispatch(msg);
        onWrite.run();
    }
}
