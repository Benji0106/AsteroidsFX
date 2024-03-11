package dk.sdu.mmmi.cbse.collisiondetectionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class CollisionDetectorControlSystem implements IPostEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {

        for (Entity e1 : world.getEntities()) {
            for (Entity e2 : world.getEntities()) {
                if (e1.getID().equals(e2.getID())) {
                    continue;
                }
                double difX = Math.abs(e1.getX() - e2.getX());
                double difY = Math.abs(e1.getY() - e2.getY());
                double realDistance = Math.sqrt(difX * difX + difY * difY);
                if (realDistance <= (e1.getSize() + e2.getSize())) {
                    e1.setHit(true);
                    e1.getHits().add(e2);
                }
            }
        }

    }

}
