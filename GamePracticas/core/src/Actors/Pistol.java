package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Pistol extends Image {
    TextureRegion stand, jump;
    Animation walk;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;
    Texture koalaTexture;

    Boolean cambioDireccion;
    
    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 5f;
    final float DAMPING = 0.87f;

    public Pistol() {
        cambioDireccion = false;
        final float width = 18;
        final float height = 26;
        this.setSize(1, height / width);

        koalaTexture = new Texture("pistol.png");
        
    
    }

    public void act(float delta) {
        time = time + delta;
        
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
            yVelocity = yChange = 0;
        }

        this.setPosition(x + xChange, y + yChange);

        xVelocity = xVelocity * DAMPING;
        if (Math.abs(xVelocity) < 0.5f) {
            xVelocity = 0;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        
        if (isFacingRight) {
            batch.draw(koalaTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            batch.draw(koalaTexture, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
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
