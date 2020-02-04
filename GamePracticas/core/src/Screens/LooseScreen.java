/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;


public class LooseScreen implements Screen {
        final MyGdxGame game;
	OrthographicCamera camera;
        Texture bg;

	public LooseScreen(final MyGdxGame gam) {
		game = gam;
//                bg = new Texture(Gdx.files.internal("firstScreen.jpg"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

	}
        
        

	@Override
	public void render(float delta) {
		
            Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            

            camera.update();
            game.batch.setProjectionMatrix(camera.combined);

            game.batch.begin();
            //game.batch.draw(bg, 0, 0, 800, 480);

            game.font.draw(game.batch, "Welcome to SUPER KOALIO!!! ", 100, 150);
            game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
            game.batch.end();

            if (Gdx.input.isTouched()) {
                    game.setScreen(new MainScreen(game));
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
