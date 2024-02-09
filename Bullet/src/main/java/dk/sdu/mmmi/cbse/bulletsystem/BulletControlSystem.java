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
            double changeX = 2*Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = 2*Math.sin(Math.toRadians(bullet.getRotation()));
            bullet.setX(bullet.getX() + changeX);
            bullet.setY(bullet.getY() + changeY);
//            System.out.println("Bullet: " + bullet + " is moving (" + bullet.getX() + ", " + bullet.getY() + ")" );

        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity newBullet = new Bullet();
        newBullet.setPolygonCoordinates(0, 2, 3.5, 0, 0, -2, -3.5, 0); // bullet shape
//        newBullet.setPolygonCoordinates(-5,-5,10,0,-5,5); // spaceShip shape
        newBullet.setX(shooter.getX());
        newBullet.setY(shooter.getY());
        newBullet.setRotation(shooter.getRotation());
//        System.out.println("Creating bullet: " + newBullet);
        return newBullet;
    }

    private void setShape(Entity entity) {

    }

}
