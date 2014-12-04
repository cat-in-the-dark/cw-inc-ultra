package com.catinthedark.cw_inc.impl.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;
import java.util.List;

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
