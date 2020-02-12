package Actors;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Planta extends Image {
    Animation plantAnimation;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;

    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 5f;
    final float DAMPING = 0.87f;

    public Planta() {
        
        final float width = 16;
        final float height = 32;
        this.setSize(1, height / width);
          
        Texture e = new Texture("nes.png");
        TextureRegion[][] e1 = TextureRegion.split(e, 16, 32);
   
        plantAnimation = new Animation(0.5f, e1[0][12], e1[0][13]);
        plantAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
    
    }
    
    @Override
    public void act(float delta) {
        time = time + delta;   
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        
        TextureRegion frame = (TextureRegion) plantAnimation.getKeyFrame(time);
          
        batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());

    }

}
