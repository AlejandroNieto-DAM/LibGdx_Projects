package FinalMap;

import Actors.Bala;
import Actors.Bowser;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.scenes.scene2d.*;
import Actors.MainActor;
import Actors.FireBall;
import Screens.LooseScreen;
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;
import java.util.Iterator;

public class Final implements Screen {
    
    public final int minionEnemys = 10;
    public final int numberFlyingTortoises = 10;
    
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    MyGdxGame game;

    MainActor mainActor;
 
    ArrayList<FireBall> fireBalls;
    double tiempoSpawnFireBall;
    
    Bowser ej = new Bowser();
    
    boolean accesible = false;
    
    public Final(MyGdxGame game){
        this.game = game;
    }

    @Override
    public void show() {
        
        tiempoSpawnFireBall = 0.0;
        map = new TmxMapLoader().load("Map3/Pelea.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera); 
  
        mainActor = new MainActor();
        mainActor.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        mainActor.setPosition(3, 20); 
        stage.addActor(mainActor);
        
        
        fireBalls = new ArrayList();
        
        
        
        ej.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        ej.setPosition(21, 4); 
        stage.addActor(ej);

    }
    
    
    public void spawnFireBall(float x, float y){
        FireBall fireBall = new FireBall();
        
        fireBall.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        if(ej.getFacingRight() == true){
           fireBall.setPosition(x + 1, y);
        } else {
           fireBall.setPosition(x - 1, y); 
        }
        
        if(ej.getFacingRight() == false){
                fireBall.facingRight(false);
        } else {
            fireBall.facingRight(true);
        }
        
        stage.addActor(fireBall);
        fireBalls.add(fireBall);
         
    }


    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.tiempoSpawnFireBall += delta;
        
        if(tiempoSpawnFireBall > Math.random() * 8 + 3){
            this.spawnFireBall(ej.getX(), ej.getY());
            this.tiempoSpawnFireBall = 0;
        }
        
        this.mainActorLimits();
        
        
        //System.out.println("Koala y " + mainActor.getY());
        //System.out.println("Koala x " + mainActor.getX());
        
        this.checkCollisions();

        
        ej.setFacingRight(mainActor.getX());
        
        
        camera.update(); 
        this.game.batch.setProjectionMatrix(camera.combined);
        
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();
    }
    
    public void checkCollisions(){
        this.bowserCollisions();
        this.fireBallsCollisions();
    }
    
    public void bowserCollisions(){
        ej.positionToCome(mainActor.getX(), mainActor.getY());
        
        if(ej.hit(mainActor.getX(), mainActor.getY())){
            mainActor.bump();
        } else {

            if(ej.getY() + 1f > mainActor.getY() && mainActor.getY() >= ej.getY() && ej.getX() + 1.2f > mainActor.getX() && ej.getX() - 1.2f < mainActor.getX()){

                if(mainActor.getState() == 1){
                        game.setScreen(new Final(game));
                        dispose();
                }

                if(mainActor.getState() == 2){
                    mainActor.chico();
                }     
            }
                
            if(ej.getState() == 10){
                    ej.setY(-10f);
                    accesible = true;
            }
        }
    }
    
    public void fireBallsCollisions(){
        Iterator<FireBall> iter = fireBalls.iterator();
        while(iter.hasNext()){
            FireBall a = iter.next();
 
            if(a.hit(mainActor.getX(), mainActor.getY()) == true){
                
                if(mainActor.getState() == 2){
                    mainActor.chico();
                }
                
                if(mainActor.getState() == 1){
                    game.setScreen(new LooseScreen(game));
                    dispose();
                }       
                  
            } else {
                
                if(a.getRebotes() == 6){
                    a.setX(80);
                    iter.remove(); 
                }
                
            }
            
            
        }
    }
    
    public void mainActorLimits(){
        if(mainActor.getX() > 13 && mainActor.getX() < 12){
           camera.position.x = mainActor.getX();
           
        }
           
        if(mainActor.getX() < 0){
            mainActor.setPosition(0, mainActor.getY());
        }
           
        if(mainActor.getX() > 25){
            mainActor.setPosition(12, mainActor.getY());
        }
        
        if(mainActor.getX() > 23 && mainActor.getX() < 25 && mainActor.getY() < 6 && accesible == true){
            game.setScreen(new EndGame(game));
            dispose();
        }
           
        if(mainActor.getY() < -20){
            game.setScreen(new LooseScreen(game));
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
