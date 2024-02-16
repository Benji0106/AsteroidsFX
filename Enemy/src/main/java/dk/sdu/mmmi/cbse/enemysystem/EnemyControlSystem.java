package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import static dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin.createEnemyShip;

public class EnemyControlSystem implements IEntityProcessingService {
    private static Boolean moving = false;
    private static double turning = 0.5;
    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            if (moving) {
                moveEnemy(enemy);
            }
            if ((Math.random() > 0.95 && moving) || (Math.random() > 0.65 && !moving)) {
                moving = !moving;
            }

            if (turning > 0.7) {
                turningRight(enemy);
            } else if (turning < 0.3) {
                turningLeft(enemy);
            }
            randomIncreaseOrDecrease();
            stabilizeTurning();

        }
    }

    private void stabilizeTurning() {
        if (turning > 1) {
            turning = 1;
        } else if (turning < 0) {
            turning = 0;
        }
    }
    private void randomIncreaseOrDecrease() {
        if (Math.random() > 0.5) {
            turning += 0.1;
        } else {
            turning -= 0.1;
        }
    }
    private void moveEnemy(Entity enemy) {
        enemy.setY(enemy.getY() + 2*Math.sin(Math.toRadians(enemy.getRotation())));
        enemy.setX(enemy.getX() + 2*Math.cos(Math.toRadians(enemy.getRotation())));
    }

    private void turningRight(Entity entity) {
        entity.setRotation(entity.getRotation() + 3);
    }

    private void turningLeft(Entity entity) {
        entity.setRotation(entity.getRotation() - 3);
    }


}
