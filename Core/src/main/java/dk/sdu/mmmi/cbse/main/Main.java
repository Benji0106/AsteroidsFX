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

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage window) throws Exception {
        Text text = new Text(10, 20, "Destroyed asteroids: 0");
        Pane gameWindow = new Pane();
        gameWindow.setStyle("-fx-background-color: lightgray;");
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(text);

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


        render(gameWindow);

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();

    }

    private void render(Pane gameWindow) {
        new AnimationTimer() {
            private long then = 0;

            @Override
            public void handle(long now) {
                removeRedundantBullets();
                update();
                draw(gameWindow);
//                showWorldEntities();
                then = now;
                gameData.getKeys().update();
            }

        }.start();
    }

    private void showWorldEntities() {
        System.out.println("--------------------- World entities ---------------------");
        world.getEntities().forEach(entity -> System.out.println(entity));
        System.out.println("-------------------------- End ---------------------------");
    }

    private void update() {

        // Update
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
//            System.out.println(entityProcessorService);
            entityProcessorService.process(gameData, world);
        }
//        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
//            postEntityProcessorService.process(gameData, world);
//        }
    }

    private void removeRedundantBullets() {
        /** delete bullet if out of screen **/
        for (Entity bullet : world.getEntities(Bullet.class)) {
            if (bullet.getX() < -10 || bullet.getX() > gameData.getDisplayWidth()+10 || bullet.getY() < -10 || bullet.getY() > gameData.getDisplayHeight()+10) {
                polygons.remove(bullet);
                world.removeEntity(bullet);
//                System.out.println("Bullet: " + bullet + " is removed, because it is out of screen (" + bullet.getX() + ", " + bullet.getY() + ")");
            }
        }

//        Map<Entity, Polygon> relevantPolygons = new ConcurrentHashMap<>();
//        for (Entity entity : world.getEntities()) {
//            Polygon polygon = polygons.get(entity);
//            if (polygon != null) {
//                relevantPolygons.put(entity, polygon);
//            }
//        }
//        polygons = relevantPolygons;
    }

    private void draw(Pane gameWindow) {
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
//                System.out.println("Polygon not found for entity: " + entity);
                
                Polygon newPolygon = new Polygon(entity.getPolygonCoordinates());
                if (polygons.containsKey(entity)) {
                    polygons.replace(entity, newPolygon);
                } else polygons.put(entity, newPolygon);
//                System.out.println("Polygon created for entity: " + entity);
                gameWindow.getChildren().add(newPolygon);
//                System.out.println("Polygon: " + newPolygon);
            } else {
                polygon.setTranslateX(entity.getX());
                polygon.setTranslateY(entity.getY());
                polygon.setRotate(entity.getRotation());
            }
        }
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
//        System.out.println(ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList()));
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
