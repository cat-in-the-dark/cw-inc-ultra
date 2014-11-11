package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 11.11.14.
 */
public interface Port<T> {
    public void write(T msg);

    public void dispatch(boolean isBlocking) throws InterruptedException;
}
