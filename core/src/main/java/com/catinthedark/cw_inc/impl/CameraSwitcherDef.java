package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.*;

/**
 * Created by over on 15.11.14.
 */
public class CameraSwitcherDef extends AbstractSystemDef {
    CameraSwitcherDef(SharedMemory<Vector2>.Reader entities) {
        sys = new Sys(entities);
        onGameStart = serialPort(sys::onGameStart);
        onPlayerCreated = asyncPort(sys::onPlayerCreated);
        updater(sys::pollPlayerPos);
    }

    private final Sys sys;
    public final Port<Nothing> onGameStart;
    public final Port<Integer> onPlayerCreated;
    public final Pipe<Vector2> cameraMoveTo = new Pipe<>();

    private class Sys {
        Sys(SharedMemory<Vector2>.Reader entities) {
            this.entities = entities;
        }

        private final SharedMemory<Vector2>.Reader entities;
        private Vector2 cameraPos = new Vector2(0, 0);
        private Integer playerPointer = null;

        void onGameStart(long globalTime, Nothing ignored) {
            cameraPos.set(0, 0);
        }

        void onPlayerCreated(long globalTime, Integer playerPointer) {
            cameraPos.set(512, 320);
            this.playerPointer = playerPointer;
        }

        void pollPlayerPos(long globalTime, long delay) throws InterruptedException {
            if (playerPointer != null) {
                if (entities.map(playerPointer).x > cameraPos.x) {
                    cameraPos.x = entities.map(playerPointer).x;
                    cameraMoveTo.write(cameraPos);
                }
            }
        }

    }
}
