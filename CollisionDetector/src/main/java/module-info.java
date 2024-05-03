import dk.sdu.mmmi.cbse.collisiondetectionsystem.CollisionDetectionSystem;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module CollisionDetector {
    exports dk.sdu.mmmi.cbse.collisiondetectionsystem;
    requires Common;
    provides IPostEntityProcessingService with CollisionDetectionSystem;
}
