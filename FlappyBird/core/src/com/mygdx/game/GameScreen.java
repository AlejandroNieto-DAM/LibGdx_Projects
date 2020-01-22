/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen implements Screen {
  	final MyGdxGame game;

	Texture dropImage;
        Texture dropImage2;
        
        Texture bg;
        
	Texture bucketImage;
        Texture down;
	Sound dropSound;
        Sound jumpSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
        Array<Rectangle> raindrops2;
	long lastDropTime;
	int dropsGathered;
        int lifes = 5;
                
        int highscore;
        
        float yVelocity = 0;
        
        float GRAVITY = -14f;
        float JUMP = 600.0f;
        
        float DISTANCE = 450;
        float VOLUME = 0.5f;

	public GameScreen(final MyGdxGame gam, int highscore) {
		this.game = gam;
                
                this.highscore = highscore;
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("tuberiaHieloAbajo.png"));
                dropImage2 = new Texture(Gdx.files.internal("tuberiaHieloArriba.png"));
                
		bucketImage = new Texture(Gdx.files.internal("supermanJump.png"));
                down = new Texture(Gdx.files.internal("supermanDown.png"));
                
                bg = new Texture(Gdx.files.internal("fondo.jpg"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("mario-coin.mp3"));
                jumpSound = Gdx.audio.newSound(Gdx.files.internal("mario-bros-jump.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("uf.mp3"));
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 40; // center the bucket horizontally
		bucket.y = 480 / 2 - 64 / 2; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		bucket.width = 50;
		bucket.height = 50;
                
		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
                raindrops2 = new Array<Rectangle>();
		spawnRaindrop();

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
                Rectangle raindrop2 = new Rectangle();
                                                                               
                
		raindrop.x = 800;
		raindrop.y = MathUtils.random(-200, 0);
		raindrop.width = 64;
		raindrop.height = 300;
		raindrops.add(raindrop);
                
                raindrop2.x = 800;
		raindrop2.y = raindrop.y + DISTANCE;
		raindrop2.width = 64;
		raindrop2.height = 300;
                raindrops2.add(raindrop2);
                
                
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
                game.batch.draw(bg, 0, 0, 800, 480);
		game.font.draw(game.batch, "SCORE: " + dropsGathered, 0, 480);

                
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
                
                for (Rectangle raindrop : raindrops2) {
			game.batch.draw(dropImage2, raindrop.x + 64, raindrop.y, -raindrop.width, raindrop.height);
		}
                
		game.batch.end();
		
                
		if (Gdx.input.isKeyJustPressed(Keys.UP)){
                    bucket.y = bucket.y + JUMP * Gdx.graphics.getDeltaTime();
                    yVelocity = 320;
                    jumpSound.play(VOLUME);
                }
			
		
		// make sure the bucket stays within the screen bounds
                
		if (bucket.y <= 0.0){
			bucket.y = 0;
                        if(dropsGathered > highscore){
                            highscore = dropsGathered;
                            try {
                                this.saveHighScore();
                            } catch (IOException ex) {
                                Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        game.setScreen(new LoseScreen(game, dropsGathered, highscore));
                        dispose();
                }
		if (bucket.y > 480 - 64)
			bucket.y = 480 - 64;

               
                
		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 900000000)
			spawnRaindrop();
                
               
                 
		
                
                
               
               
                
                yVelocity = yVelocity + GRAVITY;
                float y = bucket.getY();
                
                float yChange = yVelocity * delta;
                bucket.setPosition(bucket.x, y + yChange);
                
                Batch batch = game.batch;
                batch.begin();
		if(yVelocity >= 0){
                    batch.draw(bucketImage, bucket.x, bucket.y);
                } else {
                   batch.draw(down, bucket.x, bucket.y);
                }
		batch.end();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<Rectangle> iter = raindrops.iterator();
                
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
                        
                       
                        if(dropsGathered > 5 && dropsGathered <= 10){
                           raindrop.x -= 600 * Gdx.graphics.getDeltaTime();
                        } else if(dropsGathered > 10){
                            raindrop.x -= 700 * Gdx.graphics.getDeltaTime(); 
                        } else {
                            raindrop.x -= 500 * Gdx.graphics.getDeltaTime(); 
                        }
                        
                        
			if (raindrop.x + 64 < 0){
                            iter.remove();
                            dropsGathered += 1;
                            dropSound.play(VOLUME);
                        }
				
				
			if (raindrop.overlaps(bucket)) {
                            
                            if(dropsGathered > highscore){
                                highscore = dropsGathered;
                                try {
                                    this.saveHighScore();
                                } catch (IOException ex) {
                                    Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            game.setScreen(new LoseScreen(game, dropsGathered, highscore));
                            dispose();
                            
			}
                        
                        
		}
                
                Iterator<Rectangle> iter2 = raindrops2.iterator();
                
		while (iter2.hasNext()) {
			Rectangle raindrop = iter2.next();
                        
                       
                        
                        if(dropsGathered > 5 && dropsGathered <= 10){
                           raindrop.x -= 600 * Gdx.graphics.getDeltaTime();
                        } else if(dropsGathered > 10){
                            raindrop.x -= 700 * Gdx.graphics.getDeltaTime(); 
                        } else {
                            raindrop.x -= 500 * Gdx.graphics.getDeltaTime(); 
                        }
                        

                        
			if (raindrop.overlaps(bucket)) {
                            if(dropsGathered > highscore){
                                highscore = dropsGathered;
                            }
                            game.setScreen(new LoseScreen(game, dropsGathered, highscore));
                            dispose();
                            
			}
                        
                        
		}
	}
        
        public void saveHighScore() throws FileNotFoundException, IOException{
            File ficheroBorrar = new File("HighScore.dat");
            ficheroBorrar.delete();
            File fichero = new File("HighScore.dat");
            FileOutputStream fileout = new FileOutputStream(fichero,true);  
            ObjectOutputStream dataOS = new ObjectOutputStream(fileout);  


            dataOS.writeInt(this.highscore);
             

            dataOS.close();  //cerrar stream de salida   
        
        }

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}
