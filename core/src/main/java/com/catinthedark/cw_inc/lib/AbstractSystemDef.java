package com.catinthedark.cw_inc.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by over on 08.11.14.
 */
public abstract class AbstractSystemDef {
    protected final Updater[] updaters;
    protected final boolean isQueueBlocking;
    public final int masterDelay;

    public final BlockingQueue<RunnableEx> masterQueue = new LinkedBlockingQueue<>();
    private final List<BlockingQueue<RunnableEx>> secondaryQueues = new ArrayList<>();

    private AbstractSystemDef(Updater[] updaters, int masterDelay, boolean isQueueBlocking) {
        this.updaters = updaters;
        this.masterDelay = masterDelay;
        this.isQueueBlocking = isQueueBlocking;
    }

    public <T> Port<T> serialPort(DispatchableLogicFunction<T> fn) {
        return new SerialPort<>(this, fn);
    }

    public <T> Port<T> directPort(DispatchableLogicFunction<T> fn) {
        return new DirectPort<>(fn);
    }

    public <T> Port<T> asyncPort(DispatchableLogicFunction<T> fn) {
        return new AsyncPort<>(masterQueue, fn);
    }

    public <T> Port<T> singleMsgPort(DispatchableLogicFunction<T> fn) {
        BlockingQueue<RunnableEx> queue = new LinkedBlockingDeque<>();
        secondaryQueues.add(queue);
        return new SingleMsgPort<>(queue, fn);
    }

    public AbstractSystemDef() {
        this(new Updater[]{}, 0, true);
    }


    public AbstractSystemDef(Updater[] updaters, int masterDelay) {
        this(updaters, masterDelay, false);
    }


    private interface NoArgFunction<T> {
        T call() throws InterruptedException;
    }


    void update() throws InterruptedException {
        for (Updater updater : updaters)
            updater.fn.doLogic(0, 0);

        NoArgFunction<RunnableEx> queueOp;
        if (isQueueBlocking)
            queueOp = masterQueue::take;
        else
            queueOp = masterQueue::poll;

        RunnableEx runnableEx;
        while ((runnableEx = queueOp.call()) != null) {
            //System.out.println("get runnableEx");
            runnableEx.run();
        }

        for (BlockingQueue<RunnableEx> queue : secondaryQueues)
            while ((runnableEx = queue.poll()) != null)
                runnableEx.run();

    }

    public void start() {
        System.out.println("Starting system: " + this.toString());
        onStart();
    }

    public void onStart() {
        if (!secondaryQueues.isEmpty() && isQueueBlocking)
            throw new RuntimeException("Could not mix blocking master queue with Async ports! Choose one feature");
    }


    protected void defer(RunnableEx runnableEx, int delay) {
        new Thread(() -> {
            try {
                System.out.println("add defer:orig");
                Thread.currentThread().sleep(delay);
                masterQueue.put(runnableEx);
            } catch (InterruptedException ignored) {
            }
            System.out.println("Shutdown defer thread");
        }).start();

    }
}