package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Port;
import com.catinthedark.cw_inc.lib.SharedMemory;
import com.catinthedark.cw_inc.lib.view.ScreenManager;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystemDef extends AbstractSystemDef {
    public ViewSystemDef(SharedMemory<Vector2>.Reader entites) {
        Sys sys = new Sys(entites);
        updater(sys::render);
        cameraUp = asyncPort(sys::cameraUp);
        onMenuEnter = serialPort(sys::menuEnter);
        newEntity = asyncPort(sys::newEntity);
    }

    public final Port<Nothing> cameraUp;
    public final Port<Nothing> onMenuEnter;
    public final Port<Integer> newEntity;

    private class Sys {
        public Sys(SharedMemory<Vector2>.Reader entites) {
            shared = new RenderShared();
            shared.camera.update();
            if (entites == null)
                throw new RuntimeException("entities is null?? wtf");
            shared.entities = entites;
            manager = new ScreenManager<>(shared, new LogoScreen(), new MainScreen());
        }

        final RenderShared shared;
        final ScreenManager<RenderShared> manager;

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

        void newEntity(long globalTime, int pointer) {
            System.out.println("Get entity with id:" + pointer);
            shared.entityPointers.add(pointer);
        }

    }
}