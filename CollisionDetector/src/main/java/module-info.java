import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module CollisionDetector {
    exports dk.sdu.mmmi.cbse.collisiondetectionsystem;
    requires Common;
    provides IGamePluginService with dk.sdu.mmmi.cbse.collisiondetectionsystem.CollisionDetectorPlugin;
    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisiondetectionsystem.CollisionDetectorControlSystem;
}
