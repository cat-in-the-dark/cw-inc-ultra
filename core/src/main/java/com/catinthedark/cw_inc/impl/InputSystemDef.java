package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.catinthedark.cw_inc.lib.*;

/**
 * Created by over on 11.11.14.
 */
public class InputSystemDef extends AbstractSystemDef {
    private final Sys sys = new Sys();
    public final Port<Nothing> menuEnter = serialPort(sys::menuEnter);
    public final Pipe<Nothing> onGameStart = new Pipe<>();
    public final Pipe<Nothing> onKeyD = new Pipe<>();
    public final Pipe<Nothing> onKeyA = new Pipe<>();
    public final Pipe<Nothing> onKeySpace = new Pipe<>();
    public final Pipe<Nothing> onPlayerAttack = new Pipe<>();
    public final Pipe<DirectionX> playerDirXSet = new Pipe<>();
    public final Pipe<DirectionY> playerDirYSet = new Pipe<>();

    {
        updater(sys.ifInState(GameState.IN_GAME, sys::moveKeysPoll));
        updater(sys.ifInState(GameState.IN_GAME, sys::spaceKeyPoll));
        masterDelay = 20;
    }

    private class Sys {
        public LogicalFunction ifInState(GameState _state, LogicalFunction fn) {
            return (float delay) -> {
                if (state == _state)
                    fn.doLogic(delay);
            };
        }

        boolean canAttack = true;

        Sys(){
            Gdx.input.setInputProcessor(new InputAdapter() {
                boolean handleKeyDown(int keycode){
                    try {
                        if (keycode == Input.Keys.ENTER) {
                                if(state == GameState.MENU)
                                    _startGame();
                                else if(sys.state == GameState.IN_GAME)
                                    _playerAttack();
                            return true;
                        }
                        if(keycode == Input.Keys.W){
                            playerDirYSet.write(DirectionY.UP);
                            return true;
                        }
                        if(keycode == Input.Keys.S){
                            playerDirYSet.write(DirectionY.DOWN);
                            return true;
                        }
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }

                    return false;
                }
                boolean handleKeyUp(int keycode){
                    try {
                        if(keycode == Input.Keys.W || keycode == Input.Keys.S){
                            playerDirYSet.write(DirectionY.MIDDLE);
                            return true;
                        }
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }

                    return false;
                }

                @Override
                public boolean keyDown(int keycode) {
                   return handleKeyDown(keycode);
                }

                @Override
                public boolean keyUp(int keycode) {
                    return handleKeyUp(keycode);
                }
            });
        }

        GameState state;
        DirectionX playerDirX;

        void _startGame() throws InterruptedException{
            state = GameState.IN_GAME;
            playerDirX = DirectionX.LEFT;
            onGameStart.write(Nothing.NONE);
        }

        void _playerAttack() throws InterruptedException{
            if(canAttack) {
                onPlayerAttack.write(Nothing.NONE);
                canAttack = false;
                defer(() -> canAttack = true, 1500);
            }
        }


        void moveKeysPoll(float delay) throws InterruptedException {
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                onKeyD.write(Nothing.NONE);
                if(playerDirX != DirectionX.RIGHT){
                    playerDirX = DirectionX.RIGHT;
                    playerDirXSet.write(DirectionX.RIGHT);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)){
                onKeyA.write(Nothing.NONE);
                if(playerDirX != DirectionX.LEFT){
                    playerDirX = DirectionX.LEFT;
                    playerDirXSet.write(DirectionX.LEFT);
                }
            }
        }


        void spaceKeyPoll(float delay) throws InterruptedException {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
                onKeySpace.write(Nothing.NONE);
        }

        void menuEnter(Nothing ignored) {
            state = GameState.MENU;
        }

    }
}