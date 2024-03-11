package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.ArrayList;
import java.util.Collection;

public class Asteroid extends Entity{

    // Rotating Attributes
    private boolean isRotating;
    private double rotationSpeed;
    private static final double minRotationSpeed = 2;
    private static final double maxRotationSpeed = 10;

    // Movement Attributes
    private double movingDirection;
    private static final double minSpeed = 1;
    private static final double maxSpeed = 4;

    // Other Attributes
    private AsteroidLevel asteroidLevel;
    private final Collection<Asteroid> familyAsteroids = new ArrayList<>();

    // Rotation Getters & Setters
    public boolean isRotating() {return isRotating;}
    public void setIsRotating(boolean isRotating) {this.isRotating = isRotating;}
    public double getRotationSpeed() {return rotationSpeed;}
    public void setRotationSpeed(double rotationSpeed) {this.rotationSpeed = rotationSpeed;}
    public static double getMinRotationSpeed() {return minRotationSpeed;}
    public static double getMaxRotationSpeed() {return maxRotationSpeed;}

    // Movement Getters & Setters
    public double getMovingDirection() {return movingDirection;}
    public void setMovingDirection(double movingDirection) {this.movingDirection = movingDirection;}
    public static double getMinSpeed() {return minSpeed;}
    public static double getMaxSpeed() {return maxSpeed;}

    // Other Getters & Setters
    public AsteroidLevel getAsteroidLevel() {return asteroidLevel;}
    public void setAsteroidLevel(AsteroidLevel asteroidLevel) {this.asteroidLevel = asteroidLevel;}
    public Collection<Asteroid> getFamilyAsteroids() {return familyAsteroids;}
}
