package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.AbstractSystem;
import com.catinthedark.cw_inc.lib.Port;
import com.catinthedark.cw_inc.lib.QueuePort;

import java.util.Arrays;
import java.util.List;

/**
 * Created by over on 08.11.14.
 */
public class System1 implements AbstractSystem {
    public final Port<String> messagePoll = new QueuePort<>(this::onMessage);
    private final List<Port> ports = Arrays.asList(messagePoll);

    @Override
    public List<Port> getPorts() {
        return ports;
    }


    @Override
    public void onStart() {
        System.out.println("Sys1: Wait for message...");
    }

    final Thread mainThread;

    public System1(Thread mainThread) {
        this.mainThread = mainThread;
    }

    private void onMessage(long globalTime, String msg) throws InterruptedException {
        System.out.println("Sys1: Got message: " + msg);
        System.out.println("Sys1: Stopping sys thread!");
        throw new InterruptedException("well done!");
    }
}
