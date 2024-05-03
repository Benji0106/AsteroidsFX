package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class BulletPlugin implements IGamePluginService, BulletSPI {

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("All bullets removed from the world");
        for (Bullet bullet : world.getEntities(Bullet.class)) {
            world.removeEntity(bullet);
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet newBullet = new Bullet();
        newBullet.setShooter(shooter);
        newBullet.setSize(2);
        newBullet.setMovementSpeed(3);
        newBullet.setPolygonCoordinates(-1.5, 0.8, 0, 0.6, 0.5, 0, 0, -0.6, -1.5, -0.8, -3, -0.6, -3.5, 0, -3, 0.6); // Oval shape
        newBullet.setX(shooter.getX());
        newBullet.setY(shooter.getY());
        newBullet.setRotation(shooter.getRotation());
        return newBullet;
    }

}
