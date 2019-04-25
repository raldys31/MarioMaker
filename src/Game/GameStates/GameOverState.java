package Game.GameStates;

import Display.UI.UIStringButton;
import Main.Handler;
import Resources.Images;
import Display.UI.UIManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameOverState extends State {

    private UIManager uiManager;

    public GameOverState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIStringButton(50, 200, 128, 64, "Restart", () -> {
            handler.getMouseManager().setUimanager(null);
            handler.setIsInMap(false);
            State.setState(handler.getGame().menuState);
        },handler,Color.RED));

    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.GameOver,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);
    }
}
