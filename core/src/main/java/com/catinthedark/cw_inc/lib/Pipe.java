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

    /**
     * ^^ recursion
     *
     * @param msg
     * @throws InterruptedException
     */
    public void write(T msg) throws InterruptedException {
        new RunnableEx() {
            int index = -1;

            @Override
            public void run() throws InterruptedException {
                index++;
                if (index >= ports.size())
                    return;
                System.out.println("index:" + index);
                ports.get(index).write(msg, this);
            }
        }.run();
    }
}
