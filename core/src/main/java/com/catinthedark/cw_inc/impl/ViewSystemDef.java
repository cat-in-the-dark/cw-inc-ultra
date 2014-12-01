package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.catinthedark.cw_inc.impl.level.LevelMatrix;
import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Port;
import com.catinthedark.cw_inc.lib.SharedMemory;
import com.catinthedark.cw_inc.lib.view.ScreenManager;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystemDef extends AbstractSystemDef {
    public ViewSystemDef(SharedMemory<Vector2>.Reader entites, LevelMatrix.View levelView) {
        Sys sys = new Sys(entites, levelView);
        updater(sys::threadLocal);
        updater(sys::cameraMove);
        updater(sys::render);
        onMenuEnter = serialPort(sys::menuEnter);
        onGameStart = serialPort(sys::onGameStart);
        newEntity = asyncPort(sys::newEntity);
        playerCreated = asyncPort(sys::playerCreated);
    }

    public final Port<Nothing> onMenuEnter;
    public final Port<Nothing> onGameStart;
    public final Port<Integer> newEntity;
    public final Port<Integer> playerCreated;

    private class Sys {
        public Sys(SharedMemory<Vector2>.Reader entites, LevelMatrix.View levelView) {
            shared = new RenderShared();
            shared.camera.update();
            if (entites == null)
                throw new RuntimeException("entities is null?? wtf");
            shared.entities = entites;
            shared.levelView = levelView;
            manager = new ScreenManager<>(shared, new LogoScreen(), new MenuScreen(), new
                    GameScreen());
        }

        final RenderShared shared;
        final ScreenManager<RenderShared> manager;


        void render(float delay) {
            manager.render(shared);
        }

        void menuEnter(Nothing ignored) {
            manager.goTo(1);
        }

        void onGameStart(Nothing ignored) {
            shared.camera.position.set(512, 320, 0);
            shared.backgroundCamera.position.set(16, 10, 0);
            shared.camera.update();
            shared.backgroundCamera.update();
            manager.goTo(2);
        }

        void newEntity(int pointer) {
            System.out.println("Get entity with id:" + pointer);
            shared.entityPointers.add(pointer);
        }

        void threadLocal(float delay) {
            if (shared.playerPointer != null)
                shared.playerPos = shared.entities.map(shared.playerPointer).cpy();
        }


        void playerCreated(int pointer) {
            shared.playerPointer = pointer;
        }

        void cameraMove(float delay) {
            if (shared.playerPointer == null)
                return;

            Vector2 ppos = shared.playerPos;
            Vector3 camPos = shared.camera.position;
            Vector3 backPos = shared.backgroundCamera.position;
            float distance = ppos.x - camPos.x;

            if (distance > 128) {
                float dx = ppos.x - camPos.x;
                shared.camera.position.set(camPos.x + 5, camPos.y, camPos.z);

                backPos.x += 5.0f / 32 / 2;
                shared.backgroundCamera.position.set(backPos);
                shared.camera.update();
                shared.backgroundCamera.update();
            }
        }
    }
}