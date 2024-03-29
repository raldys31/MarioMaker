package Game.GameStates;

import Display.UI.UIPointer;
import Game.Entities.DynamicEntities.BaseDynamicEntity;
import Game.Entities.StaticEntities.BaseStaticEntity;
import Game.World.MapBuilder;
import Main.GameSetUp;
import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;

import Display.UI.UIListener;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameState extends State {

    public GameState(Handler handler){
        super(handler);
        handler.getGame().pointer = new UIPointer(28 * MapBuilder.pixelMultiplier,197 * MapBuilder.pixelMultiplier,128,128,handler);

    }

    @Override
    public void tick() {
    	if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
    		State.setState(handler.getGame().pauseState);
    	} 
    	handler.getMario().tick();
    	
    	//Draw Luigi if multiplayer mode is selected
        if (handler.isMultiplayerMode() == true) {	
    		handler.getLuigi().tick();
    	}
    	if(handler.getMap().getListener() != null && MapBuilder.mapDone) {
    		handler.getMap().getListener().tick();
    		handler.getMap().getHand().tick();
    		handler.getMap().getWalls().tick();
    	}
    	for (BaseDynamicEntity entity:handler.getMap().getEnemiesOnMap()) {
    		entity.tick();
    	}
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        handler.getMap().drawMap(g2);
        
        //Draw on Luigi's screen
        if (handler.isMultiplayerMode() == true) {
        	Graphics2D g2L = (Graphics2D) handler.getGame().getgL().create();
        	handler.getMap().drawMapL(g2L);
        }
    }

}
