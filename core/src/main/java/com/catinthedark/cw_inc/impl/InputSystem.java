package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.catinthedark.cw_inc.lib.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by over on 11.11.14.
 */
public class InputSystem implements AbstractSystem {

    public final Port<Nothing> keyEsc = new QueuePort<>(this::onEsc, true);
    public final Pipe<Nothing> onKeyUp = new Pipe<>();

    private final List<Port> ports = Arrays.asList(keyEsc);
    private final List<Updater> updaters = Arrays.asList(new Updater(this::keyUpPoll));

    @Override
    public int getMasterDelay() {
        return 100;
    }

    @Override
    public List<Port> getPorts() {
        return ports;
    }

    @Override
    public List<Updater> getUpdaters() {
        return updaters;
    }

    private void keyUpPoll(long globalTime, long delay) throws InterruptedException {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            onKeyUp.write(Nothing.NONE);
    }

    private void onEsc(long globalTime, Nothing ignored) throws InterruptedException {
        System.out.println("key ESC pressed");
        Thread.currentThread().interrupt();
    }
}
