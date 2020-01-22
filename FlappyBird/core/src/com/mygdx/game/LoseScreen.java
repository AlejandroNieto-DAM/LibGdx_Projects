/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class LoseScreen implements Screen {
        final MyGdxGame game;
	OrthographicCamera camera;
        int score = 0;
        int highscore = 0;
        Texture bg;

	public LoseScreen(final MyGdxGame gam, int score, int highscore) {
		game = gam;
                bg = new Texture(Gdx.files.internal("firstScreen.jpg"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
                this.score = score;

                if(highscore > this.highscore){
                    this.highscore = highscore;
                }
                
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
                game.batch.draw(bg, 0, 0, 800, 480);

		game.font.draw(game.batch, "You Loose noob go kill yourself. Your score is:  " + this.score, 100, 200);
                game.font.draw(game.batch, "The highscore now is:  " + this.highscore, 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin again!!", 100, 100);
		game.batch.end();
                
                
                
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game, highscore));
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
