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
import java.util.Iterator;

public class Map1 implements Screen {
    
    public final int minionEnemys = 10;
    public final int cannonAmmoEnemys = 5;
    
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    MyGdxGame game;
    Double tiempo;
    Double tiempoCheckCollisions;
    
    MainActor mainActor;
    
    ArrayList<Bala> cannonAmmo;
    ArrayList<Minion> minions;
    ArrayList<Seta> setas;
    ArrayList<Planta> plantas;
    
    ArrayList<Integer> positionSetasX;
    ArrayList<Integer> positionSetasY;

    TiledMapTileLayer plants;
    
    Boolean hit;
    
    Map1(MyGdxGame game){
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
  
        mainActor = new MainActor();
        mainActor.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        mainActor.setPosition(10, 10); 
        stage.addActor(mainActor);
        
        
        minions = new ArrayList();
        cannonAmmo = new ArrayList();
        plantas = new ArrayList();
        setas = new ArrayList();
        
        
        positionSetasX = new ArrayList();
        positionSetasY = new ArrayList();
        
        tiempo = 0.0;
        tiempoCheckCollisions = 0.0;
        hit = false;
        
        this.loadMap(0, 0);
        this.setasPositions();
        
        for(int i = 0; i < minionEnemys; i++){
            minionSpawn();
        }
        
        
        for(int i = 0; i < cannonAmmoEnemys; i++){
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
    
    public void spawnPlant(float x, float y){
        Planta plant = new Planta();
        
        plant.setPosition(x + 0.5f, y);
        stage.addActor(plant);
        
        plantas.add(plant);

    }
    
    
    public void minionSpawn(){
        Minion minion = new Minion();
        
        minion.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        double pos = Math.random()*239 + 30;
        int pos2 = (int) pos;
         
        minion.setPosition(pos2, 40);
        stage.addActor(minion);
        
        minions.add(minion);
    }
    
    public void setaSpawn(float x, float y){
        Seta seta = new Seta();
        
        seta.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        seta.setPosition(x, y);
        stage.addActor(seta);
        
        setas.add(seta);
    }
    
    public void canonAmmoSpawn(){
        Bala cannonAmmo = new Bala();
        
        cannonAmmo.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        double pos = Math.random()*219 + 100;
        int pos2 = (int) pos;
        double pos3 = Math.random()*4 + 8;
        int pos24 = (int) pos3;
        
        cannonAmmo.setPosition(pos2, pos24);
        stage.addActor(cannonAmmo);
        
        this.cannonAmmo.add(cannonAmmo);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.mainActorLimits();
        
        //System.out.println("Koala y " + koala.getY());
        //System.out.println("Koala x " + koala.getX());

        if(hit){
            tiempoCheckCollisions += delta;
            if(tiempoCheckCollisions > 1){
                tiempoCheckCollisions = 0.0;
                hit = false;
            }
        }
        this.checkCollisions(delta);
        
        camera.update(); 
        this.game.batch.setProjectionMatrix(camera.combined);
        
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
        this.plantsCollisions();

    }
    
    public void plantsCollisions(){
        Iterator<Planta> iter = plantas.iterator();

        while(iter.hasNext()){
            Planta a = iter.next();
               
            if(a.getY() + 1.5f > mainActor.getY() && a.getY() < mainActor.getY() && a.getX() + 0.8f > mainActor.getX() && a.getX() - 0.8f < mainActor.getX() && hit == false){
                
                if(mainActor.getState() == 1){
                    game.setScreen(new LooseScreen(game));
                    dispose();
                }
                
                if(mainActor.getState() == 2){
                    mainActor.chico();
                } 
                
                hit = true;
            } 
        }
    }
    
    public void minionCollisions(){
        
        Iterator<Minion> iter = minions.iterator();
        while(iter.hasNext()){
            Minion a = iter.next();
            
            if(a.dead(mainActor.getX(), mainActor.getY()) == true){
                mainActor.bump();
            }
            
            if(a.getY() - 1 < mainActor.getY() && a.getY() + 0.5f > mainActor.getY() && a.getX() + 0.5f > mainActor.getX() && a.getX() - 0.5f < mainActor.getX() && hit == false){
                
                if(mainActor.getState() == 1){
                    game.setScreen(new LooseScreen(game));
                    dispose();
                }
                
                if(mainActor.getState() == 2){
                    mainActor.chico();
                } 
                
                hit = true;
            } 
            
            if(a.getState() == 3){
               a.setY(-10f);
               iter.remove();
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
                    mainActor.grande();  
                    setas.get(i).remove();
                    setas.remove(i);
                }   
            } 
        }
    }
    
    public void canonAmmoCollisions(){
        for(int i = 0; i < cannonAmmo.size(); i++){
            
            if(mainActor.getX() - cannonAmmo.get(i).getX() < 3 && mainActor.getY() - cannonAmmo.get(i).getY() < 3 && cannonAmmo.get(i).getX() - mainActor.getX() < 3 && cannonAmmo.get(i).getY() - mainActor.getY() < 3){
               cannonAmmo.get(i).positionToCome(mainActor.getX(), mainActor.getY());
            }else{
                cannonAmmo.get(i).positionToCome(0, 0);
            }
        
            if(cannonAmmo.get(i).getX() + 2 > mainActor.getX() && cannonAmmo.get(i).getX() - 1.5 < mainActor.getX() && mainActor.getY() < cannonAmmo.get(i).getY() + 1.5 && cannonAmmo.get(i).getY() - 2 > mainActor.getY() && hit == false){
               
                if(mainActor.getState() == 1){
                    game.setScreen(new LooseScreen(game));
                    dispose();
                    mainActor.stunt();
                }
                
                if(mainActor.getState() == 2){
                    mainActor.chico();
                    mainActor.stunt();
                } 
                
                hit = true;
            }
            
        } 
    }
    
    public void loadMap(float startX, float startY) {
        
        TiledMapTileLayer plant = (TiledMapTileLayer)map.getLayers().get("plants");
        
        float endX = startX + plant.getWidth();
        float endY = startY + plant.getHeight();

        int x = (int) startX;
        while (x < endX) {
            int y = (int) startY;
            while (y < endY) {
                //System.out.println("Position x --> " + x);
                //System.out.println("Position y --> " + y);
                if (plant.getCell(x, y) != null ) {
                    if(plant.getProperties().get("plants", Boolean.class) == false){
                      plant.setCell(x, y, null);
                      spawnPlant(x, y);  
                    }      
                }
                y = y + 1;
            }
            x = x + 1;
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
