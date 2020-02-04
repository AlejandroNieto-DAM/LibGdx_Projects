package Actors;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Koala extends Image {
    TextureRegion stand, jump;
    Animation walk;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true;
    public TiledMapTileLayer layer;
    float doubleJump = 0;

    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.87f;

    public Koala() {
        final float width = 363;
        final float height = 458;
        this.setSize(1, height / width);

        Texture koalaTexture = new Texture("1.png");
        TextureRegion[][] grid = TextureRegion.split(koalaTexture, (int) width, (int) height);

        Texture jumpTexture = new Texture("jump.png");
        TextureRegion[][] grid2 = TextureRegion.split(jumpTexture, 362, 483);
        jump = grid2[0][0];
        
        Texture standTexture = new Texture("stand.png");
        TextureRegion[][] grid3 = TextureRegion.split(standTexture, 232, 439);
        stand = grid3[0][0];
        
        
        walk = new Animation(0.05f, grid[0][0], grid[0][1], grid[0][2], grid[0][3], grid[0][4], grid[1][0], grid[1][1], grid[1][2], grid[1][3], grid[1][4]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
    }
    
    public void stunt(){
        if(isFacingRight){
            xVelocity -= 10;
        } else {
            xVelocity += 10;
        }
    }

    public void act(float delta) {
        time = time + delta;
        
        
        boolean upTouched = Gdx.input.isTouched() && Gdx.input.getY() < Gdx.graphics.getHeight() / 2;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
            if (canJump) {
                yVelocity = yVelocity + MAX_VELOCITY * 4;     
            } 
            
            canJump = false;    
        }
        

        boolean leftTouched = Gdx.input.isTouched() && Gdx.input.getX() < Gdx.graphics.getWidth() / 3;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftTouched) {
            xVelocity = -1 * MAX_VELOCITY;
            isFacingRight = false;
        }

        boolean rightTouched = Gdx.input.isTouched() && Gdx.input.getX() > Gdx.graphics.getWidth() * 2 / 3;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightTouched) {
            xVelocity = MAX_VELOCITY;
            isFacingRight = true;
        }

        yVelocity = yVelocity + GRAVITY;

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        if (canMoveTo(x + xChange, y, false) == false) {
            xVelocity = xChange = 0;
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
        TextureRegion frame;

        if (yVelocity != 0) {
            frame = jump;
        } else if (xVelocity != 0) {
            frame = (TextureRegion) walk.getKeyFrame(time);
        } else {
            frame = stand;
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
