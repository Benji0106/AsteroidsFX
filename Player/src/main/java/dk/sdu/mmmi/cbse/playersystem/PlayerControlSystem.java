package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;


public class PlayerControlSystem implements IEntityProcessingService {
    private int loopsSinceLastShot = 0;
    @Override
    public void process(GameData gameData, World world) {
            
        for (Entity player : world.getEntities(Player.class)) {

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - 5);                
            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + 5);                
            }

            fixTurningDEG(player);

            if (gameData.getKeys().isDown(GameKeys.UP)) {
                player.setX(player.getX() + player.getMovementSpeed()*Math.cos(Math.toRadians(player.getRotation())));
                player.setY(player.getY() + player.getMovementSpeed()*Math.sin(Math.toRadians(player.getRotation())));
            }
            if ((gameData.getKeys().isDown(GameKeys.SPACE)) && loopsSinceLastShot>8) {
                world.addEntity(getBulletSPIs().stream().findFirst().orElse(null).createBullet(player, gameData));
                loopsSinceLastShot = 0;
            }
            loopsSinceLastShot++;



            if (player.getX() < 0) {
                player.setX(1);
            }

            if (player.getX() > gameData.getDisplayWidth()) {
                player.setX(gameData.getDisplayWidth()-1);
            }

            if (player.getY() < 0) {
                player.setY(1);
            }

            if (player.getY() > gameData.getDisplayHeight()) {
                player.setY(gameData.getDisplayHeight()-1);
            }
        }
    }

    private void fixTurningDEG(Entity player) {
        if (player.getRotation() < 0) {
            player.setRotation(player.getRotation()+360);
        } else if (player.getRotation() >= 360) {
            player.setRotation(player.getRotation()-360);
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
//        System.out.println("BulletSPIs");
//        System.out.println(ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList()));
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
