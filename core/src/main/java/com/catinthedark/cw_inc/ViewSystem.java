package com.catinthedark.cw_inc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.catinthedark.cw_inc.lib.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by over on 11.11.14.
 */
public class ViewSystem implements AbstractSystem {
    private final List<Updater> updaters = Arrays.asList(new Updater(this::render));

    public final Port<Nothing> cameraUp = new QueuePort<>(this::cameraUp);
    private final List<Port> ports = Arrays.asList(cameraUp);

    @Override
    public List<Updater> getUpdaters() {
        return updaters;
    }

    @Override
    public List<Port> getPorts() {
        return ports;
    }

    final SpriteBatch batch = new SpriteBatch();
    final Texture img = new Texture("badlogic.jpg");
    final Camera camera = new OrthographicCamera(640, 480);

    {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void afterCameraMove() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void cameraUp(long globalTime, Nothing ignored) {
        camera.position.y += 10;
        afterCameraMove();
    }


    void render(long globalTime, long delay) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }
}
