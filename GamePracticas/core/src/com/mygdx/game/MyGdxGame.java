package com.mygdx.game;

import Screens.FirstScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
    
        public SpriteBatch batch;
        public BitmapFont font;

        
	public void create() {
            batch = new SpriteBatch();
            font = new BitmapFont(Gdx.files.internal("fuente_juego.fnt"));
            font.setColor(Color.BLACK);
            this.setScreen(new FirstScreen(this));
	}
}
