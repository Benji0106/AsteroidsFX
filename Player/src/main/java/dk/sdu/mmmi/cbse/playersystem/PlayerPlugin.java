package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
public class PlayerPlugin implements IGamePluginService {
    @Override
    public void start(GameData gameData, World world) {
        // Add entity to the world
        world.addEntity(createPlayerShip(gameData));
    }

    private Entity createPlayerShip(GameData gameData) {
        Entity playerShip = new Player();
        playerShip.setSize(5);
        playerShip.setMovementSpeed(2);
        playerShip.setPolygonCoordinates(12, -1, 8, -1, 8, -3, 6, -3, 6, -5, -2, -5, -2, -7, 0, -7, 0, -9, -10, -9, -10, -5, -8, -5, -8, -3, -6, -3, -6, -1, -10, -1, -10, 1, -6, 1, -6, 3, -8, 3, -8, 5, -10, 5, -10, 9, 0, 9, 0, 7, -2, 7, -2, 5, 2, 5, 2, 1, 4, 1, 4, -1, 2, -1, 2, -3, 4, -3, 4, -1, 6, -1, 6, 1, 4, 1, 4, 3, 2, 3, 2, 5, 6, 5, 6, 3, 8, 3, 8, 1, 12, 1); // double-sized Spaceship shape
        playerShip.setX(gameData.getDisplayHeight()/2);
        playerShip.setY(gameData.getDisplayWidth()/2);
        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("Player removed from the world");
        for (Entity player : world.getEntities(Player.class)) {
            world.removeEntity(player);
        }
    }

}
