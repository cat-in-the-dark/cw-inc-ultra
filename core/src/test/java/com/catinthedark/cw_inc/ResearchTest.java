package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.Launcher;
import org.junit.Test;

public class ResearchTest {
    @Test
    public void test() throws InterruptedException {
        System1Def sys1 = System1Def.instance();
        System2Def sys2 = System2Def.instance();

        sys2.messagePipe().connect(sys1.messagePoll);

        Launcher.inThread(sys2);
        Launcher.inThread(sys1).join();
    }
}