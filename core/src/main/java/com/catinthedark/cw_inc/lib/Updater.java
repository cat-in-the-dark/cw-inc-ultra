package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 08.11.14.
 */
public class Updater {
    final LogicalFunction fn;
    final int period;
    final UpdateConditionFn condFn;


    public Updater(LogicalFunction fn, int period, UpdateConditionFn condFn) {
        this.fn = fn;
        this.period = period;
        this.condFn = condFn;
    }

    public Updater(LogicalFunction fn, UpdateConditionFn condFn) {
        this(fn, 1, condFn);
    }

    public Updater(LogicalFunction fn) {
        this(fn, 1, null);
    }

}
