package Actors;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Bowser extends Image {
    TextureRegion jump;
    Animation walk;
    
    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    
    public TiledMapTileLayer layer;

    final float GRAVITY = -1.5f;
    final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.57f;
    
    private int state = 0;
     
    boolean cambioDireccion;
    
    double tiempoSalto = 0.0;
    double tiempoComportamiento = 0.0;
    
    float positionToComeX = 0.0f;
    float positionToComeY = 0.0f;

       
    Texture bowser;

    public Bowser() {
        cambioDireccion = false;
        final float width = 18;
        final float height = 26;
        this.setSize(1, height / width);


        Texture koalaTexture = new Texture("koalio.png");
        TextureRegion[][] grid = TextureRegion.split(koalaTexture, 18, 26);

        jump = grid[0][1];
        walk = new Animation(0.15f, grid[0][2], grid[0][3], grid[0][4]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
 
    }
    
    public int getState(){
        return state;
    }
    
    public void bump(){
        yVelocity =  0;
        yVelocity = yVelocity + MAX_VELOCITY * 4;  
        
    }
    
    public boolean getFacingRight(){
        return isFacingRight;
    }
     
    public void setFacingRight(float x){
        if(x < this.getX()){
           isFacingRight = false;

        } else {
           isFacingRight = true;
        }
    }
    
     
    public boolean hit(float x, float y){
        
        boolean isHitted = false;
  
        if((y < this.getY() + 1.5f) && (y > this.getY() + 0.5f) && (x > this.getX() - 1f) && (x < this.getX() + 1f)){
             state++; 
            isHitted = true;
        }
        
        return isHitted;

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

    public void act(float delta) {
        time = time + delta;
        tiempoSalto += delta;
        
        yVelocity = yVelocity + GRAVITY;
        
        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        
        if(this.tiempoComportamiento < 10){
            
            if(tiempoSalto > Math.random() * 10 + 4){
                this.bump();
                tiempoSalto = 0;
            }
         
            float velocidad1 = 1 * MAX_VELOCITY; 
            float velocidad2 = -1 * MAX_VELOCITY; 

            if (canMoveTo(x + xChange, y, false) == false) {
                if(cambioDireccion){
                    cambioDireccion = false;
                } else {
                    cambioDireccion = true;
                }
            }

            if(cambioDireccion){
                xVelocity = velocidad1;
            } else {
                xVelocity = velocidad2;
            }
            
            this.tiempoComportamiento += delta;
            
            
        } else if(tiempoComportamiento >= 10 && tiempoComportamiento <= 15){
            
            if(positionToComeX != 0){
                x = positionToComeX;
                y = positionToComeY;  
            } else {
               x = this.getX();
               y = this.getY();
            }

           if (canMoveTo(x + xChange, y, false) == false) {
               xVelocity = xChange = 0;
           }
            
            this.tiempoComportamiento += delta;

        }
        
        if(this.tiempoComportamiento > 15){
            this.tiempoComportamiento = 0;
        }
        
        //System.out.println("tiempoCompt + " + tiempoComportamiento1);
        
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

        
        if (yVelocity != 0) {
            frame = jump;
        } else if (xVelocity != 0) {
            frame = (TextureRegion) walk.getKeyFrame(time);
        } else {
            frame = jump;
        }
        

        if (isFacingRight) {
            batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            batch.draw(frame, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
        }
    }

    private boolean canMoveTo(float startX, float startY, boolean shouldDestroy) {
        
        if(this.layer != null){
            float endX = startX + this.getWidth();
            float endY = startY + this.getHeight();

            int x = (int) startX;
            while (x < endX) {

                int y = (int) startY;
                while (y < endY) {
                    if (layer.getCell(x, y) != null ) {
                        if (shouldDestroy) {
                            //layer.setCell(x, y, null);
                        }
                        return false;
                    }
                    y = y + 1;
                }
                x = x + 1;
            }

            return true;
        } else {
            return false;
        }
        
    }
}
