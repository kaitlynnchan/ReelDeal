package cmpt276.assign3.assign3game.model;

import java.util.ArrayList;

public class GameConfig {

    private ArrayList<ItemsManager> config = new ArrayList<>();

    // Singleton implementation of ItemsManager
    private static GameConfig instance;
    private GameConfig(){}
    public static GameConfig getInstance(){
        if(instance == null){
            instance = new GameConfig();
        }
        return instance;
    }

    public ArrayList<ItemsManager> getConfig() {
        return config;
    }

    public ItemsManager get(int index){
        return config.get(index);
    }

    public int getIndex(ItemsManager manager){
        for(int i = 0; i < config.size(); i++){
            ItemsManager l = config.get(i);
            if(l.getRows() == manager.getRows()
                    && l.getCols() == manager.getCols()
                    && l.getTotalItems() == manager.getTotalItems()){
                return i;
            }
        }
        return -1;
    }

    public void setConfig(ArrayList<ItemsManager> config) {
        this.config = config;
    }

    public void add(ItemsManager manager){
        config.add(manager);
    }

    public boolean isThere(ItemsManager manager){
        if(config == null){
            return false;
        }
        for(ItemsManager l : config){
            if(l.getRows() == manager.getRows()
                    && l.getCols() == manager.getCols()
                    && l.getTotalItems() == manager.getTotalItems()){
                return true;
            }
        }
        return false;
    }
}
