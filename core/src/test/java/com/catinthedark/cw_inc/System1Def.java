package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Port;
import com.catinthedark.cw_inc.lib.QueuePort;

/**
 * Created by over on 08.11.14.
 */
public class System1Def extends AbstractSystemDef {
//    @Override
//    public void onStart() {
//        System.out.println("Sys1: Wait for message...");
//    }

    public static System1Def instance() {
        Sys sys = new Sys();
        Port<String> messagePoll = new QueuePort<>(sys::onMessage);
        return new System1Def(messagePoll);
    }

    private System1Def(Port<String> messagePoll) {
        super(new Port[]{messagePoll});
        this.messagePoll = messagePoll;
    }

    public final Port<String> messagePoll;

    private static class Sys {
        private void onMessage(long globalTime, String msg) throws InterruptedException {
            System.out.println("Sys1: Got message: " + msg);
            System.out.println("Sys1: Stopping sys thread!");
            throw new InterruptedException("well done!");
        }
    }
}
