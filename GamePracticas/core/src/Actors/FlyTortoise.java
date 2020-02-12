package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class FlyTortoise extends Image {
    
    private final int NORMAL = 1;
    private final int HITTED = 2;
    private final int DEAD = 3;
    
    TextureRegion hitted, normal;
    
    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;
    TextureRegion flyingTortoise;
    
    float positionToComeX = 0;
    float positionToComeY = 0;
    
    boolean parar = false;
    
    
    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 5f;
    final float DAMPING = 0.87f;
    
    
    public int state = NORMAL;

    public FlyTortoise() {
        final float width = 16;
        final float height = 32;
        this.setSize(1, height / width);

        Texture e = new Texture("nes.png");
        TextureRegion[][] e1 = TextureRegion.split(e, 16, 32);
                
        normal = e1[0][26];
        hitted = e1[0][27];
        
    
    }
    public boolean hit(float x, float y){
        
        boolean hit = false;
        
        if((y < this.getY() + 1f) && (y > this.getY() + 0.5f) && (x > this.getX() - 1f) && (x < this.getX() + 1f)){
            
            if(state == NORMAL){
               flyingTortoise = hitted; 
            }
             
            if(state != DEAD){
               state++; 
            }
            
            hit = true;
        }
        
        return hit;
   
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
            frame = normal;     
        } else if(state == HITTED || state == DEAD){   
            frame = hitted;  
        }
        
        if (isFacingRight) {
            batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            batch.draw(frame, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
        }   
        
    }
}
