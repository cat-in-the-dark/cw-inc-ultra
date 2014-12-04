package com.catinthedark.cw_inc.lib.io;

import com.catinthedark.cw_inc.lib.common.RunnableEx;

/**
 * Created by over on 11.11.14.
 */
public interface Port<T> {
    public void write(T msg, RunnableEx onWrite) throws InterruptedException;
}
