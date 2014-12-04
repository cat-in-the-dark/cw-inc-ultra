package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.catinthedark.cw_inc.impl.common.Assets;
import com.catinthedark.cw_inc.impl.common.GameShared;
import com.catinthedark.cw_inc.impl.input.InputSystemDef;
import com.catinthedark.cw_inc.impl.level.LevelSystemDef;
import com.catinthedark.cw_inc.impl.physics.PhysicsSystemDef;
import com.catinthedark.cw_inc.impl.view.ViewSystemDef;
import com.catinthedark.cw_inc.lib.CallbackRunner;
import com.catinthedark.cw_inc.lib.Launcher;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Pipe;

public class CwInc extends ApplicationAdapter {
    private CallbackRunner runner;

    @Override
    public void create() {
        Assets.init();

        GameShared gameShared = new GameShared();

        final LevelSystemDef levelSystem = new LevelSystemDef(gameShared);
        final ViewSystemDef viewSystem = new ViewSystemDef(gameShared, levelSystem
                .levelView());
        final PhysicsSystemDef physicsSystem = new PhysicsSystemDef(gameShared);
        final InputSystemDef inputSystem = new InputSystemDef(gameShared);

        final Pipe<Nothing> menuEnter = new Pipe<>();

        inputSystem.onGameStart.connect(physicsSystem.onGameStart,
                levelSystem.onGameStart,
                viewSystem.onGameStart);
        levelSystem.createBlock.connect(physicsSystem.onCreateBlock);
        levelSystem.createBot.connect(physicsSystem.onCreateBot);
        physicsSystem.botCreated.connect(viewSystem.newBot);
        physicsSystem.botKilled.connect(viewSystem.botKilled);

        inputSystem.onKeyD.connect(physicsSystem.playerMoveRight);
        inputSystem.onKeyA.connect(physicsSystem.playerMoveLeft);
        inputSystem.onKeySpace.connect(physicsSystem.playerJump);
        inputSystem.onPlayerAttack.connect(viewSystem.playerAttack, physicsSystem.playerAttack);

        menuEnter.connect(viewSystem.onMenuEnter, inputSystem.menuEnter);


        Launcher.inThread(inputSystem);
        Launcher.inThread(levelSystem);
        runner = Launcher.viaCallback(viewSystem);
        Launcher.inThread(physicsSystem);

        //goto menu after 1000ms
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                menuEnter.write(Nothing.NONE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void render() {
        try {
            runner.step(Gdx.graphics.getDeltaTime());
        } catch (InterruptedException ex) {
            System.out.println("Shutting down VM");
            System.exit(0);
        }

    }
}
