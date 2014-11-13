package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Pipe;
import com.catinthedark.cw_inc.lib.Updater;

/**
 * Created by over on 08.11.14.
 */
public class System2Def extends AbstractSystemDef {
    public static System2Def instance() {
        return new System2Def(new Sys());
    }

    public System2Def(Sys sys) {
        super(new Updater[]{new Updater(sys::updaterMain)}, 1000);
        this.sys = sys;
    }

    private final Sys sys;

    public Pipe<String> messagePipe() {
        return sys.messagePipe;
    }


    private static class Sys {
        public final Pipe<String> messagePipe = new Pipe<>();

        private void updaterMain(long globalTime, long delay) throws InterruptedException {
            System.out.println("Sys2: Send message to pipe!");
            messagePipe.write("Hi, Over!");
        }
    }
}
