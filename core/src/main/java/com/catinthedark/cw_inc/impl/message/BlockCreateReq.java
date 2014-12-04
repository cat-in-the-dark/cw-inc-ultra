package com.catinthedark.cw_inc.impl.message;

import com.catinthedark.cw_inc.impl.level.BlockType;

/**
 * Created by over on 02.12.14.
 */
public class BlockCreateReq {
    public final BlockType type;
    public final float x;
    public final float y;
    public final long id;

    public BlockCreateReq(long id, BlockType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.id = id;
    }
}
