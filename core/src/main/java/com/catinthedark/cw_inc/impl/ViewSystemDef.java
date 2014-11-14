package com.catinthedark.cw_inc.impl;

import com.catinthedark.cw_inc.lib.*;
import com.catinthedark.cw_inc.lib.view.ScreenManager;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystemDef extends AbstractSystemDef {
    public static ViewSystemDef instance() {
        Sys worker = new Sys();
        return new ViewSystemDef(worker);
    }

    private ViewSystemDef(Sys sys) {
        super(new Updater[]{new Updater(sys::render)}, 0);
        this.cameraUp = asyncPort(sys::cameraUp);
        this.onMenuEnter = serialPort(sys::menuEnter);
    }

    public final Port<Nothing> cameraUp;
    public final Port<Nothing> onMenuEnter;

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

        void menuEnter(long globalTime, Nothing ignored) {
            manager.goTo(1);
        }
    }
}