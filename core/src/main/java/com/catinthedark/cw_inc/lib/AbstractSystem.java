package com.catinthedark.cw_inc.lib;

import java.util.Collections;
import java.util.List;

/**
 * Created by over on 08.11.14.
 */
public interface AbstractSystem {
    default List<Port> getPorts() {
        return Collections.emptyList();
    }

    default List<Updater> getUpdaters() {
        return Collections.emptyList();
    }

    default int getMasterDelay() {
        return 0;
    }

    default void update() throws InterruptedException {
        for (Updater updater : getUpdaters())
            updater.fn.doLogic(0, 0);
        for (Port port : getPorts())
            port.dispatch(isQueueBlocking());
    }

    default String getName() {
        return toString();
    }

    default void start() {
        if (isQueueBlocking() == true && getUpdaters() != null)
            throw new RuntimeException("Could not mix blocking ports with updaters! Please, choose one");
        onStart();
    }

    default void onStart() {
        System.out.println("Starting system: " + getName());
    }

    default boolean isQueueBlocking() {
        return false;
    }
}