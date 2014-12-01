package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.impl.level.BlockType;
import com.catinthedark.cw_inc.impl.level.LevelMatrix;
import com.catinthedark.cw_inc.impl.level.Preset;
import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Port;
import com.catinthedark.cw_inc.lib.SharedMemory;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by over on 18.11.14.
 */
public class LevelSystemDef extends AbstractSystemDef {
    public LevelSystemDef(SharedMemory<Vector2>.Reader entities) {
        this.sys = new Sys(entities);
        updater(sys::createLevel);
        masterDelay = 100;

        onGameStart = serialPort(sys::onGameStart);
    }

    private final Sys sys;
    public final Port<Nothing> onGameStart;
    private final Random rand = new Random();

    public LevelMatrix.View levelView() {
        return sys.matrix.view;
    }

    private class Sys {
        final LevelMatrix matrix;
        final SharedMemory<Vector2>.Reader entities;
        int currentX = 0;
        GameState state = GameState.INIT;

        Sys(SharedMemory<Vector2>.Reader entities) {
            this.entities = entities;

            matrix = new LevelMatrix(10, 70, block -> {

            });
        }

        public void addPreset() {
            System.out.println("add preset!");
            for (BlockType[] col : Preset.easyPresets[rand.nextInt(Preset.easyPresets.length)]
                .blocks) {
                LevelMatrix.ColMapper mapper = matrix.nextCol();
                for (int y = 0; y < col.length; y++) {
                    BlockType block = col[y];
                    if (block == BlockType.EMPTY)
                        continue;
                    mapper.setCell(y, block.at(currentX, y * 32));
                }
                currentX += 32;
            }
        }


        public void createLevel(float delta) {
            if (state != GameState.IN_GAME)
                return;

            if (entities.map(1).x + 500 > currentX)
                addPreset();
        }

        public void onGameStart(Nothing ignored) {
            //matrix.reset
            IntStream.range(1, 3).forEach(i -> addPreset());
            state = GameState.IN_GAME;
        }

    }

}
