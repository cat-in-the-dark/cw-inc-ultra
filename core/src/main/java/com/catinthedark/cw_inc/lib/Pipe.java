package com.catinthedark.cw_inc.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by over on 08.11.14.
 */
public class Pipe<T> {
    private final List<Port<T>> ports = new ArrayList<>();

    public void connect(Port<T>... ports) {
        for (Port port : ports)
            this.ports.add(port);
    }

    public void write(T msg) throws InterruptedException {
        for (Port p : ports)
            p.write(msg);
    }
}
