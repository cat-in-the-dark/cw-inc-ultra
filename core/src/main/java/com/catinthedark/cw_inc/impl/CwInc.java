package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.*;
import com.catinthedark.cw_inc.lib.CallbackRunner;
import com.catinthedark.cw_inc.lib.Launcher;
import com.catinthedark.cw_inc.lib.Nothing;

public class CwInc extends ApplicationAdapter {
    private CallbackRunner runner;

    @Override
    public void create() {
        final ViewSystemDef viewSystem = ViewSystemDef.instance();
        final InputSystemDef inputSystem = InputSystemDef.instance();

        inputSystem.onKeyUp().connect(viewSystem.cameraUp);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    inputSystem.keyEsc.write(Nothing.NONE);
                    return true;
                }
                return false;
            }
        });

        Launcher.inThread(inputSystem);
        runner = Launcher.viaCallback(viewSystem);
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
