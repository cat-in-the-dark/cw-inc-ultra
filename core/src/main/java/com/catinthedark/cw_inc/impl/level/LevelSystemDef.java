package com.catinthedark.cw_inc.impl.level;

import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.impl.common.Constants;
import com.catinthedark.cw_inc.impl.common.GameShared;
import com.catinthedark.cw_inc.impl.common.GameState;
import com.catinthedark.cw_inc.impl.message.BlockCreateReq;
import com.catinthedark.cw_inc.lib.*;
import com.catinthedark.cw_inc.lib.common.Nothing;
import com.catinthedark.cw_inc.lib.io.Pipe;
import com.catinthedark.cw_inc.lib.io.Port;

import java.util.Random;

/**
 * Created by over on 18.11.14.
 */
public class LevelSystemDef extends AbstractSystemDef {
    public LevelSystemDef(GameShared pShared) {
        this.sys = new Sys(pShared);
        updater(sys::createLevel);
        masterDelay = 100;

        onGameStart = serialPort(sys::onGameStart);
        createBlock = new Pipe<>();
        createBot = new Pipe<>();
    }

    private final Sys sys;
    public final Port<Nothing> onGameStart;
    private final Random rand = new Random();
    public final Pipe<BlockCreateReq> createBlock;
    public final Pipe<Vector2> createBot;

    public LevelMatrix.View levelView() {
        return sys.matrix.view;
    }

    private class Sys {
        final LevelMatrix matrix;
        final GameShared pShared;
        int currentX = 0;
        GameState state = GameState.INIT;
        long blockIdSeq = 0;

        Sys(GameShared pShared) {
            this.pShared = pShared;

            matrix = new LevelMatrix(10, 70, block -> {

            });
        }

        public void addPreset() throws InterruptedException {
            System.out.println("add preset!");
            int presetEdge = currentX;
            Preset preset = Preset.easyPresets[rand.nextInt(Preset.easyPresets.length)];

            for (BlockType[] col : preset.blocks) {
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

            for(Vector2 botPos : preset.botsPositions){
                Vector2 deployTo = botPos.cpy();
                deployTo.x = presetEdge/32 + deployTo.x;
                createBot.write(deployTo);
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
