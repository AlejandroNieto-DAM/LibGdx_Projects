package Actors;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Bowser extends Image {
    TextureRegion stand;
    Animation walk, jump;
    
    TextureRegion stand1, jump1;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;

    final float GRAVITY = -1.5f;
    final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.57f;
    
    public int GRANDE = 2;
    public int NORMAL = 1;
    
    public int state = NORMAL;
    
    boolean cambioDireccion;
    
    double tiempoSalto = 0.0;

    
    public Boolean hitState = false;
    
        Texture koalaTexture;

    public Bowser() {
        cambioDireccion = false;
        final float width = 18;
        final float height = 26;
        this.setSize(1, height / width);


        Texture koalaTexture = new Texture("koalio.png");
        TextureRegion[][] grid = TextureRegion.split(koalaTexture, 18, 26);

        stand1 = grid[0][0];
        jump1 = grid[0][1];
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
    
    public float getYVelocity(){
        return yVelocity;
    }
    
    public void setFacingRight(float x){
        if(x < this.getX()){
           isFacingRight = false;

        } else {
           isFacingRight = true;
        }
    }
    
    public boolean dead(float x, float y){
        
        boolean isDead = false;
        
        //System.out.println("xS " + this.getX());
        //System.out.println("y " + this.getY());
        
        if((y < this.getY() + 1f) && (y > this.getY() + 0.5f) && (x > this.getX() - 1f) && (x < this.getX() + 1f)){
            // System.out.println("yeyo entrao aqui en el hit");
               state++; 
 
            isDead = true;
        }
        
        return isDead;
        
        
        
    }

    public void act(float delta) {
        time = time + delta;
        tiempoSalto += delta;
        
        
        if(tiempoSalto > Math.random() * 10 + 4){
            this.bump();
            tiempoSalto = 0;
        }
        
        yVelocity = yVelocity + GRAVITY;
        
        float velocidad1 = 1 * MAX_VELOCITY; 
        float velocidad2 = -1 * MAX_VELOCITY; 
        

        yVelocity = yVelocity + GRAVITY;

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

         
        
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

        if (canMoveTo(x, y + yChange, yVelocity > 0) == false) {
            canJump = yVelocity < 0;
            if(!canJump){
                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    if (canJump) {
                        yVelocity = yVelocity + MAX_VELOCITY * 4;     
                    }   
                    canJump = false;
                }
            }
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
            frame = jump1;
        } else if (xVelocity != 0) {
            frame = (TextureRegion) walk.getKeyFrame(time);
        } else {
            frame = stand1;
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
                            layer.setCell(x, y, null);
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
