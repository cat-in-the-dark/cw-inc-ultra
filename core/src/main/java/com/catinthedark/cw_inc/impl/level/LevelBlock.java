package com.catinthedark.cw_inc.impl.level;

/**
 * Created by over on 22.11.14.
 */
public class LevelBlock {

    public final long id;
    public final int x;
    public final int y;
    public final BlockType type;

    public LevelBlock(long id, BlockType type, int x, int y) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
