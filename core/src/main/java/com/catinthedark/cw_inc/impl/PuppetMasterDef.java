package com.catinthedark.cw_inc.impl;

/**
 * Created by over on 14.11.14.
 */

import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Pipe;
import com.catinthedark.cw_inc.lib.Port;

/**
 * Главная система, которая контролирует весь игровой процесс
 */
public class PuppetMasterDef extends AbstractSystemDef {
    final Sys sys = new Sys();
    public final Pipe<Nothing> onMenuEnter = new Pipe<>();
    public final Pipe<Nothing> onGameStart = new Pipe<>();
    public final Port<Nothing> gameStart = asyncPort(sys::onGameStart);

    {
        isQueueBlocking = true;
    }

    @Override
    public void onStart() {
        System.out.println("puppet: on start executed");
        defer(() -> {
            System.out.println("defer executed");
            onMenuEnter.write(Nothing.NONE);
            sys.state = GameState.MENU;
        }, 1000);

    }

    private class Sys {
        GameState state = GameState.INIT;

        void onGameStart(Nothing ignored) throws InterruptedException {
            if (state == GameState.MENU) {
                System.out.println("puppet:broadcast GAME_START");
                onGameStart.write(Nothing.NONE);
                state = GameState.IN_GAME;
            }
        }

    }
}
