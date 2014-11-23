package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.impl.level.LevelBlock;
import com.catinthedark.cw_inc.lib.view.Layer;
import com.catinthedark.cw_inc.lib.view.Screen;

/**
 * Created by over on 15.11.14.
 */
public class GameScreen extends Screen<RenderShared> {
    public GameScreen() {
        super(new Layer<RenderShared>() {
                  @Override
                  public void render(RenderShared shared) {
                      Assets.textures.backgroundFar.setView(shared.backgroundCamera);
                      Assets.textures.backgroundFar.render(new int[]{0});
                  }
              }, new Layer<RenderShared>() {
                  final SpriteBatch batch = new SpriteBatch();

                  @Override
                  public void render(RenderShared shared) {
                      batch.setProjectionMatrix(shared.camera.combined);
                      batch.begin();
                      shared.levelView.forEach(row -> {
                          for (LevelBlock block : row) {
                              if (block != null) {
                                  TextureRegion tex = null;
                                  switch (block.type) {

                                      case UNDERGROUND:
                                          tex = Assets.textures.underground;
                                          break;
                                      case GRASS:
                                          tex = Assets.textures.grass;
                                          break;
                                      case GRASS_SHADOW:
                                          tex = Assets.textures.grass_shadow;
                                          break;
                                      case GRASS_SLOPE_LEFT:
                                          tex = Assets.textures.grass_slope_slope;
                                          break;
                                      case GRASS_SLOPE_LEFT_SHADOW:
                                          tex = Assets.textures.grass_slope_left_shadow;
                                          break;
                                      case GRASS_SLOPE_RIGHT:
                                          tex = Assets.textures.grass_slope_right;
                                          break;
                                      case GRASS_SLOPE_RIGHT_SHADOW:
                                          tex = Assets.textures.grass_slope_right_shadow;
                                          break;
                                      case EMPTY:
                                          break;
                                  }
                                  batch.draw(tex, block.x, block.y);
                              }
                          }
                      });
                      batch.end();
                  }
              }, new Layer<RenderShared>() {
                  final SpriteBatch batch = new SpriteBatch();

                  @Override
                  public void render(RenderShared shared) {
                      batch.setProjectionMatrix(shared.camera.combined);
                      batch.begin();
//                shared.entityPointers.forEach(p -> {
//                    Vector2 pos = shared.entities.map(p);
//                    batch.draw(Assets.textures.heartReg, pos.x - 512, pos.y - 320, 16, 16);
//                });
                      if (shared.playerPos != null) {
                          Vector2 pPos = shared.playerPos;
                          batch.draw(Assets.textures.playerFrames[0][0], pPos.x, pPos.y);
                      }
                      batch.end();
                  }
              }

        );
    }

    @Override
    public void beforeShow() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void beforeRender() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
