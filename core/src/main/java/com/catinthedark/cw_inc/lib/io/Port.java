package com.catinthedark.cw_inc.lib.io;

/**
 * Created by over on 11.11.14.
 */
public interface Port<T> {
    public void write(T msg, Runnable onWrite) throws InterruptedException;
}
