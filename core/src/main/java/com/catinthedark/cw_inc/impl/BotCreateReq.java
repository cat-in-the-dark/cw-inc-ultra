package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by over on 04.12.14.
 */
public class BotCreateReq {
    public final long id;
    public final Vector2 pos;

    public BotCreateReq(long id, Vector2 pos) {
        this.id = id;
        this.pos = pos;
    }
}
