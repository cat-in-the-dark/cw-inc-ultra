package com.catinthedark.cw_inc.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by over on 08.11.14.
 */
public class Launcher {

    public static class SystemTask {
        final AbstractSystem system;
        final public int delay;

        public SystemTask(AbstractSystem system, int delay) {
            this.system = system;
            this.delay = delay;
        }
    }

    private static List<SystemTask> prepare(AbstractSystem... systems) {
        List<AbstractSystem> sorted = Arrays.stream(systems)
                .sorted((s1, s2) -> Integer.compare(s1.getMasterDelay(), s2.getMasterDelay()))
                .collect(Collectors.toList());

        final List<SystemTask> tasks = new ArrayList<>();

        int delay = sorted.get(0).getMasterDelay();
        tasks.add(new SystemTask(sorted.get(0), sorted.get(0).getMasterDelay()));

        for (int i = 1; i < sorted.size(); i++) {
            AbstractSystem system = sorted.get(i);
            tasks.add(new SystemTask(system, system.getMasterDelay() - delay));
            delay += system.getMasterDelay();
        }

        return tasks;
    }

    public static Thread inThread(AbstractSystem... systems) {
        final List<SystemTask> tasks = prepare(systems);

        Thread thread = new Thread(new Runnable() {
            boolean running = true;

            @Override
            public void run() {
                tasks.forEach(t -> t.system.start());

                while (running) {
                    try {
                        for (SystemTask task : tasks) {
                            Thread.sleep(task.delay);
                            task.system.update();
                        }
                    } catch (InterruptedException ex) {
                        running = false;
                    }
                }
            }
        });
        thread.start();
        return thread;
    }

    public static CallbackRunner viaCallback(AbstractSystem... systems) {
        final List<SystemTask> tasks = prepare(systems);
        tasks.forEach(t -> t.system.start());

        return (CallbackRunner) () -> {
            for (SystemTask task : tasks) {
                Thread.sleep(task.delay);
                task.system.update();
            }
        };

    }

}
