package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by over on 15.11.14.
 */
public class PhysicsSystemDef extends AbstractSystemDef {
    public PhysicsSystemDef(SharedMemory<Vector2>.Writer entities) {
        masterDelay = 100;
        entityCreated = new Pipe<>();

        Sys sys = new Sys(entities);
        updater(sys::update);
        menuEnter = serialPort(sys::onMenuEnter);
    }

    public final Pipe<Integer> entityCreated;
    public final Port<Nothing> menuEnter;

    private class Sys {
        public Sys(SharedMemory<Vector2>.Writer entities) {
            this.entities = entities;
        }

        final List<Integer> pointers = new ArrayList<>();
        final Random rand = new Random(System.nanoTime());
        GameState state = GameState.INIT;
        final SharedMemory<Vector2>.Writer entities;

        void update(long globalTime, long delay) {
            pointers.forEach(p -> entities.map(p).set(rand.nextInt(1024), rand.nextInt(640)));
        }

        void onMenuEnter(long globalTime, Nothing ignored) throws InterruptedException {
            System.out.println("Physics: on menu enter");
            for (int i = 0; i < 10; i++) {
                int pointer = entities.alloc(new Vector2(rand.nextInt(1024), rand.nextInt(640)));
                pointers.add(pointer);
                entityCreated.write(pointer);
            }
            state = GameState.MENU;
        }
    }
}