package com.catinthedark.cw_inc.impl.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.impl.common.Assets;
import com.catinthedark.cw_inc.impl.common.Constants;
import com.catinthedark.cw_inc.impl.level.LevelBlock;
import com.catinthedark.cw_inc.impl.physics.BotPhysicsData;
import com.catinthedark.cw_inc.lib.view.Layer;
import com.catinthedark.cw_inc.lib.view.Screen;

import java.util.stream.IntStream;

/**
 * Created by over on 15.11.14.
 */
public class GameScreen extends Screen<RenderShared> {
    final FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 640, false);
    final SpriteBatch result = new SpriteBatch();
    final ShaderProgram scanline = new ShaderProgram(Gdx.files.internal("edge.vert").readString(),
            Gdx.files.internal("edge.frag").readString());

    public GameScreen() {
        super(new Layer<RenderShared>() {
                  @Override
                  public void render(RenderShared shared) {
                      Assets.textures.backgroundFar.setView(shared.backgroundCamera);
                      Assets.textures.backgroundFar.render(new int[]{0});
                  }
              }, new Layer<RenderShared>() {
                  final SpriteBatch batch = new SpriteBatch();
                  final ShaderProgram grayscale = new ShaderProgram(Gdx.files.internal("first.vert").readString(),
                          Gdx.files.internal("first.frag").readString());

                  @Override
                  public void render(RenderShared shared) {
                      batch.setProjectionMatrix(shared.camera.combined);
                      batch.setShader(grayscale);
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
                                          //must be filtered in LevelSystem
                                          break;
                                  }
                                  batch.draw(tex, block.x - Constants.BLOCK_WIDTH * 32 / 2, block.y - Constants.BLOCK_HEIGHT * 32 / 2);
                              }
                          }
                      });
                      batch.end();
                  }
              }, new Layer<RenderShared>() {
                  final SpriteBatch batch = new SpriteBatch();
                  final ShapeRenderer shapeRenderer = new ShapeRenderer();
                  final PlayerRender pRender = new PlayerRender();

                  {
                      shapeRenderer.setColor(0.9f, 0.65f, 0.18f, 1.0f);
                  }

                  @Override
                  public void render(RenderShared shared) {
                      batch.setProjectionMatrix(shared.camera.combined);
                      batch.begin();

                      shared.botsPointers.forEach(p -> {
                          BotPhysicsData data = shared.gShared.bots.map(p);
                          Vector2 pos = data.pos;
                          TextureRegion botTex;
                          //System.out.print(data.velocity.x);
                          if(data.velocity.x >= 0)
                              botTex = Assets.textures.mushroomedCrabFramesLeft[0][0];
                          else
                              botTex = Assets.textures.mushroomedCrabFramesRight[0][0];

                          batch.draw(botTex,
                                  (pos.x - Constants.CRAB_WIDTH / 2) * 32,
                                  (pos.y - Constants.CRAB_HEIGHT / 2) * 32, 64, 64);
                      });
//                      Vector2 pPos = shared.playerPos;
//                      TextureRegion pTex;
//                      if (shared.playerDirX == DirectionX.LEFT)
//                          pTex = Assets.textures.playerFramesBack[0][0];
//                      else
//                          pTex = Assets.textures.playerFrames[0][0];
//
//                      batch.draw(pTex, (pPos.x - Constants.PLAYER_WIDTH / 2) * 32, (pPos.y - Constants.PLAYER_HEIGHT / 2) * 32);
//                      batch.end();
                      pRender.render(shared, batch);

                      batch.end();

                      //draw cable
                      Vector2[] dataset = shared.gShared.cableDots.stream()
                              .map(vec -> vec.cpy().scl(32))
                              .toArray(Vector2[]::new);

                      CatmullRomSpline<Vector2> romSpline = new CatmullRomSpline<>(dataset, false);

                      Vector2[] dots = IntStream.rangeClosed(0, Constants.CABLE_STEPS - 1)
                              .boxed()
                              .map(i -> {
                                  Vector2 vec = new Vector2();
                                  romSpline.valueAt(vec, ((float) i) / ((float) Constants.CABLE_STEPS - 1));
                                  return vec;
                              })
                              .toArray(Vector2[]::new);

                      Gdx.gl20.glLineWidth(Constants.CABLE_THICK);
                      shapeRenderer.setProjectionMatrix(shared.camera.combined);
                      shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                      for (int i = 0; i < Constants.CABLE_STEPS - 1; ++i)
                          shapeRenderer.line(dots[i], dots[i + 1]);
                      shapeRenderer.end();


                  }
              }

        );
    }

    @Override
    public void beforeShow() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void beforeRender() {
        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void postEffect(RenderShared shared) {
        fbo.end();
//        System.out.print(scanline.getLog());
//        for(String uniform : scanline.getUniforms()){
//            System.out.println(uniform);
//        }
        scanline.begin();
//        scanline.setUniformf("rt_w", 1024);
//        scanline.setUniformf("rt_h", 640);
        scanline.end();
        TextureRegion reg = new TextureRegion(fbo.getColorBufferTexture());
        reg.flip(false, true);
        result.setShader(scanline);
        result.begin();
        result.draw(reg,0,0);
        result.end();
    }
}
