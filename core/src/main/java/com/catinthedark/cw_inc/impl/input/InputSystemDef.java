package com.catinthedark.cw_inc.impl.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.catinthedark.cw_inc.impl.common.DirectionX;
import com.catinthedark.cw_inc.impl.common.DirectionY;
import com.catinthedark.cw_inc.impl.common.GameShared;
import com.catinthedark.cw_inc.impl.common.GameState;
import com.catinthedark.cw_inc.lib.*;
import com.catinthedark.cw_inc.lib.common.Nothing;
import com.catinthedark.cw_inc.lib.io.Pipe;
import com.catinthedark.cw_inc.lib.io.Port;

import static com.catinthedark.cw_inc.lib.util.SysUtils.conditional;

/**
 * Created by over on 11.11.14.
 */
public class InputSystemDef extends AbstractSystemDef {
    private Sys sys;
    public final Port<Nothing> menuEnter;
    public final Pipe<Nothing> onGameStart = new Pipe<>();
    public final Pipe<Nothing> onKeyD = new Pipe<>();
    public final Pipe<Nothing> onKeyA = new Pipe<>();
    public final Pipe<Nothing> onKeySpace = new Pipe<>();
    public final Pipe<Nothing> onPlayerAttack = new Pipe<>();

    public InputSystemDef(GameShared gameShared) {
        sys = new Sys(gameShared);
        updater(conditional(() -> sys.state == GameState.IN_GAME, sys::moveKeysPoll));
        updater(conditional(() -> sys.state == GameState.IN_GAME, sys::spaceKeyPoll));
        menuEnter = serialPort(sys::menuEnter);
        masterDelay = 20;
    }

    private class Sys {
        boolean canAttack = true;
        final GameShared gameShared;

        Sys(GameShared gameShared) {
            this.gameShared = gameShared;
            Gdx.input.setInputProcessor(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {

                    if (keycode == Input.Keys.ENTER) {
                        if (state == GameState.MENU)
                            _startGame();
                        else if (sys.state == GameState.IN_GAME)
                            _playerAttack();
                        return true;
                    }
                    if (keycode == Input.Keys.W) {
                        gameShared.pDirection.update((d) -> d.dirY = DirectionY.UP);
                        return true;
                    }
                    if (keycode == Input.Keys.S) {
                        gameShared.pDirection.update((d) -> d.dirY = DirectionY.DOWN);
                        return true;
                    }

                    return false;
                }

                @Override
                public boolean keyUp(int keycode) {
                    if (keycode == Input.Keys.W || keycode == Input.Keys.S) {
                        gameShared.pDirection.update((d) -> d.dirY = DirectionY.MIDDLE);
                        return true;
                    }

                    return false;
                }
            });
        }

        GameState state;

        void _startGame() {
            state = GameState.IN_GAME;
            gameShared.pDirection.update((d) -> d.dirX = DirectionX.RIGHT);
            onGameStart.write(Nothing.NONE);
        }

        void _playerAttack() {
            if (canAttack) {
                onPlayerAttack.write(Nothing.NONE);
                canAttack = false;
                defer(() -> canAttack = true, 1500);
            }
        }


        void moveKeysPoll(float delay) throws InterruptedException {
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                onKeyD.write(Nothing.NONE);
                gameShared.pDirection.update((d) -> d.dirX = DirectionX.RIGHT);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                onKeyA.write(Nothing.NONE);
                gameShared.pDirection.update((d) -> d.dirX = DirectionX.LEFT);
            }
        }


        void spaceKeyPoll(float delay) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                onKeySpace.write(Nothing.NONE);
        }

        void menuEnter(Nothing ignored) {
            state = GameState.MENU;
        }

    }
}