package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;


public class EnemyControlSystem implements IEntityProcessingService {
    private boolean moving = false;
    private boolean inCriticalArea = false;
    private double turning = 0.5;
    private int loopsSinceLastShot = 0;

    /** Main Methods **/
    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            handleTurning(enemy);
            handleMovement(enemy);
            handleShooting(enemy, gameData, world);
            handlePlacement(enemy, gameData);
        }
    }


    /** 1. Step Methods **/
    private void handleTurning(Entity enemy) {
        if (!inCriticalArea) {
            randomIncreaseOrDecrease();
            stabilizeTurning();
            if (turning > 0.7) {
                turningRight(enemy);
            } else if (turning < 0.3) {
                turningLeft(enemy);
            }
            enemy.setRotation(fixTurningDEG(enemy.getRotation()));
        }
    }
    private void handleMovement(Entity enemy) {
        if (!inCriticalArea) {
            if (moving) {
                moveEnemy(enemy);
            }
            if ((Math.random() > 0.98 && moving) || (Math.random() > 0.85 && !moving)) {
                moving = !moving;
            }
        } else {
            moveEnemy(enemy);
        }
    }
    private void handleShooting(Entity enemy, GameData gameData, World world) {
        if (loopsSinceLastShot > 30 && Math.random() > 0.85) {
            loopsSinceLastShot = 0;
            if (getBulletSPIs().size() > 0) {
                world.addEntity(getBulletSPIs().stream().findFirst().orElse(null).createBullet(enemy, gameData));
            }
        } else {
            loopsSinceLastShot++;
        }
    }
    private void handlePlacement(Entity enemy, GameData gameData) {
        checkPlacement(enemy, gameData);
        if (inCriticalArea) {
            handleCriticalRotation(enemy, gameData);
        }
    }


    /** 2. Step - Turning Methods **/
    private void randomIncreaseOrDecrease() {
        if (Math.random() > 0.5) {
            turning += 0.1;
        } else {
            turning -= 0.1;
        }
    }
    private void stabilizeTurning() {
        if (turning > 1) {
            turning = 1;
        } else if (turning < 0) {
            turning = 0;
        }
    }
    private void turningRight(Entity entity) {
        entity.setRotation(entity.getRotation() + 3);
    }
    private void turningLeft(Entity entity) {
        entity.setRotation(entity.getRotation() - 3);
    }
    private double fixTurningDEG(double degree) {
        if (degree < 0) {
            while (degree < 0) {
                degree += 360;
            }
        } else if (degree >= 360) {
            while (degree >= 360) {
                degree -= 360;
            }
        }
        return degree;
    }


    /** 2. Step - Movement Methods **/
    private void moveEnemy(Entity enemy) {
        enemy.setY(enemy.getY() + enemy.getMovementSpeed()*Math.sin(Math.toRadians(enemy.getRotation())));
        enemy.setX(enemy.getX() + enemy.getMovementSpeed()*Math.cos(Math.toRadians(enemy.getRotation())));
    }


    /** 2. Step - Shooting Methods **/
    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }


    /** 2. Step - Placement Methods **/
    private void checkPlacement(Entity enemy, GameData gameData) {
        inCriticalArea = enemy.getX() < 50 || enemy.getX() > gameData.getDisplayWidth() - 50 || enemy.getY() < 50 || enemy.getY() > gameData.getDisplayHeight() - 50;
    }
    private void handleCriticalRotation(Entity enemy, GameData gameData) {
        int quadrant = calculateQuadrant(enemy, gameData);
        double optimalRotation = calculateOptimalRotation(enemy, gameData, quadrant);
        double currentRotation = (enemy.getRotation());
        turnTowardsOptimalRotation(enemy, optimalRotation, currentRotation);
        this.turning = 0.5;
    }


    /** 3. Step - Placement Methods **/
    private int calculateQuadrant(Entity enemy, GameData gameData) {
        double enemyRelativeX = (enemy.getX()) - ((double) gameData.getDisplayWidth() / 2);
        double enemyRelativeY = (enemy.getY()*(-1)) + ((double) gameData.getDisplayHeight() / 2);
        if (enemyRelativeX >= 0 && enemyRelativeY >= 0) {
            return 1;
        } else if (enemyRelativeX < 0 && enemyRelativeY >= 0) {
            return 2;
        } else if (enemyRelativeX < 0 && enemyRelativeY < 0) {
            return 3;
        } else if (enemyRelativeX >= 0 && enemyRelativeY < 0) {
            return 4;
        }
        return 0;
    }
    private double calculateOptimalRotation(Entity enemy, GameData gameData, int quadrant) {
        double enemyRelativeX = (enemy.getX()) - ((double) gameData.getDisplayWidth() / 2);
        double enemyRelativeY = (enemy.getY()*(-1)) + ((double) gameData.getDisplayHeight() / 2);
        double angleDEG = (enemy.getRotation() +180);
        double angle = Math.toDegrees(Math.asin(enemyRelativeY / Math.sqrt(enemyRelativeX * enemyRelativeX + enemyRelativeY * enemyRelativeY)));

        switch (quadrant) {
            case 1:
            case 4:
                angleDEG = 180 - angle;
                break;
            case 2:
                angleDEG = angle;
                break;
            case 3:
                angleDEG = 360 + angle;
                break;
            default:
                System.out.println("Error in calculateOptimalRotation");
                break;
        }

        angleDEG = fixTurningDEG(angleDEG);
        return angleDEG;
    }
    private void turnTowardsOptimalRotation(Entity enemy, double optimalRotation, double currentRotation) {
        if (isBetween(currentRotation, optimalRotation-20, optimalRotation+20)) {
            return;
        }

        if (isBetween(optimalRotation, (currentRotation -360), (currentRotation -360)+180) ||
                isBetween(optimalRotation, currentRotation, currentRotation +180) ||
                isBetween(optimalRotation, (currentRotation +360), (currentRotation +360)+180)) {
            turningRight(enemy);
        } else if (isBetween(optimalRotation, (currentRotation -360)-180, (currentRotation -360)) ||
                isBetween(optimalRotation, currentRotation -180, currentRotation) ||
                isBetween(optimalRotation, (currentRotation +360)-180, (currentRotation +360))) {
            turningLeft(enemy);
        } else System.out.println(
            "-----\n" +
            "Not turning\n" +
            "optimalRotation: " + optimalRotation + "\n" +
            "currentRotation: " + currentRotation + "\n" +
            "-----");
    }
    private boolean isBetween(double angle, double lowNum, double highNum) {
        return angle >= lowNum && angle <= highNum;
    }


}
