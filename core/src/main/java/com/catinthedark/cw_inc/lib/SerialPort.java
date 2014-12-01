package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 14.11.14.
 */
public class SerialPort<T> implements Port<T> {
    final AbstractSystemDef systemDef;
    final DispatchableLogicFunction<T> fn;

    public SerialPort(AbstractSystemDef systemDef, DispatchableLogicFunction<T> fn) {
        this.systemDef = systemDef;
        this.fn = fn;
    }

    @Override
    public void write(T msg, RunnableEx onWrite) {
        systemDef.masterQueue.add(() -> {
            fn.dispatch(msg);
            onWrite.run();
        });
    }
}
