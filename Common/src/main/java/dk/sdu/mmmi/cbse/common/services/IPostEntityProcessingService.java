package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for the PostEntityProcessingServices. Used to process Services that needs processing after IEntityProcessingService.
 */
public interface IPostEntityProcessingService {

    /**
     * Method to process the PostEntityProcessingService
     * @param gameData Provides access to the game data such as window size and GameKeys.
     * @param world Provides access a map of all entities in the game.
     */
    void process(GameData gameData, World world);
}
