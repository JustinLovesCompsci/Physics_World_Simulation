package jboxGlue;

import jgame.platform.JGEngine;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class WorldManager
{
    private static World ourWorld;
    
    static {
        ourWorld = null;
    }

    public static World getWorld ()
    {
        if (ourWorld == null) { // make sure we have a world
            throw new RuntimeException("call initWorld() before getWorld()!");
        }
        return ourWorld;
    }

    public static void initWorld (JGEngine engine)
    {
    	Vec2 gravity = new Vec2(0.0f, 0.0f);
        AABB worldBounds = new AABB(new Vec2(0, 0), new Vec2(engine.displayWidth(), engine.displayHeight()));
        ourWorld = new World(worldBounds, gravity, false);
        ourWorld.setGravity(gravity);
    }
}
