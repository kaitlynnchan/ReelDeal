package cmpt276.assign3.assign3game.model;

import java.util.ArrayList;

public class GameConfig {

    private ArrayList<ItemsManager> config;

    // Singleton implementation of ItemsManager
    private static GameConfig instance;
    private GameConfig(){}
    public static GameConfig getInstance(){
        if(instance == null){
            instance = new GameConfig();
        }
        return instance;
    }

    public void add(ItemsManager manager){
//        if(!isThere(manager)){
            config.add(manager);
//        }
    }

    public boolean isThere(ItemsManager manager){
        if(config == null){
            return false;
        }
        for(ItemsManager l : config){
            if(l == manager){
                return true;
            }
        }
        return false;
    }
}
