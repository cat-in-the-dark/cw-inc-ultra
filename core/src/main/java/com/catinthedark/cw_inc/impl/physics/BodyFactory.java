package com.catinthedark.cw_inc.impl.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.catinthedark.cw_inc.impl.common.Constants;
import com.catinthedark.cw_inc.impl.level.BlockType;

/**
 * Created by over on 04.12.14.
 */
public class BodyFactory {

    public static Body createPlayer(World world, Cable cable){
        CircleShape playerShape = new CircleShape();
        playerShape.setRadius(Constants.PLAYER_WIDTH / 2);
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 10);
        Body playerBody = world.createBody(bodyDef);
        Fixture pFix = playerBody.createFixture(playerShape, 0.1f);
        pFix.setUserData(new PlayerUserData());

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = playerBody;
        jointDef.bodyB = cable.getBodyList().get(0);
        world.createJoint(jointDef);

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.bodyA = playerBody;
        weldJointDef.bodyB = cable.getBodyList().get(
                cable.getBodyList().size() - 1);
        weldJointDef.localAnchorA.set(-30, 8);
        world.createJoint(weldJointDef);
        pFix.setFriction(Constants.FRICTION);

        return playerBody;
    }
    public static Body createBlock(World world, BlockType type, float atX, float atY) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(atX, atY);
        Body blockBody = world.createBody(def);

        PolygonShape blockShape;
        Vector2 dots[];
        Fixture blockFixture = null;

        switch (type) {
            case GRASS:
            case GRASS_SHADOW:
            case UNDERGROUND:
            case GRASS_SLOPE_LEFT_SHADOW:
            case GRASS_SLOPE_RIGHT_SHADOW:
                blockShape = new PolygonShape();
                blockShape.setAsBox(Constants.BLOCK_WIDTH / 2, Constants.BLOCK_HEIGHT / 2);
                blockFixture = blockBody.createFixture(blockShape, 0);
                break;
            case GRASS_SLOPE_LEFT:
                blockShape = new PolygonShape();
                dots = new Vector2[3];
                dots[0] = new Vector2(Constants.BLOCK_WIDTH / 2, Constants.BLOCK_HEIGHT / 2);
                dots[1] = new Vector2(Constants.BLOCK_WIDTH / 2, -1 * Constants.BLOCK_HEIGHT / 2);
                dots[2] = new Vector2(-1 * Constants.BLOCK_WIDTH, -1 * Constants.BLOCK_HEIGHT);
                blockShape.set(dots);
                blockFixture = blockBody.createFixture(blockShape, 0);
                break;
            case GRASS_SLOPE_RIGHT:
                blockShape = new PolygonShape();
                dots = new Vector2[3];
                dots[0] = new Vector2(-1 * Constants.BLOCK_WIDTH / 2, Constants.BLOCK_HEIGHT / 2);
                dots[1] = new Vector2(-1 * Constants.BLOCK_WIDTH / 2, -1 * Constants.BLOCK_HEIGHT / 2);
                dots[2] = new Vector2(Constants.BLOCK_WIDTH / 2, -1 * Constants.BLOCK_HEIGHT / 2);
                blockShape.set(dots);
                blockFixture = blockBody.createFixture(blockShape, 0);
                break;
            default:
        }

        blockFixture.setUserData(new BlockUserData());

        return blockBody;
    }

    public static Body createBot(World world, int pointer, Vector2 deployTo) {
        CircleShape crabShape = new CircleShape();
        crabShape.setRadius(Constants.CRAB_WIDTH / 2);
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(deployTo);
        Body body = world.createBody(bodyDef);
        body.createFixture(crabShape, 1.0f)
                .setUserData(new BotUserData(pointer, 1));

        return body;
    }

}
