package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.impl.level.BlockType;
import com.catinthedark.cw_inc.impl.level.LevelMatrix;
import com.catinthedark.cw_inc.impl.level.Preset;
import com.catinthedark.cw_inc.lib.*;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by over on 18.11.14.
 */
public class LevelSystemDef extends AbstractSystemDef {
    public LevelSystemDef(PhysicsShared.Reader pShared) {
        this.sys = new Sys(pShared);
        updater(sys::createLevel);
        masterDelay = 100;

        onGameStart = serialPort(sys::onGameStart);
        createBlock = new Pipe<>();
    }

    private final Sys sys;
    public final Port<Nothing> onGameStart;
    private final Random rand = new Random();
    public final Pipe<BlockCreateReq> createBlock;

    public LevelMatrix.View levelView() {
        return sys.matrix.view;
    }

    private class Sys {
        final LevelMatrix matrix;
        final PhysicsShared.Reader pShared;
        int currentX = 0;
        GameState state = GameState.INIT;
        long blockIdSeq = 0;

        Sys(PhysicsShared.Reader pShared) {
            this.pShared = pShared;

            matrix = new LevelMatrix(10, 70, block -> {

            });
        }

        public void addPreset() throws InterruptedException {
            System.out.println("add preset!");
            for (BlockType[] col : Preset.easyPresets[rand.nextInt(Preset.easyPresets.length)]
                    .blocks) {
                LevelMatrix.ColMapper mapper = matrix.nextCol();
                for (int y = 0; y < col.length; y++) {
                    BlockType block = col[y];
                    if (block == BlockType.EMPTY)
                        continue;
                    mapper.setCell(y, block.at(blockIdSeq, currentX, y * 32));
                    createBlock.write(new BlockCreateReq(blockIdSeq, block, currentX / 32 * Constants.BLOCK_WIDTH, y * Constants.BLOCK_HEIGHT));
                }
                currentX += 32;
            }
        }


        public void createLevel(float delta) throws InterruptedException {
            if (state != GameState.IN_GAME)
                return;

            if (pShared.pPos.get().x * 32 + 500 > currentX)
                addPreset();
        }

        public void onGameStart(Nothing ignored) throws InterruptedException {
            //matrix.reset
            //IntStream.range(1, 3).forEach(i -> addPreset());
            for (int i = 0; i < 3; i++)
                addPreset();
            state = GameState.IN_GAME;
        }

    }

}
