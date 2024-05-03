package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * The IGamePluginService is used to start/setup and stop/shut down game plugins if needed.
 */
public interface IGamePluginService {

    /**
     * Start/set up the game plugin
     * @param gameData Provides access to the game data such as window size and GameKeys.
     * @param world Provides access a map of all entities in the game.
     */
    void start(GameData gameData, World world);

    /**
     * Stop/shut down the game plugin
     * @param gameData Provides access to the game data such as window size and GameKeys.
     * @param world Provides access a map of all entities in the game.
     */
    void stop(GameData gameData, World world);

}
