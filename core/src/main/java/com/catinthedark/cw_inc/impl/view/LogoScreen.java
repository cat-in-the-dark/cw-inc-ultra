package com.catinthedark.cw_inc.impl.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.catinthedark.cw_inc.impl.common.Assets;
import com.catinthedark.cw_inc.lib.view.Layer;
import com.catinthedark.cw_inc.lib.view.Screen;

/**
 * Created by over on 14.11.14.
 */
public class LogoScreen extends Screen<RenderShared> {
    public LogoScreen() {
        super(1000, new Layer<RenderShared>() {
            private final SpriteBatch batch = new SpriteBatch();

            @Override
            public void render(RenderShared shared) {
                batch.setProjectionMatrix(shared.camera.combined);
                Gdx.gl.glClearColor(1, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                batch.setProjectionMatrix(shared.camera.combined);
                batch.begin();
                batch.draw(Assets.textures.logoTex, -512, -320);
                batch.end();
            }
        });
    }
}
