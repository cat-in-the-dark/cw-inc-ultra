package com.catinthedark.cw_inc.impl;

import com.catinthedark.cw_inc.lib.*;
import com.catinthedark.cw_inc.lib.view.ScreenManager;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystemDef extends AbstractSystemDef {
    private final Sys sys = new Sys();
    public final Port<Nothing> cameraUp = asyncPort(sys::cameraUp);
    public final Port<Nothing> onMenuEnter = serialPort(sys::menuEnter);

    {
        updater(sys::render);
    }

    private class Sys {
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

        void menuEnter(long globalTime, Nothing ignored) {
            manager.goTo(1);
        }
    }
}