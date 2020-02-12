package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class FireBall extends Image {

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    public TiledMapTileLayer layer;
    
    TextureRegion fireBallTexture;

    int rebote = 0;

    Boolean cambioDireccion;
    
    private Boolean booleanRight = false;
    
    final float GRAVITY = -3.5f;
    final float MAX_VELOCITY = 12f;
    final float DAMPING = 0.87f;

    public FireBall() {
        cambioDireccion = false;
        final float width = 16;
        final float height = 32;
        this.setSize(1, height / width);

        Texture e = new Texture("nes.png");
        TextureRegion[][] e1 = TextureRegion.split(e, 16, 32);
        
        fireBallTexture = e1[2][28];    
    
    }
    
    public int getRebotes(){
        return rebote;
    }
    
    public void bump(){
        yVelocity =  0;
        yVelocity = yVelocity + MAX_VELOCITY * 3;  
        yVelocity = yVelocity + GRAVITY;
    }
    
    public boolean hit(float x, float y){
        
        boolean isHitted = false;

        if((y < this.getY() + 1f) && (y > this.getY() + 0.5f) && (x > this.getX() - 0.5f) && (x < this.getX() + 0.5f)){
            isHitted = true;
        }
        
        return isHitted;
        
    }
    
    public void facingRight(boolean right){
        if(right == true){
            booleanRight = false;
        } else {
            booleanRight = true;
        }
    }

    public void act(float delta) {
        time = time + delta;
        
        if(this.getY() < 3.2){
            this.bump();
            rebote++;
        }
        
         float velocidad1 = 0;
         float velocidad2 = 0;
         
        if(booleanRight){
            velocidad1 = 1  * MAX_VELOCITY; 
            velocidad2 = -1  * MAX_VELOCITY; 
        } else {
            velocidad1 = -1  * MAX_VELOCITY; 
            velocidad2 = 1  * MAX_VELOCITY; 
        }
        
        yVelocity = yVelocity + GRAVITY;

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;
  
        if (canMoveTo(x + xChange, y, false) == false) {
            if(cambioDireccion ){
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
        
        if (canMoveTo(x, y + yChange, yVelocity > 0) == false) {
            
            yVelocity = yChange = 0;
        }

        this.setPosition(x + xChange, y + yChange);

    }

    public void draw(Batch batch, float parentAlpha) { 
        batch.draw(fireBallTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());   
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
                        //layer.setCell(x, y, null);
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
