package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.SharedArray;
import com.catinthedark.cw_inc.lib.SharedPool;
import com.catinthedark.cw_inc.lib.SharedVal;

/**
 * Created by over on 03.12.14.
 * Be careful with this thing!
 * For every var MUST be 1 writer an N readers
 */
public class GameShared {

    static Vector2[] allocCableBlocks(){
        Vector2[] blocks = new Vector2[Constants.CABLE_SEGS];
        for(int i=0; i<blocks.length; i++)
            blocks[i] = new Vector2();
        return blocks;
    }
    public final SharedVal<Vector2> pPos = new SharedVal<>(new Vector2(0,0));
    public  final SharedVal<Vector2> pVelocity = new SharedVal<>(new Vector2(0,0));
    public final SharedVal<Direction> pDirection = new SharedVal<>(new Direction(DirectionX.RIGHT, DirectionY.MIDDLE));
    public  final SharedArray<Vector2> cableDots = new SharedArray<>(allocCableBlocks());
    public final SharedPool<BotPhysicsData> bots = new SharedPool<>(BotPhysicsData.class, 20);

}
