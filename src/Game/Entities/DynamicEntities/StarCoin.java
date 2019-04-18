package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Images;

public class StarCoin extends Item {

    public StarCoin(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.starcoin);
    }

    @Override
    public void tick(){
    }

}