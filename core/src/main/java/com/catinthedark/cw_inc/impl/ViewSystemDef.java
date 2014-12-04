package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.catinthedark.cw_inc.impl.level.LevelMatrix;
import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Port;
import com.catinthedark.cw_inc.lib.SharedMemory;
import com.catinthedark.cw_inc.lib.view.Renderable;
import com.catinthedark.cw_inc.lib.view.ScreenManager;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystemDef extends AbstractSystemDef {
    public ViewSystemDef(PhysicsShared.Reader pShared, SharedMemory<Vector2>.Reader bots, LevelMatrix.View levelView) {
        Sys sys = new Sys(pShared, bots, levelView);
        updater(sys::update);
        onMenuEnter = serialPort(sys::menuEnter);
        onGameStart = serialPort(sys::onGameStart);
        newBot = asyncPort(sys::newBot);
        playerDirX = asyncPort(sys::playerDirX);
        playerDirY = asyncPort(sys::playerDirY);
        playerAttack = asyncPort(sys::playerAttack);
    }

    public final Port<Nothing> onMenuEnter;
    public final Port<Nothing> onGameStart;
    public final Port<Integer> newBot;
    public final Port<DirectionX> playerDirX;
    public final Port<DirectionY> playerDirY;
    public final Port<Nothing> playerAttack;

    private class Sys {
        public Sys(PhysicsShared.Reader pShared, SharedMemory<Vector2>.Reader bots, LevelMatrix.View levelView) {
            shared = new RenderShared();
            shared.camera.update();
            if (bots == null)
                throw new RuntimeException("bots is null?? wtf");
            shared.bots = bots;
            shared.pShared = pShared;
            shared.levelView = levelView;
            manager = new ScreenManager<>(shared, new LogoScreen(), new MenuScreen(), new
                    GameScreen());
        }

        final RenderShared shared;
        final ScreenManager<RenderShared> manager;


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

        void newBot(int pointer) {
            System.out.println("Get entity with id:" + pointer);
            shared.botsPointers.add(pointer);
        }


        void _render() {
            manager.render(shared);
        }

        void _cameraMove() {
            Vector2 ppos = shared.playerPos;
            Vector3 camPos = shared.camera.position;
            Vector3 backPos = shared.backgroundCamera.position;
            float distance = ppos.x * 32 - camPos.x;

            if (distance > 128) {
                float dx = ppos.x * 32 - camPos.x;
                shared.camera.position.set(camPos.x + 5, camPos.y, camPos.z);

                backPos.x += 5.0f / 32 / 2;
                shared.backgroundCamera.position.set(backPos);
                shared.camera.update();
                shared.backgroundCamera.update();
            }
        }

        void _pollPlayerAnimation() {
            if (Gdx.input.isKeyPressed(Input.Keys.A) ||
                    Gdx.input.isKeyPressed(Input.Keys.D))
                shared.animatePlayerMove = true;
            else
                shared.animatePlayerMove = false;
        }

        void update(float delay) {
            shared.playerPos = shared.pShared.pPos.get().cpy();
            shared.delay = delay;
            _pollPlayerAnimation();
            _render();
            _cameraMove();
        }

        void playerDirX(DirectionX dirX) {
            shared.playerDirX = dirX;
        }
        void playerDirY(DirectionY dirY) {
            shared.playerDirY = dirY;
        }
        void playerAttack(Nothing ignored){
            shared.playerAttack = new Renderable<RenderShared>() {
                int wifiRayOffset;
                float stateTime = 0;

                @Override
                public boolean render(RenderShared shared, SpriteBatch batch) {
                    stateTime += shared.delay;

                    wifiRayOffset += 4;
                    if (wifiRayOffset > Assets.textures.shot.getWidth() / 2)
                        wifiRayOffset = 0;

                    Vector2 playerPos = shared.playerPos;

                    if (shared.playerDirX == DirectionX.RIGHT) {
                        switch (shared.playerDirY) {
                            case UP:
                                batch.draw(Assets.textures.shot,
                                        (playerPos.x + Constants.PLAYER_HEIGHT / 2)
                                                * 32 + 3,
                                        (playerPos.y - Constants.PLAYER_WIDTH / 2)
                                                * 32 + 38,
                                        0, 0, 256, 32, 1, 1, 45,
                                        256 - wifiRayOffset,
                                        0, 256, 32, false, false);
                                break;
                            case MIDDLE:
                                batch.draw(
                                        Assets.textures.shot,
                                        (playerPos.x + Constants.PLAYER_HEIGHT / 2)
                                                * 32,
                                        (playerPos.y - Constants.PLAYER_WIDTH / 2)
                                                * 32 + 20,
                                        0, 0, 256, 32, 1, 1, 0,
                                        256 - wifiRayOffset,
                                        0, 256, 32, false, false);
                                break;
                            case DOWN:
                                batch.draw(Assets.textures.shot,
                                        (playerPos.x + Constants.PLAYER_HEIGHT / 2)
                                                * 32 - 15,
                                        (playerPos.y - Constants.PLAYER_WIDTH / 2)
                                                * 32 + 5,
                                        0, 0, 256, 32, 1, 1, -45,
                                        256 - wifiRayOffset,
                                        0, 256, 32, false, false);
                                break;
                        }
                    } else {
                        switch (shared.playerDirY) {
                            case UP:
                                batch.draw(Assets.textures.shot,
                                        (playerPos.x - Constants.PLAYER_HEIGHT / 2)
                                                * 32 - 5,
                                        (playerPos.y - Constants.PLAYER_WIDTH / 2)
                                                * 32 + 42,
                                        0, 0, -256, 32, 1, 1, -45,
                                        256 - wifiRayOffset,
                                        0, 256, 32, false, false);
                                break;
                            case MIDDLE:
                                batch.draw(
                                        Assets.textures.shot,
                                        (playerPos.x - Constants.PLAYER_HEIGHT / 2)
                                                * 32,
                                        (playerPos.y - Constants.PLAYER_WIDTH / 2)
                                                * 32 + 20,
                                        0, 0, -256, 32, 1, 1, 0,
                                        256 - wifiRayOffset,
                                        0, 256, 32, false, false);
                                break;
                            case DOWN:
                                batch.draw(Assets.textures.shot,
                                        (playerPos.x - Constants.PLAYER_HEIGHT / 2)
                                                * 32 + 13,
                                        (playerPos.y - Constants.PLAYER_WIDTH / 2)
                                                * 32 + 3,
                                        0, 0, -256, 32, 1, 1, 45,
                                        256 - wifiRayOffset, 0, 256, 32, false, false);
                                break;
                        }
                    }
                    return stateTime < 1.3f;
                }
            };
        }
    }
}