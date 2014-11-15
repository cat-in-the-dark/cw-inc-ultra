package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.view.Layer;
import com.catinthedark.cw_inc.lib.view.Screen;

/**
 * Created by over on 15.11.14.
 */
public class GameScreen extends Screen<RenderShared> {
    public GameScreen() {
        super(new Layer<RenderShared>() {
            final SpriteBatch batch = new SpriteBatch();

            @Override
            public void render(RenderShared shared) {
                batch.setProjectionMatrix(shared.camera.combined);
                batch.begin();
                shared.entityPointers.forEach(p -> {
                    Vector2 pos = shared.entities.map(p);
                    batch.draw(Assets.textures.heartReg, pos.x - 512, pos.y - 320, 16, 16);
                });
                batch.end();
            }
        });
    }

    @Override
    public void beforeShow() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
