package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private Pane gameWindow;
    private Collection<? extends IGamePluginService> gamePlugins;
    private Collection<? extends IEntityProcessingService> entityProcessors;
    private Collection<? extends IPostEntityProcessingService> postEntityProcessors;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage window) throws Exception {

        gamePlugins = ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
        entityProcessors = ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
        postEntityProcessors = ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());

        gameWindow = new Pane();
        gameWindow.setStyle("-fx-background-color: lightgray;");
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());

//        Text text = new Text(10, 20, "Destroyed asteroids: 0");
//        gameWindow.getChildren().add(text);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        render();

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();

    }

    private void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
                removeRedundantEntities(world.getEntities());
            }
        }.start();
    }

    private void removeRedundantEntities(Collection<Entity> entityList) {
        for (Entity entity : entityList) {
            /* delete bullet if out of screen **/
            if (entity.isRedundant()) {
                gameWindow.getChildren().remove(polygons.get(entity));
                polygons.remove(entity);
                world.removeEntity(entity);
            }
        }
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                Polygon newPolygon = new Polygon(entity.getPolygonCoordinates());
                if (polygons.containsKey(entity)) {
                    polygons.replace(entity, newPolygon);
                } else polygons.put(entity, newPolygon);
                gameWindow.getChildren().add(newPolygon);
            } else {
                polygon.setTranslateX(entity.getX());
                polygon.setTranslateY(entity.getY());
                polygon.setRotate(entity.getRotation());
            }
        }
    }

    private void update() {
        // Update
        if (!getEntityProcessingServices().isEmpty()) {
            for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
                entityProcessorService.process(gameData, world);
            }
        }
        if (!getPostEntityProcessingServices().isEmpty()) {
            for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
                postEntityProcessorService.process(gameData, world);
            }
        }
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return gamePlugins;
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return entityProcessors;
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postEntityProcessors;
    }
}
