package Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.scenes.scene2d.*;
import Actors.Koala;
import Actors.Pistol;
import Actors.Tiraco;
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;

public class MainScreen implements Screen {
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    Koala koala;
    ArrayList<Pistol> pistol;
    MyGdxGame game;
    Double tiempo;
    
    Tiraco t;
    
    MainScreen(MyGdxGame game){
        this.game = game;
    }

    public void show() {
        map = new TmxMapLoader().load("level1.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera);

        koala = new Koala();
        pistol = new ArrayList();
        t = new Tiraco();
        
        koala.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        t.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        
        koala.setPosition(20, 10);
        t.setPosition(29, 10);
        
        
        stage.addActor(koala);
        stage.addActor(t);
        
        tiempo = 0.0;
        
    }
    
    public void pistolSpawn(float position){
        Pistol p = new Pistol();
        
        p.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        p.setPosition(position + 8, 20);
        stage.addActor(p);
        
        pistol.add(p);
    }
    
    public void checkCollisions(){
        for(int i = 0;  i < pistol.size(); i++){
           if(koala.getX() - pistol.get(i).getX() < 0.5 && koala.getY() - pistol.get(i).getY() < 0.5 && pistol.get(i).getX() - koala.getX() < 0.5 && pistol.get(i).getY() - koala.getY() < 0.5){
               koala.stunt();
            } 
        }
        
        if(koala.getX() - t.getX() < 3 && koala.getY() - t.getY() < 3 && t.getX() - koala.getX() < 3 && t.getY() - koala.getY() < 3){
               t.positionToCome(koala.getX(), koala.getY());
        }else{
            t.positionToCome(0, 0);
        }
        
        
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(koala.getX() > 13 && koala.getX() < 199){
           camera.position.x = koala.getX();
        }
        
        if(koala.getY() < -20){
            game.setScreen(new LooseScreen(game));
            dispose();
        } 
        
        
        
        
        tiempo += delta;
        double tiempoAleatorio = Math.random()*5 + 2;
        //System.out.println("Delta "  + tiempo);
        
        if(tiempo >= tiempoAleatorio){
            tiempo = 0.0;
            //this.pistolSpawn(camera.position.x);
        }
        
        //System.out.println("Koala y " + koala.getY());
        //System.out.println("Koala x " + koala.getX());

        //System.out.println("Pistol y " + pistol.getY());
        //System.out.println("Pistol x " + pistol.getX());

        
        this.checkCollisions();
        
        camera.update(); 
        
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, 20 * width / height, 20);
    }

    public void resume() {
    }
}
