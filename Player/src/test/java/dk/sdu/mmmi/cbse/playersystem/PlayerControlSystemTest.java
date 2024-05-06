package dk.sdu.mmmi.cbse.playersystem;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerControlSystemTest {

    @Test
    public void PayerShouldMoveForwardWithRotation0() {
        // Arrange
        Player player = new Player();
        player.setX(30);
        player.setY(30);
        player.setMovementSpeed(1);
        player.setRotation(0);
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();

        // Act
        playerControlSystem.moveForward(player);

        // Assert
        assertEquals(31, player.getX());
        assertEquals(30, player.getY());
    }

    @Test
    public void PayerShouldMoveForwardWithRotation90() {
        // Arrange
        Player player = new Player();
        player.setX(30);
        player.setY(30);
        player.setMovementSpeed(1);
        player.setRotation(90);
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();

        // Act
        playerControlSystem.moveForward(player);

        // Assert
        assertEquals(30, player.getX());
        assertEquals(31, player.getY());
    }

    @Test
    public void PayerShouldMoveForwardWithRotation180() {
        // Arrange
        Player player = new Player();
        player.setX(30);
        player.setY(30);
        player.setMovementSpeed(1);
        player.setRotation(180);
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();

        // Act
        playerControlSystem.moveForward(player);

        // Assert
        assertEquals(29, player.getX());
        assertEquals(30, player.getY());
    }

    @Test
    public void PayerShouldMoveForwardWithRotation270() {
        // Arrange
        Player player = new Player();
        player.setX(30);
        player.setY(30);
        player.setMovementSpeed(1);
        player.setRotation(270);
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();

        // Act
        playerControlSystem.moveForward(player);

        // Assert
        assertEquals(30, player.getX());
        assertEquals(29, player.getY());
    }

    @Test
    public void PayerShouldTurnLeft() {
        // Arrange
        Player player = new Player();
        player.setRotation(45);
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();

        // Act
        playerControlSystem.turnLeft(player);

        // Assert
        assertEquals(40, player.getRotation());
    }

    @Test
    public void PayerShouldTurnRight() {
        // Arrange
        Player player = new Player();
        player.setRotation(45);
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();

        // Act
        playerControlSystem.turnRight(player);

        // Assert
        assertEquals(50, player.getRotation());
    }

}