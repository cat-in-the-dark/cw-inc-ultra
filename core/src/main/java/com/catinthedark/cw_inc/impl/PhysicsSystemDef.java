package com.catinthedark.cw_inc.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.catinthedark.cw_inc.lib.*;

import java.util.*;

import static com.catinthedark.cw_inc.impl.SysUtils.conditional;

/**
 * Created by over on 15.11.14.
 */
public class PhysicsSystemDef extends AbstractSystemDef {
    public PhysicsSystemDef(PhysicsShared.Writer shared, SharedMemory<Vector2>.Writer entities) {
        masterDelay = 16;
        entityCreated = new Pipe<>();

        Sys sys = new Sys(shared, entities);
        updater(conditional(() -> sys.state == GameState.IN_GAME, sys::update));
        onGameStart = serialPort(sys::onGameStart);
        playerMoveRight = asyncPort(sys::playerMoveRight);
        playerMoveLeft = asyncPort(sys::playerMoveLeft);
        playerJump = asyncPort(sys::playerJump);
        onCreateBlock = asyncPort(sys::createBlock);
    }

    public final Pipe<Integer> entityCreated;
    public final Port<Nothing> onGameStart;
    public final Port<Nothing> playerMoveRight;
    public final Port<Nothing> playerMoveLeft;
    public final Port<Nothing> playerJump;
    public final Port<BlockCreateReq> onCreateBlock;

    private class Sys {
        public Sys(PhysicsShared.Writer shared, SharedMemory<Vector2>.Writer entities) {
            this.shared = shared;
            this.entities = entities;
        }

        final List<Integer> pointers = new ArrayList<>();
        final Random rand = new Random(System.nanoTime());
        GameState state = GameState.INIT;
        PhysicsShared.Writer shared;
        final SharedMemory<Vector2>.Writer entities;
        World world;

        float frontEdge = 0;
        float backEdge = 0;

        Body playerBody;
        boolean isPlayerOnFloor = true;
        Cable cable;
        final Map<Long, Body> blocks = new HashMap<>();

        void update(float delay) {
            world.step(1.0f / 60, 10, 300);
            if(playerBody.getPosition().x > frontEdge) {
                frontEdge = playerBody.getPosition().x;
                backEdge = frontEdge - (16+4) < 0 ? 0: frontEdge - (16+4);
            }

            shared.pPos.update(pos -> pos.set(playerBody.getPosition()));

            for(int idx = 0; idx < cable.getBodyList().size(); idx++) {
                final Vector2 blockPos = cable.getBodyList().get(idx).getPosition();
                shared.cableDots.update(idx, pos -> pos.set(blockPos));
            }

            pointers.forEach(p -> entities.map(p).set(rand.nextInt(1024), rand.nextInt(640)));
        }


        /**
         * Created by kirill on 24.08.14.
         */
        public class Cable {
            private List<Body> bodyList;

            private List<Joint> jointList;

            public final float segmentLength;
            public final float segmentThick;

            public Cable(World world, Vector2 startPos, float segmentLength, int segmentCount) {
                this.segmentLength = segmentLength;
                this.segmentThick = 0.5f;

                bodyList = new ArrayList<Body>(segmentCount);
                jointList = new ArrayList<Joint>(segmentCount + 1);  // we need joints at ends of cable;

                for (int i = 0; i < segmentCount; i++) {
                    CircleShape segmentShape = new CircleShape();
                    segmentShape.setRadius(segmentThick / 2);

                    BodyDef bodyDef = new BodyDef();
                    bodyDef.fixedRotation = false;
                    bodyDef.type = BodyDef.BodyType.DynamicBody;
                    bodyDef.position.set(startPos.x - i * segmentLength, startPos.y);
                    Body segmentBody = world.createBody(bodyDef);
                    segmentBody.createFixture(segmentShape, 0.01f);

                    bodyList.add(segmentBody);

                    if (i > 0) {
                        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                        revoluteJointDef.bodyA = segmentBody;
                        revoluteJointDef.bodyB = bodyList.get(i - 1);
                        revoluteJointDef.collideConnected = false;
                        revoluteJointDef.localAnchorA.set(0, segmentLength);

                        Joint joint = world.createJoint(revoluteJointDef);
                    }
                }
            }

            public List<Body> getBodyList() {
                return bodyList;
            }

            public List<Joint> getJointList() {
                return jointList;
            }
        }


        void _createPlayer() throws InterruptedException {
            CircleShape playerShape = new CircleShape();
            playerShape.setRadius(Constants.PLAYER_WIDTH / 2);
            BodyDef bodyDef = new BodyDef();
            bodyDef.fixedRotation = true;
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(0, 10);
            playerBody = world.createBody(bodyDef);
            Fixture pFix = playerBody.createFixture(playerShape, 0.1f);
            pFix.setUserData(new PlayerData());
            cable  = new Cable(world, new Vector2(5, 5), 1.0f, Constants.CABLE_SEGS);

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

        }

        void onGameStart(Nothing ignored) throws InterruptedException {
            System.out.println("Physics: on game start");
            if(world != null)
                world.dispose();
            world = new World(new Vector2(0, Constants.WORLD_GRAVITY), true);

            world.setContactListener(new HitTester((isOnGround) -> isPlayerOnFloor = isOnGround));

            _createPlayer();

            for (int i = 0; i < 10; i++) {
                int pointer = entities.alloc(new Vector2(rand.nextInt(1024), rand.nextInt(640)));
                pointers.add(pointer);
                entityCreated.write(pointer);
            }

            state = GameState.IN_GAME;
        }

        void playerMoveRight(Nothing ignored) {
            if (playerBody.getLinearVelocity().x < 10)
                playerBody.applyLinearImpulse(Constants.WALKING_FORCE_RIGHT,
                        new Vector2(0f, 0f), true);
        }

        void playerMoveLeft(Nothing ignored) {
            System.out.println(backEdge);
            if (playerBody.getLinearVelocity().x > -10 && playerBody.getPosition().x > backEdge + 3)
                playerBody.applyLinearImpulse(Constants.WALKING_FORCE_LEFT,
                        new Vector2(0f, 0f), true);
        }

        void playerJump(Nothing ignored) {
            if(isPlayerOnFloor){
                playerBody.applyLinearImpulse(Constants.JUMP_IMPULSE,
                        playerBody.getPosition(), true);
            }
        }

        void createBlock(BlockCreateReq req) {
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set(req.x, req.y);
            Body blockBody = world.createBody(def);

            PolygonShape blockShape;
            Vector2 dots[];
            Fixture blockFixture = null;

            switch (req.type) {
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

            blockFixture.setUserData(new BlockData());
            blocks.put(req.id, blockBody);
        }

    }
}