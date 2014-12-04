package com.catinthedark.cw_inc.impl.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.catinthedark.cw_inc.lib.Launcher;
import com.catinthedark.cw_inc.lib.VoidFunction;

/**
 * Created by over on 04.12.14.
 */
public class HitTester implements ContactListener {

    static class Tuple<A,B>{
        final A a;
        final B b;

        Tuple(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    static <A,B> Tuple<A,B>  query(Class<A> data1Clazz, Class<B> data2Clazz, Contact contact){
        if(contact.getFixtureA().getUserData() == null ||
                contact.getFixtureB().getUserData() == null)
            return null;

//        System.out.println(String.format("c1:%s c2:%s", contact.getFixtureA().getUserData().getClass().toString(),
//                contact.getFixtureB().getUserData().getClass().toString()));

        if(contact.getFixtureA().getUserData().getClass() == data1Clazz) {
            A a = (A) contact.getFixtureA().getUserData();
            if(contact.getFixtureB().getUserData().getClass() == data2Clazz) {
                B b = (B) contact.getFixtureB().getUserData();
                return new Tuple<>(a, b);
            }
        }

        if(contact.getFixtureA().getUserData().getClass() == data2Clazz) {
            B b = (B) contact.getFixtureA().getUserData();
            if(contact.getFixtureB().getUserData().getClass() == data1Clazz) {
                A a = (A) contact.getFixtureB().getUserData();
                return new Tuple<>(a, b);
            }
        }

        return null;
    }

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
