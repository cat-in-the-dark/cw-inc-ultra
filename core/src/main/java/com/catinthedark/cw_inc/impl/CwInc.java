package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.catinthedark.cw_inc.lib.*;

public class CwInc extends ApplicationAdapter {
    private CallbackRunner runner;

    @Override
    public void create() {
        Assets.init(new Config());

        GameShared physicsShared = new GameShared();

        final LevelSystemDef levelSystem = new LevelSystemDef(physicsShared);
        final ViewSystemDef viewSystem = new ViewSystemDef(physicsShared, levelSystem
                .levelView());
        final PhysicsSystemDef physicsSystem = new PhysicsSystemDef(physicsShared);
        final InputSystemDef inputSystem = new InputSystemDef();

        final Pipe<Nothing> menuEnter = new Pipe<>();

        inputSystem.onGameStart.connect(physicsSystem.onGameStart,
                levelSystem.onGameStart,
                viewSystem.onGameStart);
        levelSystem.createBlock.connect(physicsSystem.onCreateBlock);
        levelSystem.createBot.connect(physicsSystem.onCreateBot);
        physicsSystem.botCreated.connect(viewSystem.newBot);

        inputSystem.onKeyD.connect(physicsSystem.playerMoveRight);
        inputSystem.onKeyA.connect(physicsSystem.playerMoveLeft);
        inputSystem.onKeySpace.connect(physicsSystem.playerJump);
        inputSystem.playerDirXSet.connect(viewSystem.playerDirX);
        inputSystem.playerDirYSet.connect(viewSystem.playerDirY);
        inputSystem.onPlayerAttack.connect(viewSystem.playerAttack);

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
