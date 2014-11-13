package com.catinthedark.cw_inc.lib.view;

import java.util.List;

/**
 * Created by over on 12.11.14.
 */
public abstract class Screen<T> {
    private final Layer<T>[] layers;

    public Screen(Layer<T>... layers) {
        this.layers = layers;
    }

    public void render(T shared) {
        for (Layer layer : layers)
            layer.render(shared);
    }

    /**
     * no post effects by default
     *
     * @param shared
     */
    public void postEffect(T shared) {

    }
}
