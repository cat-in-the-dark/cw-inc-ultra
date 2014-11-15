package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.CallbackRunner;
import com.catinthedark.cw_inc.lib.Launcher;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.SharedMemory;

public class CwInc extends ApplicationAdapter {
    private CallbackRunner runner;

    @Override
    public void create() {
        Assets.init(new Config());

        SharedMemory<Vector2> entitiesShared = new SharedMemory<>(Vector2.class, 100);

        final ViewSystemDef viewSystem = new ViewSystemDef(entitiesShared.reader);
        final InputSystemDef inputSystem = new InputSystemDef();
        final PuppetMasterDef puppetMaster = new PuppetMasterDef();
        final PhysicsSystemDef physicsSystem = new PhysicsSystemDef(entitiesShared.writer);


        inputSystem.onKeyUp.connect(viewSystem.cameraUp);
        puppetMaster.onMenuEnter.connect(inputSystem.menuEnter, physicsSystem.menuEnter, viewSystem.onMenuEnter);
        physicsSystem.entityCreated.connect(viewSystem.newEntity);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    try {
                        inputSystem.keyEsc.write(Nothing.NONE, () -> {
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        Launcher.inThread(inputSystem);
        runner = Launcher.viaCallback(viewSystem);
        Launcher.inThread(puppetMaster);
        Launcher.inThread(physicsSystem);
    }

    @Override
    public void render() {
        try {
            runner.step();
        } catch (InterruptedException ex) {
            System.out.println("Shutting down VM");
            System.exit(0);
        }

    }
}
