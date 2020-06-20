package cmpt276.assign3.assign3game.model;

import java.util.ArrayList;

public class GameConfigs {

    private ArrayList<ItemsManager> configs = new ArrayList<>();

    // Singleton implementation of ItemsManager
    private static GameConfigs instance;
    private GameConfigs(){}
    public static GameConfigs getInstance(){
        if(instance == null){
            instance = new GameConfigs();
        }
        return instance;
    }

    public ArrayList<ItemsManager> getConfigs() {
        return configs;
    }

    public ItemsManager get(int index){
        return configs.get(index);
    }

    public int getIndex(ItemsManager manager){
        if(configs == null){
            return -1;
        }
        for(int i = 0; i < configs.size(); i++){
            ItemsManager l = configs.get(i);
            if(l.getRows() == manager.getRows()
                    && l.getCols() == manager.getCols()
                    && l.getTotalItems() == manager.getTotalItems()){
                return i;
            }
        }
        return -1;
    }

    public void setConfigs(ArrayList<ItemsManager> configs) {
        this.configs = configs;
    }

    public void add(ItemsManager manager){
        configs.add(manager);
    }

}
