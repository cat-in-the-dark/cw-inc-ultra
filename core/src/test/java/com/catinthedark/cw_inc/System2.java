package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.AbstractSystem;
import com.catinthedark.cw_inc.lib.Pipe;
import com.catinthedark.cw_inc.lib.Updater;

import java.util.Arrays;
import java.util.List;

/**
 * Created by over on 08.11.14.
 */
public class System2 implements AbstractSystem {

    private final Updater main = new Updater(this::updaterMain);
    private final List<Updater> updaters = Arrays.asList(main);

    @Override
    public List<Updater> getUpdaters() {
        return updaters;
    }

    @Override
    public int getMasterDelay() {
        return 1000;
    }

    public final Pipe<String> messagePipe = new Pipe<>();

    private void updaterMain(long globalTime, long delay) throws InterruptedException {
        System.out.println("Sys2: Send message to pipe!");
        messagePipe.write("Hi, Over!");
    }
}
