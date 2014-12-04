package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.catinthedark.cw_inc.impl.level.LevelMatrix;
import com.catinthedark.cw_inc.lib.SharedMemory;
import com.catinthedark.cw_inc.lib.view.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by over on 12.11.14.
 */
public class RenderShared {
    public final Camera camera = new OrthographicCamera(1024, 640);
    public final OrthographicCamera backgroundCamera = new OrthographicCamera(32, 20);
    public SharedMemory<Vector2>.Reader entities;
    public PhysicsShared.Reader pShared;
    public final List<Integer> entityPointers = new ArrayList<>();
    public Vector2 playerPos = null;
    public DirectionX playerDirX = DirectionX.LEFT;
    public DirectionY playerDirY = DirectionY.MIDDLE;
    public boolean animatePlayerMove = false;
    public Renderable<RenderShared> playerAttack;
    public LevelMatrix.View levelView = null;
    public float delay = 0;
}