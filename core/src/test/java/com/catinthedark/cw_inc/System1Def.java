package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Port;

/**
 * Created by over on 08.11.14.
 */
public class System1Def extends AbstractSystemDef {
    @Override
    public void onStart() {
        System.out.println("Sys1: Wait for message...");
    }

    public final Port<String> messagePoll = asyncPort(new Sys()::onMessage);

    private class Sys {
        private void onMessage(long globalTime, String msg) throws InterruptedException {
            System.out.println("Sys1: Got message: " + msg);
            System.out.println("Sys1: Stopping sys thread!");
            throw new InterruptedException("well done!");
        }
    }
}
