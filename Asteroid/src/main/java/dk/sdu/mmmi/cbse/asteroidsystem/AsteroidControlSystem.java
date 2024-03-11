package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class AsteroidControlSystem implements IEntityProcessingService {

    private static int asteroidCount = 0;
    private static final int MaxAsteroidAmount = 4;

    @Override
    public void process(GameData gameData, World world) {
        spawnNewAsteroidsRandomly(gameData, world);
        for (Asteroid astroid : world.getEntities(Asteroid.class)) {
            handleAsteroidRotation(astroid);
            handleAsteroidMovement(astroid);
            handleAsteroidOutOfBounds(astroid, gameData);
            handleAsteroidIsHit(astroid, world);
        }
    }

    private void spawnNewAsteroidsRandomly(GameData gameData, World world) {
        if (asteroidCount < MaxAsteroidAmount && Math.random() > 0.95) {
            world.addEntity(createRandomAsteroid(gameData));
            asteroidCount++;
        }
    }

    private void handleAsteroidMovement(Asteroid asteroid) {
        asteroid.setX(asteroid.getX() + asteroid.getMovementSpeed()*Math.cos(Math.toRadians(asteroid.getMovingDirection())));
        asteroid.setY(asteroid.getY() + asteroid.getMovementSpeed()*Math.sin(Math.toRadians(asteroid.getMovingDirection())));
    }

    private void handleAsteroidRotation(Asteroid asteroid) {
        if (asteroid.isRotating()) {
            asteroid.setRotation(asteroid.getRotation() + asteroid.getRotationSpeed());
        }
    }

    private void handleAsteroidIsHit(Asteroid asteroid, World world) {
        if (asteroid.isHit()) {

            // removes family from hits list
            asteroid.getHits().removeIf(hit -> asteroid.getFamilyAsteroids().contains(hit));
            if (asteroid.getHits().isEmpty()) {
                asteroid.setHit(false);
                return;
            }

            asteroid.setRedundant(true);
            asteroidCount--;

            if (asteroid.getAsteroidLevel()!=AsteroidLevel.SMALL) {
                splitAsteroid(asteroid, world, asteroid);
            }

        }
    }

    private void splitAsteroid(Asteroid asteroid, World world, Asteroid parentAsteroid) {
        ArrayList<Asteroid> children = new ArrayList<>();

        if (new Random().nextBoolean()) {
            children.add(createChildAsteroid(asteroid));
            children.add(createChildAsteroid(asteroid));
            children.add(createChildAsteroid(asteroid));
        } else {
            children.add(createChildAsteroid(asteroid));
            children.add(createChildAsteroid(asteroid));
        }

        for (Asteroid child : children) {
            for (Asteroid sibling : children) {
                if (child != sibling) {
                    child.getFamilyAsteroids().add(sibling);
                }
            }
            child.getFamilyAsteroids().add(parentAsteroid);
        }

        for (Asteroid child : children) {
            world.addEntity(child);
            asteroidCount++;
        }
    }

    private void handleAsteroidOutOfBounds(Asteroid asteroid, GameData gameData) {
        if (asteroid.getX() < -110 ||
                asteroid.getX() > gameData.getDisplayWidth() + 110 ||
                asteroid.getY() < -110 ||
                asteroid.getY() > gameData.getDisplayHeight() + 110) {
            asteroid.setRedundant(true);
            asteroidCount--;
        }
    }

    private Asteroid createChildAsteroid(Asteroid parentAsteroid) {
        Asteroid childAsteroid = new Asteroid();

        switch (parentAsteroid.getAsteroidLevel()) {
            case LARGE:
                childAsteroid.setAsteroidLevel(AsteroidLevel.MEDIUM);
                childAsteroid.setPolygonCoordinates(0, 11.54, 9.652, 3.726, 5.091, -9.359, -5.091, -9.359, -9.652, 3.726); // (radius 11 - medium)
                childAsteroid.setSize(11);
                break;
            case MEDIUM:
                childAsteroid.setAsteroidLevel(AsteroidLevel.SMALL);
                childAsteroid.setPolygonCoordinates(0, 7.349, 6.157, 2.928, 3.232, -6.123, -3.232, -6.123, -6.157, 2.928); // (radius 7 - small)
                childAsteroid.setSize(7);
                break;
        }

        childAsteroid.setIsRotating(parentAsteroid.isRotating());
        childAsteroid.setRotationSpeed(parentAsteroid.getRotationSpeed()+(Math.random()*6)-3);

        childAsteroid.setMovingDirection(parentAsteroid.getMovingDirection()+(Math.random()*60)-30);
        childAsteroid.setMovementSpeed(parentAsteroid.getMovementSpeed()+(Math.random()*4)-2);

        childAsteroid.setX(parentAsteroid.getX());
        childAsteroid.setY(parentAsteroid.getY());

        childAsteroid.setHit(false);

        return childAsteroid;
    }

    private Asteroid createRandomAsteroid(GameData gameData) {
        Asteroid newAsteroid = new Asteroid();

        if (new Random().nextBoolean()) {
            newAsteroid.setAsteroidLevel(AsteroidLevel.LARGE);
            newAsteroid.setPolygonCoordinates(0, 17.086, 14.311, 5.493, 7.538, -13.84, -7.538, -13.84, -14.311, 5.493); // (radius 16 - large)
            newAsteroid.setSize(16);
        } else {
            newAsteroid.setAsteroidLevel(AsteroidLevel.MEDIUM);
            newAsteroid.setPolygonCoordinates(0, 11.54, 9.652, 3.726, 5.091, -9.359, -5.091, -9.359, -9.652, 3.726); // (radius 11 - medium)
            newAsteroid.setSize(11);
        }

        if (Math.random() > 0.2) {
            newAsteroid.setIsRotating(true);
            newAsteroid.setRotationSpeed(Asteroid.getMinRotationSpeed() + Math.random()*(Asteroid.getMaxRotationSpeed()-Asteroid.getMinRotationSpeed()));
        } else { newAsteroid.setIsRotating(false); }

        setXAndY(newAsteroid);
        newAsteroid.setMovingDirection(getOptimalRotation(newAsteroid, gameData));
        newAsteroid.setMovementSpeed(Asteroid.getMinSpeed() + (Math.random()*(Asteroid.getMaxSpeed()-Asteroid.getMinSpeed())));
        return newAsteroid;
    }

    private void setXAndY(Asteroid newAsteroid) {
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