package com.catinthedark.cw_inc.impl.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.catinthedark.cw_inc.impl.common.Constants;
import com.catinthedark.cw_inc.impl.common.DirectionX;
import com.catinthedark.cw_inc.impl.common.GameShared;
import com.catinthedark.cw_inc.impl.common.GameState;
import com.catinthedark.cw_inc.impl.message.BlockCreateReq;
import com.catinthedark.cw_inc.lib.AbstractSystemDef;
import com.catinthedark.cw_inc.lib.Nothing;
import com.catinthedark.cw_inc.lib.Pipe;
import com.catinthedark.cw_inc.lib.Port;

import java.util.HashMap;
import java.util.Map;

import static com.catinthedark.cw_inc.lib.SysUtils.conditional;

/**
 * Created by over on 15.11.14.
 */
public class PhysicsSystemDef extends AbstractSystemDef {
    public PhysicsSystemDef(GameShared shared) {
        masterDelay = 16;
        botCreated = new Pipe<>();
        botKilled = new Pipe<>(this);

        Sys sys = new Sys(shared);
        updater(conditional(() -> sys.state == GameState.IN_GAME, sys::update));
        onGameStart = serialPort(sys::onGameStart);
        playerMoveRight = asyncPort(sys::playerMoveRight);
        playerMoveLeft = asyncPort(sys::playerMoveLeft);
        playerJump = asyncPort(sys::playerJump);
        onCreateBlock = asyncPort(sys::createBlock);
        onCreateBot = asyncPort(sys::createBot);
        playerAttack = asyncPort(sys::playerAttack);
    }

    public final Port<Vector2> onCreateBot;
    public final Pipe<Integer> botCreated;
    public final Pipe<Integer> botKilled;
    public final Port<Nothing> onGameStart;
    public final Port<Nothing> playerMoveRight;
    public final Port<Nothing> playerMoveLeft;
    public final Port<Nothing> playerJump;
    public final Port<BlockCreateReq> onCreateBlock;
    public final Port<Nothing> playerAttack;

    private class Sys {
        public Sys(GameShared shared) {
            this.shared = shared;
        }

        GameState state = GameState.INIT;
        GameShared shared;
        World world;

        float frontEdge = 0;
        float backEdge = 0;

        Body playerBody;
        boolean isPlayerOnFloor = true;
        boolean playerInAttack = false;
        Cable cable;
        final Map<Long, Body> blocks = new HashMap<>();
        final Map<Integer, Body> bots = new HashMap<>();

        void update(float delta) {
            world.step(delta, 6, 300);
            //detect allowed for player space
            if (playerBody.getPosition().x > frontEdge) {
                frontEdge = playerBody.getPosition().x;
                backEdge = frontEdge - (16 + 4) < 0 ? 0 : frontEdge - (16 + 4);
            }

            //broadcast player position
            shared.pPos.update(pos -> pos.set(playerBody.getPosition()));

            //broadcast player cable blocks position
            for (int idx = 0; idx < cable.getBodyList().size(); idx++) {
                final Vector2 blockPos = cable.getBodyList().get(idx).getPosition();
                shared.cableDots.update(idx, pos -> pos.set(blockPos));
            }

            //broadcast bots positions
            bots.entrySet().forEach((kv) -> {
                int id = kv.getKey();
                shared.bots.update(id, data -> data.pos.set(kv.getValue().getPosition()));
            });

            if (playerInAttack) {
                //raycast via bots
                Vector2 fireTo = new Vector2();
                Vector2 pPos = playerBody.getPosition();

                if (shared.pDirection.get().dirX == DirectionX.RIGHT)
                    fireTo.x = pPos.x + 8;
                else
                    fireTo.x = pPos.x - 8;

                switch (shared.pDirection.get().dirY) {
                    case UP:
                        fireTo.y = pPos.y + 8;
                        break;
                    case MIDDLE:
                        fireTo.y = pPos.y;
                        break;
                    case DOWN:
                        fireTo.y = pPos.y - 8;
                        break;
                }

                world.rayCast((fixture, point, normal, fraction) -> {
                    if (fixture.getUserData() == null)
                        return -1;

                    if (fixture.getUserData().getClass() == BotUserData.class) {
                        BotUserData data = (BotUserData) fixture.getUserData();
                        data.health -= delta;
                        //System.out.print("Bot with id:" + data.id + "on damage!");
                        if (data.health < 0) {
                            if (!data.killed) {
                                defer(() -> {
                                    System.out.println("Bot with id:" + data.id + "died!");
                                    botKilled.write(data.id, () -> {
                                        System.out.println("World locked? =>" + world.isLocked());
                                        Body botBody = bots.get(data.id);
                                        world.destroyBody(botBody);
                                        bots.remove(data.id);
                                        shared.bots.free(data.id);
                                    });
                                }, 0);
                                data.killed = true;
                            }
                        }
                    }

                    return -1;
                }, pPos, fireTo);
            }

        }


        void _createPlayer() throws InterruptedException {
            cable = new Cable(world, new Vector2(5, 5), 1.0f, Constants.CABLE_SEGS);
            playerBody = BodyFactory.createPlayer(world, cable);
        }

        void onGameStart(Nothing ignored) throws InterruptedException {
            System.out.println("Physics: on game start");
            if (world != null)
                world.dispose();
            world = new World(new Vector2(0, Constants.WORLD_GRAVITY), true);

            world.setContactListener(new HitTester((isOnGround) -> isPlayerOnFloor = isOnGround));

            _createPlayer();

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
            if (isPlayerOnFloor) {
                playerBody.applyLinearImpulse(Constants.JUMP_IMPULSE,
                        playerBody.getPosition(), true);
            }
        }

        void createBlock(BlockCreateReq req) {
            Body blockBody = BodyFactory.createBlock(world, req.type, req.x, req.y);
            blocks.put(req.id, blockBody);
        }

        void createBot(Vector2 deployTo) throws InterruptedException {
            int pointer = shared.bots.alloc(new BotPhysicsData(deployTo.cpy(), new Vector2()));
            Body botBody = BodyFactory.createBot(world, pointer, deployTo);
            bots.put(pointer, botBody);
            botCreated.write(pointer);
        }

        void playerAttack(Nothing ignored) {
            playerInAttack = true;
            defer(() -> playerInAttack = false, 1200);
        }

    }

}