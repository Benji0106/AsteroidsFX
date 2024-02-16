package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {
    private Entity enemy;
    public EnemyPlugin() {
    }
    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    public static Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Enemy();
        enemyShip.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        enemyShip.setRotation(Math.random() * 360);
        do {
            enemyShip.setX((Math.random() * (gameData.getDisplayWidth()-100))+50);
            enemyShip.setY((Math.random() * (gameData.getDisplayHeight()-100))+50);
        } while ((enemyShip.getX() > (gameData.getDisplayWidth()/2)-100 &&
                 enemyShip.getX() < (gameData.getDisplayWidth()/2)+100) &&
                 (enemyShip.getY() > (gameData.getDisplayHeight()/2)-100 &&
                 enemyShip.getY() < (gameData.getDisplayHeight()/2)+100));
        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {

    }
}
