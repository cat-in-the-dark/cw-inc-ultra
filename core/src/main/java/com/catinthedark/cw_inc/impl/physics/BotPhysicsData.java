package com.catinthedark.cw_inc.impl.physics;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by over on 04.12.14.
 */
public class BotPhysicsData {
    public final Vector2 pos;
    public final Vector2 velocity;

    public BotPhysicsData(Vector2 pos, Vector2 velocity) {
        this.pos = pos;
        this.velocity = velocity;
    }
}
