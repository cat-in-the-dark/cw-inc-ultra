package com.catinthedark.cw_inc.impl.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.function.Consumer;

import static com.catinthedark.cw_inc.lib.util.ContactUtils.query;

/**
 * Created by over on 04.12.14.
 */
public class HitTester implements ContactListener {





    @Override
    public void beginContact(Contact contact) {
        if(query(BlockUserData.class, PlayerUserData.class, contact) != null)
            onPlayerOnGround.accept(true);

    }

    @Override
    public void endContact(Contact contact) {
        if(query(BlockUserData.class, PlayerUserData.class, contact) != null)
            onPlayerOnGround.accept(false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    final Consumer<Boolean> onPlayerOnGround;

    public HitTester(Consumer<Boolean> onPlayerOnGround){
        this.onPlayerOnGround = onPlayerOnGround;
    }
}
