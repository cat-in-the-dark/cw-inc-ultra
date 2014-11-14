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
        super(new Updater[]{
                        new Updater(sys.ifInState(GameState.MENU, sys::keyUpPoll))},
                100);
        this.worker = sys;
        this.keyEsc = asyncPort(worker::onEsc);
        this.menuEnter = serialPort(worker::menuEnter);
    }

    private final Sys worker;
    public final Port<Nothing> keyEsc;
    public final Port<Nothing> menuEnter;

    public Pipe<Nothing> onKeyUp() {
        return worker.onKeyUp;
    }


    private static class Sys {
        public LogicalFunction ifInState(GameState _state, LogicalFunction fn) {
            return (long globalTime, long delay) -> {
                if (state == _state)
                    fn.doLogic(globalTime, delay);
            };
        }


        public final Pipe<Nothing> onKeyUp = new Pipe<>();
        public GameState state = GameState.INIT;


        void keyUpPoll(long globalTime, long delay) throws InterruptedException {
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                onKeyUp.write(Nothing.NONE);
        }

        void onEsc(long globalTime, Nothing ignored) throws InterruptedException {
            System.out.println("key ESC pressed");
            Thread.currentThread().interrupt();
        }

        void menuEnter(long globalTime, Nothing ignored) {
            state = GameState.MENU;
        }
    }
}