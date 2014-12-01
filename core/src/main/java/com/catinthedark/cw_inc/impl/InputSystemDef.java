package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.catinthedark.cw_inc.lib.*;

/**
 * Created by over on 11.11.14.
 */
public class InputSystemDef extends AbstractSystemDef {
    private final Sys sys = new Sys();
    public final Port<Nothing> keyEnter = asyncPort(sys::onEnter);
    public final Port<Nothing> menuEnter = serialPort(sys::menuEnter);
    public final Port<Nothing> onGameStart = serialPort(sys::onGameStart);
    public final Pipe<Nothing> onKeyW = new Pipe<>();
    public final Pipe<Nothing> onKeyA = new Pipe<>();
    public final Pipe<Nothing> onKeyEnter = new Pipe<>();

    {
        updater(sys.ifInState(GameState.IN_GAME, sys::moveKeysPoll));
        masterDelay = 20;
    }

    private class Sys {
        public LogicalFunction ifInState(GameState _state, LogicalFunction fn) {
            return (float delay) -> {
                if (state == _state)
                    fn.doLogic(delay);
            };
        }

        public GameState state = GameState.INIT;

        void moveKeysPoll(float delay) throws InterruptedException {
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                onKeyW.write(Nothing.NONE);
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                onKeyA.write(Nothing.NONE);
        }

        void onEnter(Nothing ignored) throws InterruptedException {
            onKeyEnter.write(Nothing.NONE);
        }

        void menuEnter(Nothing ignored) {
            state = GameState.MENU;
        }

        void onGameStart(Nothing ignored) {
            state = GameState.IN_GAME;
        }
    }
}