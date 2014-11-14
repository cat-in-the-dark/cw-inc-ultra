package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.catinthedark.cw_inc.lib.*;

/**
 * Created by over on 11.11.14.
 */
public class InputSystemDef extends AbstractSystemDef {
    private final Sys sys = new Sys();
    public final Port<Nothing> keyEsc = asyncPort(sys::onEsc);
    public final Port<Nothing> menuEnter = serialPort(sys::menuEnter);
    public final Pipe<Nothing> onKeyUp = new Pipe<>();

    {
        updater(sys.ifInState(GameState.MENU, sys::keyUpPoll));
        masterDelay = 100;
    }

    private class Sys {
        public LogicalFunction ifInState(GameState _state, LogicalFunction fn) {
            return (long globalTime, long delay) -> {
                if (state == _state)
                    fn.doLogic(globalTime, delay);
            };
        }

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