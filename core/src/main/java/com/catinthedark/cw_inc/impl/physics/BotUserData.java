package com.catinthedark.cw_inc.impl.physics;

/**
 * Created by over on 04.12.14.
 */
public class BotUserData {
    public final int id;
    public float health;
    public boolean killed = false;

    public BotUserData(int id, float health){
        this.id = id;
        this.health = health;
    }

}
