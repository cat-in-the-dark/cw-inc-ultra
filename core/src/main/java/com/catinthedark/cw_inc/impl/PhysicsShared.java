package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.SharedArray;
import com.catinthedark.cw_inc.lib.SharedPool;
import com.catinthedark.cw_inc.lib.SharedVal;

/**
 * Created by over on 03.12.14.
 */
public class PhysicsShared {

    static Vector2[] allocCableBlocks(){
        Vector2[] blocks = new Vector2[Constants.CABLE_SEGS];
        for(int i=0; i<blocks.length; i++)
            blocks[i] = new Vector2();
        return blocks;
    }
    private final SharedVal<Vector2> _pPos = new SharedVal<>(new Vector2(0,0));
    private final SharedVal<Vector2> _pVelocity = new SharedVal<>(new Vector2(0,0));
    private final SharedArray<Vector2> _cableDots = new SharedArray<>(allocCableBlocks());
    SharedPool<BotPhysicsData> _bots = new SharedPool<>(BotPhysicsData.class, 100);

    public final Reader reader = new Reader();
    public final Writer writer = new Writer();

    public class Reader{
        SharedVal.Reader<Vector2> pPos = _pPos::get;
        SharedVal.Reader<Vector2> pVelocity = _pVelocity::get;
        SharedArray<Vector2>.Reader cableDots = _cableDots.reader;
        SharedPool<BotPhysicsData>.Reader bots = _bots.reader;
    }

    public class Writer {
        SharedVal.Writer<Vector2> pPos = _pPos::update;
        SharedVal.Writer<Vector2> pVelocity = _pVelocity::update;
        SharedArray<Vector2>.Writer cableDots = _cableDots.writer;
        SharedPool<BotPhysicsData>.Writer bots = _bots.writer;
    }

}
