package project.game.reeldeal.model;

import java.util.ArrayList;

/**
 * Game Configurations
 * Stores a collection of existing game configurations
 *      fishesManager items in the list are distinct
 */
public class GameConfigs {

    private ArrayList<FishesManager> configs = new ArrayList<>();
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

    public ArrayList<FishesManager> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<FishesManager> configs) {
        this.configs = configs;
    }

    public void setCurrentGameIndex(int currentGameIndex) {
        this.currentGameIndex = currentGameIndex;
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

    public int getIndex(FishesManager manager){
        // Returns -1 if manager does not exist in array
        if(configs == null){
            return -1;
        }

        for(int i = 0; i < configs.size(); i++){
            FishesManager l = configs.get(i);
            if(l.getRows() == manager.getRows()
                    && l.getCols() == manager.getCols()
                    && l.getTotalFishes() == manager.getTotalFishes()){
                return i;
            }
        }
        return -1;
    }

    public FishesManager get(int index){
        return configs.get(index);
    }

    public FishesManager getCurrentGame(){
        return configs.get(currentGameIndex);
    }

    public void add(FishesManager manager){
        configs.add(manager);
    }

}
