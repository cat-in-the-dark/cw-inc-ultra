package com.catinthedark.cw_inc.impl;

/**
 * Created by over on 14.11.14.
 */

import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Pipe;

/**
 * Главная система, которая следит за всем игровым процессом
 */
public class PuppetMasterDef extends AbstractSystemDef {
    final Sys sys = new Sys();
    public final Pipe<Nothing> onMenuEnter = new Pipe<>();

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

    }
}
