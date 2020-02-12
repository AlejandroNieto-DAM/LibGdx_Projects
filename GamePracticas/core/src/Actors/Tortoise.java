package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Tortoise extends Image {
    
    private final int NORMAL = 1;
    private final int HITTED = 2;
    private final int SPINNING = 3;
    private final int DEAD = 4;
    
    Animation walk;
    Animation spinning;
    TextureRegion tortoiseTexture;
    TextureRegion tortoiseHitTexture;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;

    Boolean cambioDireccion;
    
    public int state = NORMAL;
    
    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 5f;
    final float DAMPING = 0.87f;

    public Tortoise() {
        cambioDireccion = false;
        final float width = 16;
        final float height = 32;
        this.setSize(1, height / width);

        Texture e = new Texture("nes.png");
        TextureRegion[][] e1 = TextureRegion.split(e, 16, 32);
        
        walk = new Animation(0.15f, e1[0][6], e1[0][7]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        tortoiseHitTexture = e1[0][10];
        spinning = new Animation(0.10f, e1[0][10], e1[0][11]);
        spinning.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);  
    
    }
    
    public boolean hit(float x, float y){
        
        boolean isHitted = false;

        
        if((y < this.getY() + 1f) && (y > this.getY() + 0.5f) && (x > this.getX() - 1f) && (x < this.getX() + 1f)){
            
            if(state == NORMAL){
               tortoiseTexture = tortoiseHitTexture; 
            }
            
            if(state != DEAD){
               state++; 
            }
            
            isHitted = true;
        }
        
        return isHitted;
   
    }
    
    public int getState(){
        return state;
    }

    public void act(float delta) {
        time = time + delta;
        
        float velocidad1 = 0;
        float velocidad2 = 0;
        
        
        if(state == NORMAL){
            velocidad1 = 1 * MAX_VELOCITY; 
            velocidad2 = -1 * MAX_VELOCITY; 
        } else if (state == HITTED){
            
        } else if (state == SPINNING){
            velocidad1 = 1 * MAX_VELOCITY * 1.5f; 
            velocidad2 = -1 * MAX_VELOCITY * 1.5f; 
        }
        
        yVelocity = yVelocity + GRAVITY;

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;
 
        if (canMoveTo(x + xChange, y, false) == false) {
            if(cambioDireccion){
                cambioDireccion = false;
                isFacingRight = true;

            } else {
                cambioDireccion = true;
                isFacingRight = false;
            }
        }
          
        if(cambioDireccion){
            xVelocity = velocidad1;
        } else {
            xVelocity = velocidad2;
        }
        
        

        if (canMoveTo(x, y + yChange, yVelocity > 0) == false) {
            canJump = yVelocity < 0;
            yVelocity = yChange = 0;
        }

        this.setPosition(x + xChange, y + yChange);

        xVelocity = xVelocity * DAMPING;
        if (Math.abs(xVelocity) < 0.5f) {
            xVelocity = 0;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        
        TextureRegion frame = null;

        if(state == NORMAL){   
            frame = (TextureRegion) walk.getKeyFrame(time);       
        } else if(state == HITTED){   
            frame = tortoiseHitTexture;  
        } else if(state == SPINNING || state == DEAD){
            frame = (TextureRegion) spinning.getKeyFrame(time);
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
