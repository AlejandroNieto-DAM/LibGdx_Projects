package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Bala extends Image {

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    public TiledMapTileLayer layer;
    
    TextureRegion cannonAmmo;
   
        
    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 5f;

    public Bala() {
        final float width = 16;
        final float height = 32;
        this.setSize(1, height / width);
        
        Texture e = new Texture("nes.png");
        TextureRegion[][] e1 = TextureRegion.split(e, 16, 32);
        
        cannonAmmo = e1[0][35];
        
    
    }
    
    public boolean hit(float x, float y){
        
        boolean isHitted = false;
        
        if((y < this.getY() + 1f) && (y > this.getY() + 0.3f) && (x > this.getX() - 0.8f) && (x < this.getX() + 0.8f)){ 
            isHitted = true;
        }
        
        return isHitted;
    }

    public void act(float delta) {
        time = time + delta;
        
        float x;
        float y;  

        x = this.getX();
        y = this.getY();
        
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        xVelocity = -1 * MAX_VELOCITY;
        
        this.setPosition(x + xChange, y + yChange);

    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(cannonAmmo, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

}
