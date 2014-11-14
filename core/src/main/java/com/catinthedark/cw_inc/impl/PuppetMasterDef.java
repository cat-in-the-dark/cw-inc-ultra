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
    public static PuppetMasterDef instance() {
        Sys sys = new Sys();
        return new PuppetMasterDef(sys);
    }

    private PuppetMasterDef(Sys sys) {
        super();
        this.sys = sys;
    }

    final Sys sys;
    public final Pipe<Nothing> onMenuEnter = new Pipe<>();

    @Override
    public void onStart() {
        System.out.println("puppet: on start executed");
        defer(() -> {
            System.out.println("defer executed");
            onMenuEnter.write(Nothing.NONE);
            sys.state = GameState.MENU;
        }, 1000);

    }


    private static class Sys {
        GameState state = GameState.INIT;

    }


}
