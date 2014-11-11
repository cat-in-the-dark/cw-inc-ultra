package com.catinthedark.cw_inc.lib;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by over on 08.11.14.
 */
public class QueuePort<T> implements Port<T> {
    private static final int DEFAULT_QUEUE_SIZE = 100;

    private final DispatchableLogicFunction<T> fn;
    private final LinkedBlockingQueue<T> queue;
    private final boolean singleMessage;

    public QueuePort(DispatchableLogicFunction<T> fn) {
        this(fn, false);
    }

    public QueuePort(DispatchableLogicFunction<T> fn, boolean singleMessage) {
        int queueSize = singleMessage ? 1 : DEFAULT_QUEUE_SIZE;
        queue = new LinkedBlockingQueue<>(queueSize);

        this.fn = fn;
        this.singleMessage = singleMessage;
    }

    public void write(T msg) {
        if (singleMessage)
            queue.offer(msg);
        else {

            try {
                queue.put(msg);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private interface NoArgFunction<T> {
        T call();
    }

    public void dispatch(boolean isBlocking) throws InterruptedException {
        NoArgFunction<T> queueOp;
        if (isBlocking)
            queueOp = queue::peek;
        else
            queueOp = queue::poll;

        T msg;
        while ((msg = queueOp.call()) != null) {
            fn.dispatch(0, msg);
        }

    }
}
