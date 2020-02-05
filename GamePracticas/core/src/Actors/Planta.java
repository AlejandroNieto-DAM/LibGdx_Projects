package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Planta extends Image {
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

    public Planta() {
        cambioDireccion = false;
        final float width = 16;
        final float height = 32;
        this.setSize(1, height / width);
        
        
        Texture e = new Texture("nes.png");
        TextureRegion[][] e1 = TextureRegion.split(e, 16, 32);
        
        
        walk = new Animation(0.5f, e1[0][12], e1[0][13]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
    
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
        
    }

    public void draw(Batch batch, float parentAlpha) {
        
        TextureRegion frame = (TextureRegion) walk.getKeyFrame(time);
          
        batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());

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
