package Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Coin extends Image {
    Animation coinAnimation;

    float time = 0;  
    public TiledMapTileLayer layer;
    
    public Coin() {
        final float width = 0.5f;
        final float height = 0.8f;
        this.setSize(1, height / width);
           
        Texture items = loadTexture("items.png");
        
	coinAnimation = new Animation(0.2f, new TextureRegion(items, 128, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32),
			new TextureRegion(items, 192, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32));
        coinAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    
    }
    
    public Texture loadTexture (String file) {
	return new Texture(Gdx.files.internal(file));
    }

    public void act(float delta) {
        time = time + delta;    
    }

    public void draw(Batch batch, float parentAlpha) {      
        TextureRegion frame = (TextureRegion) coinAnimation.getKeyFrame(time);  
        batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());

    }
}
