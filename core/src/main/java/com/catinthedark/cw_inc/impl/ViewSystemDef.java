package com.catinthedark.cw_inc.impl;

import com.catinthedark.cw_inc.lib.*;
import com.catinthedark.cw_inc.lib.view.ScreenManager;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystemDef extends AbstractSystemDef {
    public static ViewSystemDef instance() {
        Sys worker = new Sys();
        Port<Nothing> cameraUp = new QueuePort<>(worker::cameraUp);
        return new ViewSystemDef(worker, cameraUp);
    }

    private ViewSystemDef(Sys sys, Port<Nothing> cameraUp) {
        super(new Updater[]{new Updater(sys::render)}, new Port[]{cameraUp}, 0);
        this.cameraUp = cameraUp;
    }

    public final Port<Nothing> cameraUp;

    private static class Sys {
        final RenderShared shared = new RenderShared();
        final ScreenManager<RenderShared> manager = new ScreenManager<>(shared, new LogoScreen(), new MainScreen());

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
}