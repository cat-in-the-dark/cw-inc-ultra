package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.catinthedark.cw_inc.lib.*;

/**
 * Created by over on 11.11.14.
 */
public class InputSystemDef extends AbstractSystemDef {
    public static InputSystemDef instance() {
        Sys worker = new Sys();
        Port<Nothing> onEsc = new QueuePort<>(worker::onEsc, true);
        return new InputSystemDef(worker, onEsc);
    }

    private InputSystemDef(Sys sys, Port<Nothing> keyEsc) {
        super(new Updater[]{new Updater(sys::keyUpPoll)}, new Port[]{new QueuePort<>(sys::onEsc, true)}, 100);
        this.worker = sys;
        this.keyEsc = keyEsc;
    }

    private final Sys worker;
    public final Port<Nothing> keyEsc;

    public Pipe<Nothing> onKeyUp() {
        return worker.onKeyUp;
    }

    private static class Sys {
        public final Pipe<Nothing> onKeyUp = new Pipe<>();

        void keyUpPoll(long globalTime, long delay) throws InterruptedException {
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                onKeyUp.write(Nothing.NONE);
        }

        void onEsc(long globalTime, Nothing ignored) throws InterruptedException {
            System.out.println("key ESC pressed");
            Thread.currentThread().interrupt();
        }
    }
}
