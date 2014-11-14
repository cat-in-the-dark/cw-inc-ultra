package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.catinthedark.cw_inc.lib.*;

/**
 * Created by over on 11.11.14.
 */
public class InputSystemDef extends AbstractSystemDef {
    public static InputSystemDef instance() {
        return new InputSystemDef(new Sys());
    }

    private InputSystemDef(Sys sys) {
        super(new Updater[]{new Updater(sys::keyUpPoll)}, 100);
        this.worker = sys;
        this.keyEsc = asyncPort(worker::onEsc);
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