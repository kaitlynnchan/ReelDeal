package project.game.reeldeal.model;

import java.util.ArrayList;

/**
 * Game Configurations
 * Stores a collection of existing game configurations,
 *  all game items in the list are distinct
 */
public class GameConfigs {

    private ArrayList<Game> configs = new ArrayList<>();
    private int currentGameIndex;
    private int gamesStarted;

    // Singleton implementation of GameConfigs
    private static GameConfigs instance;
    private GameConfigs(){}
    public static GameConfigs getInstance(){
        if(instance == null){
            instance = new GameConfigs();
        }
        return instance;
    }

    public ArrayList<Game> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<Game> configs) {
        this.configs = configs;
    }

    public void setCurrentGameIndex(int currentGameIndex) {
        this.currentGameIndex = currentGameIndex;
    }

    public Game getCurrentGame(){
        return configs.get(currentGameIndex);
    }

    public int getGamesStarted() {
        return gamesStarted;
    }

    public void setGamesStarted(int gamesStarted) {
        this.gamesStarted = gamesStarted;
    }

    public void incrementGamesStarted() {
        this.gamesStarted++;
    }

    public Game get(int index){
        return configs.get(index);
    }

    public int getIndex(Game game){
        // Returns -1 if game does not exist in array
        if(configs == null){
            return -1;
        }

        for(int i = 0; i < configs.size(); i++){
            Game tempGame = configs.get(i);
            if(tempGame.getRows() == game.getRows()
                    && tempGame.getColumns() == game.getColumns()
                    && tempGame.getTotalFishes() == game.getTotalFishes()){
                return i;
            }
        }
        return -1;
    }

    public void add(Game game){
        configs.add(game);
    }
}
