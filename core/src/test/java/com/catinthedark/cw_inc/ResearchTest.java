package com.catinthedark.cw_inc;

import com.catinthedark.cw_inc.lib.Launcher;
import org.junit.Test;

public class ResearchTest {
    @Test
    public void test() throws InterruptedException {
        System1 sys1 = new System1(Thread.currentThread());
        System2 sys2 = new System2();

        sys2.messagePipe.connect(sys1.messagePoll);

        Launcher.inThread(sys2);
        Launcher.inThread(sys1).join();
    }
}