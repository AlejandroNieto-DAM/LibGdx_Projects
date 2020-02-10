package FinalMap;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.scenes.scene2d.*;
import Actors.MainActor;
import Actors.Minion;
import Actors.Planta;
import Actors.Seta;
import Actors.Bala;
import Actors.FireBall;
import Actors.FlyTortoise;
import Actors.Tortoise;
import Screens.LooseScreen;
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;
import java.util.Iterator;

public class Inicio implements Screen {
    
    
    
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    MyGdxGame game;
    
    
    MainActor mainActor;
    
    public Inicio(MyGdxGame game){
        this.game = game;
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("Map3/Inicio.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera); 
  
        mainActor = new MainActor();
        mainActor.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        mainActor.setPosition(4, 4); 
        stage.addActor(mainActor);
        
          
        
    }

 
    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.mainActorLimits();
           
        System.out.println("Koala y " + mainActor.getY());
        System.out.println("Koala x " + mainActor.getX());
        
        camera.update(); 
        this.game.batch.setProjectionMatrix(camera.combined);
        
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();
    }
    
    public void mainActorLimits(){
        if(mainActor.getX() > 13 && mainActor.getX() < 36){
           camera.position.x = mainActor.getX();
           
        }
           
        if(mainActor.getX() < 0){
            mainActor.setPosition(0, mainActor.getY());
        }
           
        if(mainActor.getX() > 49){
            mainActor.setPosition(49, mainActor.getY());
        }
           
        if(mainActor.getY() < -20){
            game.setScreen(new LooseScreen(game));
            dispose();
        } 
        
        
        if(mainActor.getX() > 42 && mainActor.getY() > 3 && mainActor.getY() < 5){
            game.setScreen(new Final(game));
            dispose();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 20 * width / height, 20);
    }

    @Override
    public void resume() {
    }
}
