package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 08.11.14.
 */
public class Updater {
    final LogicalFunction fn;
    final int period;

    public Updater(LogicalFunction fn, int period) {
        this.fn = fn;
        this.period = period;
    }

    public Updater(LogicalFunction fn) {
        this(fn, 1);
    }
}
