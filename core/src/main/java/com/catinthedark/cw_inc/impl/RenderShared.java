package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.lib.SharedMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by over on 12.11.14.
 */
public class RenderShared {
    public final Camera camera = new OrthographicCamera(1024, 640);
    public SharedMemory<Vector2>.Reader entities;
    public final List<Integer> entityPointers = new ArrayList<>();
}