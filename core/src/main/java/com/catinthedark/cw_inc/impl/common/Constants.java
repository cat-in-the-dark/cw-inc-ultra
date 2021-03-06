package com.catinthedark.cw_inc.impl.common;

import com.badlogic.gdx.math.Vector2;

public class Constants {
	public static final int EASY = 1;
	public static final int MEDIUM = 2;
	public static final int HARD = 3;
	public static final int tileSize = 32;

	public static final float PLAYER_WIDTH = 2.0f;
	public static final float PLAYER_HEIGHT = 2.0f;

	public static final float CRAB_WIDTH = 2.0f;
	public static final float CRAB_HEIGHT = 2.0f;

	public static final float MUSHROOMED_CRAB_WIDTH = 2.0f;
	public static final float MUSHROOMED_CRAB_HEIGHT = 2.0f;

	public static final float BLOCK_WIDTH = 1.0f;
	public static final float BLOCK_HEIGHT = 1.0f;

	public static final int WORLD_GRAVITY = -30;

	public static final Vector2 JUMP_IMPULSE = new Vector2(0f, 3.0f);
	public static final float WALKING_FORCE = 1.0f;
	public static final float BULLET_FORCE = 3.0f;
	public static final int FRICTION = 1;
	public static final Vector2 WALKING_FORCE_RIGHT = new Vector2(
			WALKING_FORCE, 0f);
	public static final Vector2 WALKING_FORCE_LEFT = new Vector2(-1
			* WALKING_FORCE, 0f);

	public static final Vector2 BULLET_FORCE_LEFT = new Vector2(-1
			* BULLET_FORCE, 0f);
	public static final Vector2 BULLET_FORCE_RIGHT = new Vector2(-1
			* BULLET_FORCE, 0f);

	public static final float ANIMATION_SPEED = 0.1f;
	public static final int CABLE_SEGS = 40;
	public static final int CABLE_STEPS = 100;
	public static final int CABLE_THICK = 5;
	public static final float MUSHROOMED_CRAB_SHUT_DELAY = 1f;
	public static final float MUSHROOMED_CRAB_SHUT_TIME = 0.5f;
    public static final int START_HEALTH = 20;
    public static float MAX_DISTANCE_CAMERA_AHEAD = 4.0f;
	public static float BACK_CAMERA_SPEED = 0.05f;
	public static float MAIN_CAMERA_SPEED = 6.0f;

	public static final int DISTANCE_MAX_EASY = 10000;
	public static final float BOT_DEATH_ANIMATION_TIME = 0.5f;

    public static final int CRAB_SCORE = 100;
}
