package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Bullet bullet : world.getEntities(Bullet.class)) {
            // Move Bullet
            bullet.setX(bullet.getX() + (bullet.getMovementSpeed() *Math.cos(Math.toRadians(bullet.getRotation()))));
            bullet.setY(bullet.getY() + (bullet.getMovementSpeed() *Math.sin(Math.toRadians(bullet.getRotation()))));
            if (bullet.getX() < 0 ||
                    bullet.getX() > gameData.getDisplayWidth() ||
                    bullet.getY() < 0 ||
                    bullet.getY() > gameData.getDisplayHeight()) {
                bullet.setRedundant(true);
            }
            if (bullet.isHit()) {
                if (bullet.getHits().contains(bullet.getShooter())) {
                    bullet.getHits().remove(bullet.getShooter());
                    if (bullet.getHits().isEmpty()) {
                        bullet.setHit(false);
                        continue;
                    }
                }

                bullet.setRedundant(true);
            }
        }
    }
}
