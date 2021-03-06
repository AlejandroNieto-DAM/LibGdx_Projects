/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuScreen implements Screen {
        final MyGdxGame game;
	OrthographicCamera camera;
        Texture bg;
        int highscore;

	public MainMenuScreen(final MyGdxGame gam) {
		game = gam;
                bg = new Texture(Gdx.files.internal("firstScreen.jpg"));


		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
                this.highscore = 0;

	}
        
        

	@Override
	public void render(float delta) {
		
            

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
                game.batch.draw(bg, 0, 0, 800, 480);

		game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game, this.highscore));
			dispose();
		}
	}
        
         

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
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
	}
        

           
}
