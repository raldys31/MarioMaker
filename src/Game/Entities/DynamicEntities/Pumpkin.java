package Game.Entities.DynamicEntities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;

public class Pumpkin extends BaseDynamicEntity {

    public Animation anim;

    public Pumpkin(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.pumpkin[0]);
        anim = new Animation(160,Images.pumpkin);
    }

    @Override
    public void tick(){
    	super.tick();
    	anim.tick();
    }


    @Override
    public void kill() {
        ded=true;
    }
}
