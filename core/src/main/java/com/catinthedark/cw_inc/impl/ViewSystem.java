package com.catinthedark.cw_inc.impl;

import com.catinthedark.cw_inc.lib.*;
import com.catinthedark.cw_inc.lib.view.ScreenManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystem implements AbstractSystem {
    private final List<Updater> updaters = Arrays.asList(new Updater(this::render));

    public final Port<Nothing> cameraUp = new QueuePort<>(this::cameraUp);
    private final List<Port> ports = Arrays.asList(cameraUp);

    @Override
    public List<Updater> getUpdaters() {
        return updaters;
    }

    @Override
    public List<Port> getPorts() {
        return ports;
    }

    final RenderShared shared = new RenderShared();
    final ScreenManager<RenderShared> manager = new ScreenManager<>(shared, new MainScreen());

    {
        shared.camera.update();
    }

    private void cameraUp(long globalTime, Nothing ignored) {
        shared.camera.position.y += 10;
        shared.camera.update();
    }

    void render(long globalTime, long delay) {
        manager.render(shared);
    }
}
