package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for the EntityProcessingServices. Used to process entities in the world
 */
public interface IEntityProcessingService {

    /**
     * Process the entities in the world
     * @param gameData Provides access to the game data such as window size and GameKeys.
     * @param world Provides access a map of all entities in the game.
     */
    void process(GameData gameData, World world);
}
