package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class FlyTortoise extends Image {
    
    private final int NORMAL = 1;
    private final int HITTED = 2;
    private final int DEAD = 3;
    
    TextureRegion stand, jump;
    Animation walk;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;
    TextureRegion koalaTexture;
    
    float positionToComeX = 0;
    float positionToComeY = 0;
    
    boolean parar = false;
    

    Boolean cambioDireccion;
    
    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 5f;
    final float DAMPING = 0.87f;
    
    
    public int state = NORMAL;

    public FlyTortoise() {
        cambioDireccion = false;
        final float width = 16;
        final float height = 32;
        this.setSize(1, height / width);
        
        
        Texture e = new Texture("nes.png");
        TextureRegion[][] e1 = TextureRegion.split(e, 16, 32);
        
        //koalaTexture = e1[0][26];
        
        jump = e1[0][26];
        stand = e1[0][27];
        
    
    }
    public boolean dead(float x, float y){
        
        boolean isDead = false;
        
        
        
        if((y < this.getY() + 1f) && (y > this.getY() + 0.5f) && (x > this.getX() - 1f) && (x < this.getX() + 1f)){
            
            if(state == NORMAL){
               koalaTexture = stand; 
            }
             
            if(state != DEAD){
               state++; 
            }
            
            isDead = true;
        }
        
        return isDead;
        
        
        
    }
    
    
    public void positionToCome(float x, float y){
       
           
        if(x == 0 && y == 0){
            positionToComeX = 0;
            positionToComeY = 0;
        } else {
            positionToComeX = this.getX() - ((this.getX() - x) / 15) + 0.07f;
            positionToComeY = this.getY() - ((this.getY() - y) / 15);
        }
    }
    
    public int getState(){
        return state;
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
        
        this.setPosition(x + xChange, y + yChange);

        xVelocity = xVelocity * DAMPING;
        if (Math.abs(xVelocity) < 0.5f) {
            xVelocity = 0;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        
        TextureRegion frame = null;
        
        if(state == NORMAL){   
            frame = jump;     
        } else if(state == HITTED || state == DEAD){   
            frame = stand;  
        }
        
        if (isFacingRight) {
            batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            batch.draw(frame, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
        } 
        
        
        
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
