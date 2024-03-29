package Game.Entities.DynamicEntities;

import Game.Entities.EntityBase;
import Game.Entities.StaticEntities.BaseStaticEntity;
import Game.Entities.StaticEntities.BoundBlock;
import Game.Entities.StaticEntities.TileBlock;
import Game.GameStates.State;
import Main.Handler;
import Resources.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends BaseDynamicEntity {

    protected double velX,velY;

    public String facing = "Left";
    public boolean moving = false;
    public Animation playerSmallLeftAnimation,playerSmallRightAnimation,playerBigLeftWalkAnimation,playerBigRightWalkAnimation,playerBigLeftRunAnimation,playerBigRightRunAnimation;
    public boolean falling = true, jumping = false, isBig = false, gotStarCoin = false, running = false, changeDirrection = false;
    public double gravityAcc = 0.38;
    int changeDirectionCounter=0;

    public Player(int x, int y, int width, int height, Handler handler, BufferedImage sprite,Animation PSLA,Animation PSRA,Animation PBLWA,Animation PBRWA,Animation PBLRA,Animation PBRRA) {
        super(x, y, width, height, handler, sprite);
        playerBigLeftRunAnimation=PBLRA;
        playerBigLeftWalkAnimation=PBLWA;
        playerBigRightRunAnimation=PBRRA;
        playerBigRightWalkAnimation=PBRWA;
        playerSmallLeftAnimation=PSLA;
        playerSmallRightAnimation=PSRA;
    }

    @Override
    public void tick(){

        if (changeDirrection) {
            changeDirectionCounter++;
        }
        if(changeDirectionCounter>=10){
            changeDirrection=false;
            changeDirectionCounter=0;
        }

        checkBottomCollisions();
        checkMarioHorizontalCollision();
        checkTopCollisions();
        checkItemCollision();
        enemyCollision();
        boundBricksColl();
        if(!isBig) {
            if (facing.equals("Left") && moving) {
                playerSmallLeftAnimation.tick();
            } else if (facing.equals("Right") && moving) {
                playerSmallRightAnimation.tick();
            }
        }else{
            if (facing.equals("Left") && moving && !running) {
                playerBigLeftWalkAnimation.tick();
            } else if (facing.equals("Left") && moving && running) {
                playerBigLeftRunAnimation.tick();
            } else if (facing.equals("Right") && moving && !running) {
                playerBigRightWalkAnimation.tick();
            } else if (facing.equals("Right") && moving && running) {
                playerBigRightRunAnimation.tick();
            }
        }
    }

    private void checkItemCollision() {

        for (BaseDynamicEntity entity : handler.getMap().getEnemiesOnMap()) {
            if (entity != null && getBounds().intersects(entity.getBounds()) && entity instanceof Mushroom && !isBig) {
                isBig = true;
                this.y -= 8;
                this.height += 8;
                setDimension(new Dimension(width, this.height));
                ((Item) entity).used = true;
                entity.y = -100000;
            }
            else if (entity != null && getBounds().intersects(entity.getBounds()) && entity instanceof StarCoin && !gotStarCoin) {
            	 gotStarCoin = true;
                 ((Item) entity).used = true;                
            }
        }
    }

    public void checkBottomCollisions() {
        Player mario = this;
        ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamicEntity> enemies =  handler.getMap().getEnemiesOnMap();

        Rectangle marioBottomBounds =getBottomBounds();

        if (!mario.jumping) {
            falling = true;
        }

        for (BaseStaticEntity brick : bricks) {
            Rectangle brickTopBounds = brick.getTopBounds();
            if (marioBottomBounds.intersects(brickTopBounds)) {
                mario.setY(brick.getY() - mario.getDimension().height + 1);
                falling = false;
                velY=0;
            }
        }

        for (BaseDynamicEntity enemy : enemies) {
            Rectangle enemyTopBounds = enemy.getTopBounds();
            if (marioBottomBounds.intersects(enemyTopBounds) && !(enemy instanceof Item)) {
                if(!enemy.ded) {
                    handler.getGame().getMusicHandler().playStomp();
                }
                enemy.kill();
                falling=false;
                velY=0;

            }
        }
    }

    public void checkTopCollisions() {
        Player mario = this;
        ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();

        Rectangle marioTopBounds = mario.getTopBounds();
        for (BaseStaticEntity brick : bricks) {
            Rectangle brickBottomBounds = brick.getBottomBounds();
            if (marioTopBounds.intersects(brickBottomBounds)) {
                velY=0;
                mario.setY(brick.getY() + brick.height);
            }
        }
    }

    public void checkMarioHorizontalCollision(){
        Player mario = this;
        ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamicEntity> enemies = handler.getMap().getEnemiesOnMap();

        boolean marioDies = false;
        boolean toRight = moving && facing.equals("Right");

        Rectangle marioBounds = toRight ? mario.getRightBounds() : mario.getLeftBounds();

        for (BaseStaticEntity brick : bricks) {
            Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
            if (marioBounds.intersects(brickBounds)) {
                velX=0;
                if(toRight)
                    mario.setX(brick.getX() - mario.getDimension().width);
                else
                    mario.setX(brick.getX() + brick.getDimension().width);
            }
        }

        for(BaseDynamicEntity enemy : enemies){
            Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (marioBounds.intersects(enemyBounds)) {
                marioDies = true;
                break;
            }
        }

        if(marioDies) {
            handler.getMap().reset();
        }
    }
    
  //Check if Mario intersects with enemy, if it does, Mario dies.
    public void enemyCollision() {
    	boolean marioDies = false;
    	Player mario = this;
    	ArrayList<BaseDynamicEntity> enemies = handler.getMap().getEnemiesOnMap();
    	
    	boolean toRight = moving && facing.equals("Right");
        boolean toLeft = moving && facing.equals("Left");
        
        Rectangle marioBoundsR = toRight ? mario.getRightBounds() : mario.getLeftBounds();
        Rectangle marioBoundsL = toLeft ? mario.getLeftBounds() : mario.getRightBounds();
        
        for(BaseDynamicEntity enemy : enemies){
        	if(!(enemy instanceof Item)) {
        		Rectangle enemyBoundsR = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
        		Rectangle enemyBoundsL = !toLeft ? enemy.getLeftBounds() : enemy.getRightBounds();

        		if (marioBoundsR.intersects(enemyBoundsR) || marioBoundsL.intersects(enemyBoundsL) ) {
        			marioDies = true;
        			State.setState(handler.getGame().gameOverState);
        			break;
        		}
        	}
        }
        
        if(marioDies) {
            handler.getMap().reset();
        }
        
    }

//Checks if Mario touches bound bricks
    public void boundBricksColl() {
    	Player mario = this;
    	boolean marioDies = false;
        ArrayList<BaseStaticEntity> bricks = handler.getMap().getBlocksOnMap();
        
        Rectangle marioBounds = mario.getBounds();

        for (BaseStaticEntity brick : bricks) {
            Rectangle brickTopBounds = brick.getTopBounds();
            if (marioBounds.intersects(brickTopBounds) && brick instanceof BoundBlock) {
            	marioDies = true;
            	if (handler.isSingleplayerMode()) {
                State.setState(handler.getGame().gameOverState);
            	}
                break;
            }
        }
        
        if(marioDies) {
            handler.getMap().reset();
        }
    }

    public void jump() {
        if(!jumping && !falling){
            jumping=true;
            velY=10;
            handler.getGame().getMusicHandler().playJump();
        }
    }

    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }


}
