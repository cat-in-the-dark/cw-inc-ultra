package com.catinthedark.cw_inc.impl.level;

import java.util.stream.Stream;

/**
 * Created by over on 23.11.14.
 */
public enum BlockType {

    UNDERGROUND(0, true), GRASS(1, true), GRASS_SHADOW(2, false), GRASS_SLOPE_LEFT(3, true),
    GRASS_SLOPE_LEFT_SHADOW(4, false), GRASS_SLOPE_RIGHT(5, true), GRASS_SLOPE_RIGHT_SHADOW(6,
        true),
    EMPTY(7, false);

    public boolean collidable;
    public int id;

    BlockType(int id, boolean collidable) {
        this.id = id;
        this.collidable = collidable;
    }

    public LevelBlock at(int x, int y) {
        return new LevelBlock(this, x, y);
    }

    public static BlockType byId(int id) {
        return Stream.of(values())
            .filter(type -> type.id == id)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not lookup BlockType with id: " +
                id));
    }

}
