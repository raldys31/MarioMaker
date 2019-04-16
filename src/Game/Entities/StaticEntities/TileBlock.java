package Game.Entities.StaticEntities;

import Main.Handler;
import Resources.Images;

public class TileBlock extends BaseStaticEntity {

    public TileBlock(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height,handler, Images.tileBlock);
    }

}
