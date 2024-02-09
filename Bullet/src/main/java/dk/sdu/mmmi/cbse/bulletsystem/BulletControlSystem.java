package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.playersystem.Player;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity bullet : world.getEntities(Bullet.class)) {
            /** Move Bullet **/
            double changeX = 3*Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = 3*Math.sin(Math.toRadians(bullet.getRotation()));
            bullet.setX(bullet.getX() + changeX);
            bullet.setY(bullet.getY() + changeY);
//            System.out.println("Bullet: " + bullet + " is moving (" + bullet.getX() + ", " + bullet.getY() + ")" );

        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity newBullet = new Bullet();
//        newBullet.setPolygonCoordinates(-3.5, 2, 0, 0, -3.5, -2, -7, 0); // Rumbe shape
        newBullet.setPolygonCoordinates(-1.5, 0.8, 0, 0.6, 0.5, 0, 0, -0.6, -1.5, -0.8, -3, -0.6, -3.5, 0, -3, 0.6); // Oval shape
//        newBullet.setPolygonCoordinates(-5,-5,10,0,-5,5); // triangle spaceShip shape
        newBullet.setX(shooter.getX());
        newBullet.setY(shooter.getY());
        newBullet.setRotation(shooter.getRotation());
//        System.out.println("Creating bullet: " + newBullet);
        return newBullet;
    }

    private void setShape(Entity entity) {

    }

}
