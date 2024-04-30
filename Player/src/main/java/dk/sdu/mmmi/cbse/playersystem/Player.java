package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.ArrayList;

/**
 *
 * @author Emil
 */
public class Player extends Entity {

    private ArrayList<Entity> ownBullets = new ArrayList<>();

    public void addOwnBullet(Entity bullet) {
        if (ownBullets.size() > 5) {
            ownBullets.remove(0);
        }
        ownBullets.add(bullet);
    }

    public boolean ownsBullet(Entity bullet) {
        return ownBullets.contains(bullet);
    }

    private int loopsSinceLastShot;

    public int getLoopsSinceLastShot() {
        return loopsSinceLastShot;
    }

    public void incrementLoopsSinceLastShot() {
        this.loopsSinceLastShot++;
    }

    public void resetLoopsSinceLastShot() {
        this.loopsSinceLastShot = 0;
    }

}
