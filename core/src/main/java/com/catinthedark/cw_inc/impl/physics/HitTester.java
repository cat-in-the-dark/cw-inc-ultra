package com.catinthedark.cw_inc.impl.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.catinthedark.cw_inc.lib.VoidFunction;
import static com.catinthedark.cw_inc.lib.ContactUtils.query;

/**
 * Created by over on 04.12.14.
 */
public class HitTester implements ContactListener {





    @Override
    public void beginContact(Contact contact) {
        if(query(BlockUserData.class, PlayerUserData.class, contact) != null)
            onPlayerOnGround.apply(true);

    }

    @Override
    public void endContact(Contact contact) {
        if(query(BlockUserData.class, PlayerUserData.class, contact) != null)
            onPlayerOnGround.apply(false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    final VoidFunction onPlayerOnGround;

    public HitTester(VoidFunction<Boolean> onPlayerOnGround){
        this.onPlayerOnGround = onPlayerOnGround;
    }
}
