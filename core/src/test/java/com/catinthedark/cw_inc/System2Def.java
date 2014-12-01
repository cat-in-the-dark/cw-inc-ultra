package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Pipe;

/**
 * Created by over on 08.11.14.
 */
public class System2Def extends AbstractSystemDef {
    private final Sys sys = new Sys();
    public final Pipe<String> messagePipe = new Pipe<>();

    {
        updater(sys::updaterMain);
        masterDelay = 1000;
    }

    private class Sys {
        private void updaterMain(float delay) throws InterruptedException {
            System.out.println("Sys2: Send message to pipe!");
            messagePipe.write("Hi, Over!");
        }
    }
}
