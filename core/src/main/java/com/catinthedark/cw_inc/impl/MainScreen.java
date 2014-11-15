package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.view.Layer;
import com.catinthedark.cw_inc.lib.view.Screen;

/**
 * Created by over on 12.11.14.
 */
public class MainScreen extends Screen<RenderShared> {

    public MainScreen() {
        super(new Layer<RenderShared>() {

            final SpriteBatch batch = new SpriteBatch();
            final Texture img = new Texture("badlogic.jpg");

            @Override
            public void render(RenderShared shared) {
                Gdx.gl.glClearColor(1, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                batch.setProjectionMatrix(shared.camera.combined);
                batch.begin();
                batch.draw(img, 0, 0);

                shared.entityPointers.forEach(p -> {
                    if (shared.entities == null)
                        throw new RuntimeException("shared is null??");

                    Vector2 pos = shared.entities.map(p);
                    batch.draw(Assets.textures.heartReg, pos.x, pos.y, 16, 16);
                });
                batch.end();
            }

        });
    }
}
