package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Entity {

    private List<Entity> ownBullets = new ArrayList<>();

    public void addOwnBullet(Entity bullet) {
        if (ownBullets.size() > 20) {
            ownBullets.remove(0);
        }
        ownBullets.add(bullet);
    }
    public boolean ownsBullet(Entity bullet) {
        return ownBullets.contains(bullet);
    }


}
