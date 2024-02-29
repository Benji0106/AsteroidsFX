package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidControlSystem implements IEntityProcessingService {

    private static int asteroidCount = 0;
    private static final int MaxAsteroidAmount = 5;

    @Override
    public void process(GameData gameData, World world) {

        if (asteroidCount < MaxAsteroidAmount && Math.random() > 0.95) {
            Asteroid asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
            asteroidCount++;
            System.out.println("Asteroid created - " + asteroidCount);
        }

        for (Asteroid asteroid : world.getEntities(Asteroid.class)) {
            if (asteroid.isRotating()) {
                asteroid.setRotation(asteroid.getRotation() + asteroid.getRotationSpeed());
            }
            asteroid.setX(asteroid.getX() + asteroid.getMovementSpeed()*Math.cos(Math.toRadians(asteroid.getMovingDirection())));
            asteroid.setY(asteroid.getY() + asteroid.getMovementSpeed()*Math.sin(Math.toRadians(asteroid.getMovingDirection())));
            if (asteroid.getX() < -110 ||
                    asteroid.getX() > gameData.getDisplayWidth() + 110 ||
                    asteroid.getY() < -110 ||
                    asteroid.getY() > gameData.getDisplayHeight() + 110) {
                asteroid.setRedundant(true);
                asteroidCount--;
            }
        }
    }

    private Asteroid createAsteroid(GameData gameData) {
        Asteroid newAsteroid = new Asteroid();
//        (radius 7) Small (0, 7.349, 6.157, 2.928, 3.232, -6.123, -3.232, -6.123, -6.157, 2.928)
//        (radius 11) Medium (0, 11.54, 9.652, 3.726, 5.091, -9.359, -5.091, -9.359, -9.652, 3.726)
//        (radius 16) Large (0, 17.086, 14.311, 5.493, 7.538, -13.84, -7.538, -13.84, -14.311, 5.493)


//        newAsteroid.setPolygonCoordinates(0, 8, -4.7, 6.47, -7.61, 2.47, -7.61, -2.47, -4.7, -6.47, 0, -8, 4.7, -6.47, 7.61, -2.47, 7.61, 2.47, 4.7, 6.47);
        newAsteroid.setPolygonCoordinates(0.0, 12.0, -7.05, 9.705, -11.415, 3.705, -11.415, -3.705, 7.05, -9.705, 0.0, -12.0, -7.05, -9.705, 11.415, -3.705, 11.415, 3.705, 7.05, 9.705);
        newAsteroid.setSize(8);

        if (Math.random() > 0.2) {
            newAsteroid.setIsRotating(true);
            newAsteroid.setRotationSpeed(Asteroid.getMinRotationSpeed() + Math.random()*(Asteroid.getMaxRotationSpeed()-Asteroid.getMinRotationSpeed()));
        } else { newAsteroid.setIsRotating(false); }

        setXAndY(newAsteroid);
        newAsteroid.setMovingDirection(getOptimalRotation(newAsteroid, gameData));
        newAsteroid.setMovementSpeed(Asteroid.getMinSpeed() + (Math.random()*(Asteroid.getMaxSpeed()-Asteroid.getMinSpeed())));
        return newAsteroid;
    }

    private static void setXAndY(Asteroid newAsteroid) {
        double randomCoordinateNumber1 = Math.random() * 1000-100;
        double randomCoordinateNumber2 = Math.random() * 80;
        if (new Random().nextBoolean()) {
            randomCoordinateNumber2 -= 100;
        } else {
            randomCoordinateNumber2 += 820;
        }
        if (new Random().nextBoolean()) {
            newAsteroid.setX(randomCoordinateNumber1);
            newAsteroid.setY(randomCoordinateNumber2);
        } else {
            newAsteroid.setX(randomCoordinateNumber2);
            newAsteroid.setY(randomCoordinateNumber1);
        }
    }

    private double getOptimalRotation(Asteroid asteroid, GameData gameData) {
        int quadrant = calculateQuadrant(asteroid, gameData);
        double optimalRotation = calculateOptimalRotation(asteroid, gameData, quadrant);
        double deviation = (Math.random() * 120)-60;
        return optimalRotation + deviation;
    }

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
}