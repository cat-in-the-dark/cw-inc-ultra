package com.catinthedark.cw_inc.lib.io;

import com.catinthedark.cw_inc.lib.common.DispatchableLogicFunction;
import com.catinthedark.cw_inc.lib.common.RunnableEx;

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
    public void write(T msg, Runnable onWrite) throws InterruptedException {
        queue.put(() -> fn.apply(msg));
        onWrite.run();
    }
}
