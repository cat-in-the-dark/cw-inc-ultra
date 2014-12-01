package com.catinthedark.cw_inc.lib;

import java.util.concurrent.BlockingQueue;

/**
 * Created by over on 14.11.14.
 */
public class AsyncPort<T> implements Port<T> {
    private final BlockingQueue<RunnableEx> queue;
    private final DispatchableLogicFunction<T> fn;

    public AsyncPort(BlockingQueue<RunnableEx> queue, DispatchableLogicFunction<T> fn) {
        this.queue = queue;
        this.fn = fn;
    }

    @Override
    public void write(T msg, RunnableEx onWrite) throws InterruptedException {
        queue.put(() -> fn.dispatch(msg));
        onWrite.run();
    }
}
