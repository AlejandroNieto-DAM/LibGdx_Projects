package Screens;

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
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;

public class MainScreen implements Screen {
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    MyGdxGame game;
    Double tiempo;
    
    MainActor mainActor;
    
    ArrayList<Bala> canonAmmo;
    ArrayList<Minion> minions;
    ArrayList<Seta> setas;
    
    ArrayList<Integer> positionSetasX;
    ArrayList<Integer> positionSetasY;

    
    Planta a;
    
    MainScreen(MyGdxGame game){
        this.game = game;
    }

    
    @Override
    public void show() {
        map = new TmxMapLoader().load("mapy.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera);

        positionSetasX = new ArrayList();
        positionSetasY = new ArrayList();
  
        mainActor = new MainActor();
         
        minions = new ArrayList();
        canonAmmo = new ArrayList();
        a = new Planta();
        setas = new ArrayList();
        
        mainActor.layer = (TiledMapTileLayer) map.getLayers().get("walls");

        mainActor.setPosition(10, 10);
        a.setPosition(41.5f, 6);
        
        stage.addActor(mainActor);
        stage.addActor(a);
        
        tiempo = 0.0;
        
        for(int i = 0; i < 10; i++){
            minionSpawn();
        }
        
        
        for(int i = 0; i < 5; i++){
            canonAmmoSpawn();
        }
        
    }
    
    public void setasPositions(){
        positionSetasX.add(24);
        positionSetasY.add(13);
        
        positionSetasX.add(57);
        positionSetasY.add(13);
        
        positionSetasX.add(109);
        positionSetasY.add(15);
    }
    
    public void minionSpawn(){
        Minion p = new Minion();
        
        p.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        double pos = Math.random()*239 + 30;
        int pos2 = (int) pos;
        
        
        p.setPosition(pos2, 40);
        stage.addActor(p);
        
        minions.add(p);
    }
    
    public void setaSpawn(float x, float y){
        Seta p = new Seta();
        
        p.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        p.setPosition(x, y);
        stage.addActor(p);
        
        setas.add(p);
    }
    
    public void canonAmmoSpawn(){
        Bala p = new Bala();
        
        p.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        double pos = Math.random()*219 + 100;
        int pos2 = (int) pos;
        double pos3 = Math.random()*4 + 8;
        int pos24 = (int) pos3;
        
        p.setPosition(pos2, pos24);
        stage.addActor(p);
        
        canonAmmo.add(p);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.mainActorLimits();
        
        //System.out.println("Koala y " + koala.getY());
        //System.out.println("Koala x " + koala.getX());

        this.checkCollisions(delta);
        
        camera.update(); 
        
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();
    }
    
    public void mainActorLimits(){
        if(mainActor.getX() > 13 && mainActor.getX() < 227){
           camera.position.x = mainActor.getX();
           
        }
           
        if(mainActor.getX() < 0){
            mainActor.setPosition(0, mainActor.getY());
        }
           
        if(mainActor.getX() > 239){
            mainActor.setPosition(227, mainActor.getY());
        }
           
        if(mainActor.getY() < -20){
            game.setScreen(new LooseScreen(game));
            dispose();
        } 
        
        
        if(mainActor.getX() > 233){
            game.setScreen(new LooseScreen(game));
            dispose();
        }
    }
    
    public void checkCollisions(float delta){
        tiempo += delta;
        
        this.setasCollisions();
        this.minionCollisions();
        this.canonAmmoCollisions();
        
        
        
        if(a.getX() + 0.5 > mainActor.getX() && a.getX() - 0.5 < mainActor.getX() && a.getY() + 1 > mainActor.getY()){
            game.setScreen(new LooseScreen(game));
            dispose();
        }
  
    }
    
    public void minionCollisions(){
        for(int i = 0;  i < minions.size(); i++){
            if(minions.get(i).dead(mainActor.getX(), mainActor.getY()) == true){
                mainActor.bump();
            }
        }
        
        for(int i = 0;  i < minions.size(); i++){
            if(minions.get(i).getState() == 3){
                minions.get(i).remove();
            }
        }
    }
    
    public void setasCollisions(){
        for(int i = 0 ; i < positionSetasX.size(); i++){
            if(mainActor.getX() < positionSetasX.get(i) + 1 && mainActor.getX() > positionSetasX.get(i) - 1 && mainActor.getY() > positionSetasY.get(i) - 2 && mainActor.getY() < positionSetasX.get(i)){
                setaSpawn(positionSetasX.get(i), positionSetasY.get(i));
                positionSetasX.remove(i);
                positionSetasY.remove(i);                
            }
        }
        
        for(int i = 0;  i < setas.size(); i++){
            
            if(setas.get(i).dead(mainActor.getX(), mainActor.getY()) == true){
                
                if(mainActor.getState() == 1){ 
                    System.out.println("Entramos aqui eyyo");
                    mainActor.grande();  
                    setas.get(i).remove();
                    setas.remove(i);
                }   
            } 
        }
    }
    
    public void canonAmmoCollisions(){
        for(int i = 0; i < canonAmmo.size(); i++){
            
            if(mainActor.getX() - canonAmmo.get(i).getX() < 3 && mainActor.getY() - canonAmmo.get(i).getY() < 3 && canonAmmo.get(i).getX() - mainActor.getX() < 3 && canonAmmo.get(i).getY() - mainActor.getY() < 3){
               canonAmmo.get(i).positionToCome(mainActor.getX(), mainActor.getY());
            }else{
                canonAmmo.get(i).positionToCome(0, 0);
            }
        
            if(mainActor.getX() - canonAmmo.get(i).getX() < 1.5 && mainActor.getY() - canonAmmo.get(i).getY() < 1.5 && canonAmmo.get(i).getX() - mainActor.getX() < 1.5 && canonAmmo.get(i).getY() - mainActor.getY() < 1.5){
                   mainActor.stunt();
            }   
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
