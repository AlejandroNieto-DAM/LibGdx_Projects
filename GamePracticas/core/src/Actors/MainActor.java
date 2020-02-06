package Actors;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;

public class MainActor extends Image {
    TextureRegion stand;
    Animation walk, jump;
    
    TextureRegion stand1, jump1;

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
    
    public int GRANDE = 2;
    public int NORMAL = 1;
    
    public int state = NORMAL;
    
        Texture koalaTexture;

    public MainActor() {
        final float width = 18;
        final float height = 26;
        this.setSize(1, height / width);

        koalaTexture = new Texture("sonicSprites.png");
        
        stand  = new TextureRegion(koalaTexture,8,17,40,40);

        Array<TextureRegion> frames = new Array();
        //JUMPING
        for(int i = 0; i < 5;i++){
            frames.add(new TextureRegion(koalaTexture,8+40*i,330,40,40));
        }
        jump = new Animation(0.1f,frames);
        jump.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        frames.clear();
        
        for(int i = 0; i < 8;i++){
            frames.add(new TextureRegion(koalaTexture,8+44*i,66,41,40));
        }
        walk = new Animation(0.1f,frames);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        
        
    }
    
    public void grande(){
        
        Texture koalaTexture = new Texture("koalio.png");
        TextureRegion[][] grid = TextureRegion.split(koalaTexture, 18, 26);

        stand1 = grid[0][0];
        jump1 = grid[0][1];
        walk = new Animation(0.15f, grid[0][2], grid[0][3], grid[0][4]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
       
        
        state = GRANDE;
    }
    
    
    public void chico(){
        
        stand  = new TextureRegion(koalaTexture,8,17,40,40);

        Array<TextureRegion> frames = new Array();
        //JUMPING
        for(int i = 0; i < 5;i++){
            frames.add(new TextureRegion(koalaTexture,8+40*i,330,40,40));
        }
        jump = new Animation(0.1f,frames);
        jump.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        frames.clear();
        
        for(int i = 0; i < 8;i++){
            frames.add(new TextureRegion(koalaTexture,8+44*i,66,41,40));
        }
        walk = new Animation(0.1f,frames);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        state = NORMAL;
    }
    
    public int getState(){
        return state;
    }
    
    public void stunt(){
        if(isFacingRight){
            xVelocity -= 10;
        } else {
            xVelocity += 10;
        }
    }
    
    public void bump(){
        yVelocity =  0;
        yVelocity = yVelocity + MAX_VELOCITY * 4;  
        yVelocity = yVelocity + GRAVITY;
    }
    
    public float getYVelocity(){
        return yVelocity;
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
        TextureRegion frame = null;

        
        if(state == NORMAL){
            if (yVelocity != 0) {
                frame = (TextureRegion) jump.getKeyFrame(time);
            } else if (xVelocity != 0) {
                frame = (TextureRegion) walk.getKeyFrame(time);
            } else {
                frame = stand;
            }
        } else if(state == GRANDE){
            if (yVelocity != 0) {
                frame = jump1;
            } else if (xVelocity != 0) {
                frame = (TextureRegion) walk.getKeyFrame(time);
            } else {
                frame = stand1;
            }
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
