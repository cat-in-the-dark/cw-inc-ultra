package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by over on 04.12.14.
 */
public class PlayerRender {




        private boolean blink = false;
        private int blinkCount = 0;
        private int wifiRayOffset = 0;
        private float stateTime = 0;


        public void render(RenderShared shared, SpriteBatch batch) {
            stateTime += shared.delay;
            //System.out.println("stateTime:" + stateTime);

            batch.setProjectionMatrix(shared.camera.combined);
            batch.begin();

            Vector2 playerPos = shared.playerPos;
            TextureRegion[][] frames;
            Animation jumpAnimation;
            Animation goAnimation;

            if (shared.playerDirX == DirectionX.RIGHT) {
                frames = Assets.textures.playerFrames;
                jumpAnimation = Assets.animations.playerJump;
                goAnimation = Assets.animations.playerGo;
            }
            else {
                frames = Assets.textures.playerFramesBack;
                jumpAnimation = Assets.animations.playerJumpBack;
                goAnimation = Assets.animations.playerGoBack;
            }

//            if (!player.isDamaged() || blink) {
                if (shared.animatePlayerMove) {
                    batch.draw(
                            goAnimation.getKeyFrame(stateTime),
                            (playerPos.x - Constants.PLAYER_HEIGHT / 2)
                                    * 32,
                            (playerPos.y - Constants.PLAYER_WIDTH / 2) * 32,
                            Constants.PLAYER_WIDTH * 32,
                            Constants.PLAYER_HEIGHT * 32);
                } else {

//                    if (player.isOnGround()) {

                        TextureRegion frame = null;
                        // если стоит
                        switch (shared.playerDirY) {
                            case UP:
                                frame = frames[0][13];
                                break;
                            case MIDDLE:
                                frame = frames[0][12];
                                break;
                            case DOWN:
                                frame = frames[0][14];
                                break;

                        }

                        batch.draw(
                                frame,
                                (playerPos.x - Constants.PLAYER_HEIGHT / 2)
                                        * 32,
                                (playerPos.y - Constants.PLAYER_WIDTH / 2)
                                        * 32,
                                Constants.PLAYER_WIDTH * 32,
                                Constants.PLAYER_HEIGHT * 32);
//                    } else {// летим
//                        batch.draw(
//                                jumpAnimation.getKeyFrame(player
//                                        .getStateTime()),
//                                (playerPos.x - Constants.PLAYER_HEIGHT / 2)
//                                        * conf.UNIT_SIZE,
//                                (playerPos.y - Constants.PLAYER_WIDTH / 2)
//                                        * conf.UNIT_SIZE,
//                                Constants.PLAYER_WIDTH * conf.UNIT_SIZE,
//                                Constants.PLAYER_HEIGHT * conf.UNIT_SIZE);
//                    }
                }
//            } else {
//                blinkCount++;
//            }
//            if (blinkCount >= 20) {
//                blinkCount = 0;
//                player.setDamaged(false);
//            }
//            blink = !blink;

            // draw shot
           if(shared.playerAttack != null){
               boolean res = shared.playerAttack.render(shared, batch);
               if(!res)
                   shared.playerAttack = null;
           }

            batch.end();
        }
    }

