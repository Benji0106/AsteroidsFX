package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class PlayerControlSystem implements IEntityProcessingService {

    private int loopsWithoutPlayer = 0;
    private boolean playerIsDead = false;

    @Override
    public void process(GameData gameData, World world) {

        if (world.getEntities(Player.class).isEmpty()) {
            loopsWithoutPlayer++;
        } else {
            loopsWithoutPlayer = 0;
        }

        if (playerIsDead && loopsWithoutPlayer > 200) {
            respawnPlayer(gameData, world);
            playerIsDead = false;
        }

        for (Player player : world.getEntities(Player.class)) {

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                turnLeft(player);
            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                turnRight(player);
            }

            fixTurningDEG(player);

            if (gameData.getKeys().isDown(GameKeys.UP)) {
                moveForward(player);
            }
            if ((gameData.getKeys().isDown(GameKeys.SPACE)) && player.getLoopsSinceLastShot() >8) {
                shoot(gameData, world, player);
            }
            player.incrementLoopsSinceLastShot();

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

            if (player.isHit()) {

                ArrayList<Entity> fakeHits = new ArrayList<>();
                for (Entity entity : player.getHits()) {
                    if (player.ownsBullet(entity)) {
                        fakeHits.add(entity);
                    }
                }
                for (Entity entity : fakeHits) {
                    player.removeHit(entity);
                }

                if (player.getHits().isEmpty()) {
                    player.setHit(false);
                } else {
                    player.setRedundant(true);
                    playerIsDead = true;
                }
            }

        }
    }

    private void shoot(GameData gameData, World world, Player player) {
        Entity newBullet = getBulletSPIs().stream().findFirst().orElse(null).createBullet(player, gameData);
        player.addOwnBullet(newBullet);
        world.addEntity(newBullet);
        player.resetLoopsSinceLastShot();
    }

    protected void moveForward(Player player) {
        player.setX(player.getX() + player.getMovementSpeed()*Math.cos(Math.toRadians(player.getRotation())));
        player.setY(player.getY() + player.getMovementSpeed()*Math.sin(Math.toRadians(player.getRotation())));
    }

    protected void turnRight(Player player) {
        player.setRotation(player.getRotation() + 5);
    }

    protected void turnLeft(Player player) {
        player.setRotation(player.getRotation() - 5);
    }

    private void respawnPlayer(GameData gameData, World world) {
        Player newPlayer = new Player();
        newPlayer.setSize(5);
        newPlayer.setMovementSpeed(2);
        newPlayer.setPolygonCoordinates(12, -1, 8, -1, 8, -3, 6, -3, 6, -5, -2, -5, -2, -7, 0, -7, 0, -9, -10, -9, -10, -5, -8, -5, -8, -3, -6, -3, -6, -1, -10, -1, -10, 1, -6, 1, -6, 3, -8, 3, -8, 5, -10, 5, -10, 9, 0, 9, 0, 7, -2, 7, -2, 5, 2, 5, 2, 1, 4, 1, 4, -1, 2, -1, 2, -3, 4, -3, 4, -1, 6, -1, 6, 1, 4, 1, 4, 3, 2, 3, 2, 5, 6, 5, 6, 3, 8, 3, 8, 1, 12, 1); // double-sized Spaceship shape
        newPlayer.setX(gameData.getDisplayHeight()/2);
        newPlayer.setY(gameData.getDisplayWidth()/2);
        newPlayer.resetLoopsSinceLastShot();
        world.addEntity(newPlayer);
    }

    private void fixTurningDEG(Entity player) {
        if (player.getRotation() < 0) {
            player.setRotation(player.getRotation()+360);
        } else if (player.getRotation() >= 360) {
            player.setRotation(player.getRotation()-360);
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
