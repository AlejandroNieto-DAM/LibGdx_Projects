package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Tiraco extends Image {
    TextureRegion stand, jump;
    Animation walk;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;
    Texture koalaTexture;
    
    float positionToComeX = 0;
    float positionToComeY = 0;
    
    boolean parar = false;
    

    Boolean cambioDireccion;
    
    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 5f;
    final float DAMPING = 0.87f;

    public Tiraco() {
        cambioDireccion = false;
        final float width = 18;
        final float height = 26;
        this.setSize(1, height / width);

        koalaTexture = new Texture("bala.png");
        
    
    }
    
    public void positionToCome(float x, float y){
       
           
        if(x == 0 && y == 0){
            positionToComeX = 0;
            positionToComeY = 0;
        } else {
             positionToComeX = this.getX() - ((this.getX() - x) / 15) ;
            positionToComeY = this.getY() - ((this.getY() - y) / 15);
        }
       
       
       

    }

    public void act(float delta) {
        time = time + delta;
        
        
        float x;
        float y;  

        //yVelocity = yVelocity + GRAVITY;

        if(positionToComeX != 0){
           x = positionToComeX;
           y = positionToComeY;  
        } else {
           x = this.getX();
           y = this.getY();
        }
        
        
        
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        xVelocity = -1 * MAX_VELOCITY;
        
        if (canMoveTo(x + xChange, y, false) == false) {
            xVelocity = xChange = 0;
            if(!canMoveTo(x + xChange, y, false)){
                parar = true;
            }
        }

        if (canMoveTo(x, y + yChange, yVelocity > 0) == false) {
            canJump = yVelocity < 0;
            yVelocity = yChange = 0;
            if(!canMoveTo(x, y + yChange, yVelocity > 0)){
                parar = true;
            }
        }

        this.setPosition(x + xChange, y + yChange);

        xVelocity = xVelocity * DAMPING;
        if (Math.abs(xVelocity) < 0.5f) {
            xVelocity = 0;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        
        //if(!parar){
            if (isFacingRight) {
                batch.draw(koalaTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            } else {
                batch.draw(koalaTexture, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
            } 
        //}
        
        
    }

    private boolean canMoveTo(float startX, float startY, boolean shouldDestroy) {
        float endX = startX + this.getWidth();
        float endY = startY + this.getHeight();

        int x = (int) startX;
        while (x < endX) {

            int y = (int) startY;
            while (y < endY) {
                if (layer.getCell(x, y) != null) {
                    if (shouldDestroy) {
                        layer.setCell(x, y, null);
                    }
                    return false;
                }
                y = y + 1;
            }
            x = x + 1;
        }

        return true;
    }
}
