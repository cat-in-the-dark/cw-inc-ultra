package com.catinthedark.cw_inc.lib.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by over on 04.12.14.
 */
public interface Renderable<T> {
    public boolean render(T shared, SpriteBatch batch);
}
